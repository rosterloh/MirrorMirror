package com.rosterloh.mirror.services;

import android.app.Application;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import rx.Observable;
import rx.Subscriber;

public class MqttService implements IMqttActionListener, MqttCallbackExtended, MqttTraceHandler {

    private Application application;
    private MqttAndroidClient mqttAndroidClient;
    private MqttManagerListener mListener;
    private static final String clientId = MqttClient.generateClientId();
    private static final String clientTopic = "home/2/bath/motion";
    private static final String TAG = "MqttService";
    private MqqtConnectionStatus clientStatus = MqqtConnectionStatus.NONE;
    private Observable<String> event;

    public enum MqqtConnectionStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        ERROR,
        NONE
    }

    public MqttService(Application application) {

        this.application = application;
    }

    public Observable<String> observableListenerWrapper() {

        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {

                MqttManagerListener listener = new MqttManagerListener() {

                    @Override
                    public void onMqttDisconnected() {

                    }

                    @Override
                    public void onMqttConnected() {

                    }

                    @Override
                    public void onMqttMessageArrived(String topic, String payload) {
                        subscriber.onNext(payload);
                    }
                };

                setListener(listener);
            }
        });
    }

    public void setListener(MqttManagerListener listener) {
        mListener = listener;
    }

    public void subscribe(String topic, int qos) {

        if (mqttAndroidClient != null && clientStatus == MqqtConnectionStatus.CONNECTED && topic != null) {
            try {
                Log.d(TAG, "Mqtt: subscribe to " + topic + " qos:" + qos);
                mqttAndroidClient.subscribe(topic, qos);
            } catch (MqttException e) {
                Log.e(TAG, "Mqtt:x subscribe error: ", e);
            }
        }
    }

    public void unsubscribe(String topic) {

        if (mqttAndroidClient != null && clientStatus == MqqtConnectionStatus.CONNECTED && topic != null) {
            try {
                Log.d(TAG, "Mqtt: unsubscribe from " + topic);
                mqttAndroidClient.unsubscribe(topic);
            } catch (MqttException e) {
                Log.e(TAG, "Mqtt:x unsubscribe error: ", e);
            }
        }

    }

    public void publish(String topic, String payload, int qos) {
        if (mqttAndroidClient != null && clientStatus == MqqtConnectionStatus.CONNECTED && topic != null) {
            boolean retained = false;

            try {
                Log.d(TAG, "Mqtt: publish " + payload + " for topic " + topic + " qos:" + qos);
                mqttAndroidClient.publish(topic, payload.getBytes(), qos, retained, null, null);
            } catch (MqttException e) {
                Log.e(TAG, "Mqtt:x publish error: ", e);
            }
        }
    }

    public void disconnect() {

        if (mqttAndroidClient != null && clientStatus == MqqtConnectionStatus.CONNECTED) {
            try {
                Log.d(TAG, "Mqtt: disconnect");
//              clientStatus = MqqtConnectionStatus.DISCONNECTING;
                clientStatus = MqqtConnectionStatus.DISCONNECTED;      // Note: it seems that the disconnected callback is never invoked. So we fake here that the final state is disconnected
                mqttAndroidClient.disconnect(null, this);

                mqttAndroidClient.unregisterResources();
                mqttAndroidClient = null;
            } catch (MqttException e) {
                Log.e(TAG, "Mqtt:x disconnection error: ", e);
            }
        }
    }

    public void connect(String host, int port, String username, String password, boolean cleanSession, boolean sslConnection) {

        final int timeout = MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT;
        final int keepalive = MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;

        String lastWillMessage = null;
        String lastWillTopic = null;
        int qos = 0;
        boolean retained = false;

        String uri;
        if (sslConnection) {
            uri = "ssl://" + host + ":" + port;

        } else {
            uri = "tcp://" + host + ":" + port;
        }

        Log.d(TAG, "Mqtt: Create client: " + clientId);
        mqttAndroidClient = new MqttAndroidClient(application, uri, clientId);
        mqttAndroidClient.registerResources(application);

        MqttConnectOptions conOpt = new MqttConnectOptions();
        Log.d(TAG, "Mqtt: clean session:" +(cleanSession?"yes":"no"));
        conOpt.setCleanSession(cleanSession);
        conOpt.setAutomaticReconnect(true);
        conOpt.setConnectionTimeout(timeout);
        conOpt.setKeepAliveInterval(keepalive);
        if (username != null && username.length() > 0) {
            Log.d(TAG, "Mqtt: username: " + username);
            conOpt.setUserName(username);
        }
        if (password != null && password.length() > 0) {
            Log.d(TAG, "Mqtt: password: " + password);
            conOpt.setPassword(password.toCharArray());
        }

        boolean doConnect = true;
        if ((lastWillMessage != null && lastWillMessage.length() > 0) || (lastWillTopic != null && lastWillTopic.length() > 0)) {
            // need to make a message since last will is set
            Log.d(TAG, "Mqtt: setwill");
            try {
                conOpt.setWill(lastWillTopic, lastWillMessage.getBytes(), qos, retained);
            } catch (Exception e) {
                Log.e(TAG, "Mqtt: Can't set will", e);
                doConnect = false;
            }
        }
        mqttAndroidClient.setCallback(this);
        mqttAndroidClient.setTraceCallback(this);

        if (doConnect) {

            try {
                Log.d(TAG, "Mqtt: connect to " + uri);
                clientStatus = MqqtConnectionStatus.CONNECTING;
                mqttAndroidClient.connect(conOpt, null, this);
            } catch (MqttException e) {
                Log.e(TAG, "Mqtt: connection error: ", e);
            }
        }
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

        if (clientStatus == MqqtConnectionStatus.CONNECTING) {

            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
            disconnectedBufferOptions.setBufferEnabled(true);
            disconnectedBufferOptions.setBufferSize(100);
            disconnectedBufferOptions.setPersistBuffer(false);
            disconnectedBufferOptions.setDeleteOldestMessages(false);
            mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

            Log.d(TAG, "Mqtt connect onSuccess");
            clientStatus = MqqtConnectionStatus.CONNECTED;
            if (mListener != null) mListener.onMqttConnected();
            subscribe(clientTopic, 0);
        } else if (clientStatus == MqqtConnectionStatus.DISCONNECTING) {

            Log.d(TAG, "Mqtt disconnect onSuccess");
            clientStatus = MqqtConnectionStatus.DISCONNECTED;
            if (mListener != null) mListener.onMqttDisconnected();
        } else {

            Log.d(TAG, "Mqtt unknown onSuccess");
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

        Log.d(TAG, "Mqtt onFailure. " + exception);
        clientStatus = MqqtConnectionStatus.ERROR;
        if (mListener != null) mListener.onMqttDisconnected();
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            Log.d(TAG, "Reconnected to : " + serverURI);
        } else {
            Log.d(TAG, "Connected to: " + serverURI);
            subscribe(clientTopic, 0);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

        Log.d(TAG, "Mqtt connectionLost. " + cause);
        clientStatus = MqqtConnectionStatus.DISCONNECTED;
        if (mListener != null) {
            mListener.onMqttDisconnected();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String payload = new String(message.getPayload());

        // filter cleared messages (to avoid duplicates)
        if(payload.length() > 0) {

            Log.d(TAG, "Mqtt messageArrived from topic: " + topic + " message: " + payload + " isDuplicate: " + (message.isDuplicate() ? "yes" : "no"));

            if (mListener != null) {
                mListener.onMqttMessageArrived(topic, payload);
            }

            // Fix duplicated messages clearing the received payload and processing only non null messages
            message.clearPayload();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

        Log.d(TAG, "Mqtt deliveryComplete");
    }

    @Override
    public void traceDebug(String source, String message) {

        Log.d(TAG, "Mqtt traceDebug: Source: " + source + " Message: " + message);
    }

    @Override
    public void traceError(String source, String message) {

        Log.d(TAG, "Mqtt traceError: Source: " + source + " Message: " + message);
    }

    @Override
    public void traceException(String source, String message, Exception e) {

        Log.d(TAG, "Mqtt traceException: Source: " + source + " Message: " + message);
    }

    public interface MqttManagerListener {
        void onMqttConnected();

        void onMqttDisconnected();

        void onMqttMessageArrived(String topic, String payload);
    }
}
