package com.rosterloh.mirror.presenters;

import android.app.Application;
import android.os.Build;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.interactor.MainInteractor;
import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.util.Constants;
import com.rosterloh.mirror.activity.MainActivity;
import com.rosterloh.mirror.views.MainView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class MainPresenterImpl implements MainPresenter, RecognitionListener, TextToSpeech.OnInitListener {

    private MainView view;
    private MainInteractor interactor;
    private Application application;
    private Configuration configuration;
    private SpeechRecognizer recognizer;
    private TextToSpeech textToSpeech;

    public MainPresenterImpl(MainView view, MainInteractor interactor, Application application) {

        this.view = view;
        this.interactor = interactor;
        this.application = application;
    }

    @Override
    public void setConfiguration(Configuration configuration) {

        this.configuration = configuration;
    }

    @Override
    public void start(boolean hasAccessToCalendar) {
        if (null != configuration) {
            startWeather();
            if (configuration.isVoiceCommands()) {
                initSpeechRecognitionService();
                setupTts();
            }
            startReddit();
            if (hasAccessToCalendar) {
                startCalendar();
            }
            //configuration.getServerAddress()
            interactor.getMqtt().connect("", 1883, null, null, false, false);
        }
    }

    private void updateData() {
        interactor.unSubscribe();
        if (null != configuration) {
            startWeather();
            startReddit();
            startCalendar();
        }
    }

    @Override
    public void showError(String error) {
        view.showError(error);
    }

    @Override
    public void finish() {
        interactor.getMqtt().disconnect();
        tearDownSpeechService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        interactor.unSubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    private Observable<Void> tearDownSpeechService() {
        return Observable.defer(() -> {
            if (recognizer != null) {
                recognizer.cancel();
                recognizer.shutdown();
            }
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
            view.hideListening();
            view.hideMap();
            return Observable.empty();
        });
    }

    private void startWeather() {
        interactor.loadWeather(configuration.getLocation(), configuration.isCelsius(), configuration.getPollingDelay(), ((MainActivity) view).getString(R.string.forecast_api_key), new WeatherSubscriber());
    }

    private void startReddit() {
        interactor.loadTopRedditPost(configuration.getSubreddit(), configuration.getPollingDelay(), new RedditSubscriber());
    }

    private void startCalendar() {
        interactor.loadLatestCalendarEvent(configuration.getPollingDelay(), new CalendarEventSubscriber());
    }

    private void initSpeechRecognitionService() {
        interactor.getAssetsDirForSpeechRecognizer(new AssetSubscriber());
    }

    private void setupTts() {
        initTts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    public void setupRecognizer(File assetDir) {
        initRecognizer(assetDir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        setListeningMode(Constants.KWS_SEARCH);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private Observable<Void> initTts() {
        return Observable.defer(() -> {
            textToSpeech = new TextToSpeech(application, this);
            return Observable.empty();
        });
    }

    private Observable<Void> initRecognizer(File assetDir) {
        return Observable.defer(() -> {
            try {
                recognizer = defaultSetup()
                        .setAcousticModel(new File(assetDir, "en-us-ptm"))
                        .setDictionary(new File(assetDir, "cmudict-en-us.dict"))
                        .setKeywordThreshold(1e-45f)
                        .getRecognizer();
                recognizer.addListener(this);
                recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, Constants.KEYPHRASE);
                recognizer.addKeywordSearch(Constants.COMMANDS_SEARCH, new File(assetDir, "commands.gram"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Observable.empty();
        });
    }

    public void setListeningMode(String mode) {
        recognizer.stop();
        if (mode.equals(Constants.KWS_SEARCH)) {
            recognizer.startListening(mode);
        } else {
            recognizer.startListening(mode, (int) TimeUnit.MINUTES.toMillis(1));
        }
    }

    private void processVoiceCommand(String command) {
        switch (command) {
            case Constants.KEYPHRASE:
                // wake up and listen for commands
                speak(Constants.WAKE_NOTIFICATION);
                setListeningMode(Constants.COMMANDS_SEARCH);
                view.showListening();
                break;
            case Constants.SLEEP_PHRASE:
                // go to sleep
                speak(Constants.SLEEP_NOTIFICATION);
                setListeningMode(Constants.KWS_SEARCH);
                view.hideListening();
                break;
            case Constants.UPDATE_PHRASE:
                // update data
                speak(Constants.UPDATE_NOTIFICATION);
                updateData();
                // go to sleep again and wait for activation phrase
                setListeningMode(Constants.KWS_SEARCH);
                break;
            case Constants.MAP_PHRASE:
                speak(Constants.MAP_NOTIFICATION);
                setListeningMode(Constants.COMMANDS_SEARCH);
                showMap();
                break;
        }
    }

    private void showMap() {

        timeOut(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {

                    @Override
                    public void onStart() {
                        view.showMap(configuration.getLocation());
                    }

                    @Override
                    public void onCompleted() {
                        view.hideMap();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private Observable<Void> timeOut(Integer seconds) {
        return Observable.defer(() -> {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(seconds));
            return Observable.empty();
        });
    }

    @SuppressWarnings("deprecation")
    public void speak(String sentence) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String utteranceId = this.hashCode() + "";
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) return;
        String command = hypothesis.getHypstr();
        processVoiceCommand(command);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    @Override
    public void onError(Exception e) {
        showError(e.getLocalizedMessage());
        Log.e(MainActivity.class.getSimpleName(), e.toString());
    }

    @Override
    public void onTimeout() {
        speak(Constants.SLEEP_NOTIFICATION);
        setListeningMode(Constants.KWS_SEARCH);
        view.hideListening();
    }

    @Override
    public void onInit(int status) {
    }

    private final class WeatherSubscriber extends Subscriber<Weather> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(Weather weather) {
            view.displayCurrentWeather(weather);
        }
    }

    private final class RedditSubscriber extends Subscriber<RedditPost> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(RedditPost redditPost) {
            view.displayTopRedditPost(redditPost);
        }
    }

    private final class CalendarEventSubscriber extends Subscriber<String> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(String event) {
            view.displayLatestCalendarEvent(event);
        }
    }

    private final class AssetSubscriber extends Subscriber<File> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(File assetDir) {
            setupRecognizer(assetDir);
        }
    }
}
