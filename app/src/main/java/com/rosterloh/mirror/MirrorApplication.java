package com.rosterloh.mirror;

import android.app.Application;

import com.rosterloh.mirror.di.component.ApplicationComponent;
import com.rosterloh.mirror.di.component.DaggerApplicationComponent;
import com.rosterloh.mirror.di.component.MainComponent;
import com.rosterloh.mirror.di.component.SetupComponent;
import com.rosterloh.mirror.di.module.AppModule;
import com.rosterloh.mirror.di.module.MainModule;
import com.rosterloh.mirror.di.module.MqttModule;
import com.rosterloh.mirror.di.module.ServiceModule;
import com.rosterloh.mirror.di.module.SetupModule;
import com.rosterloh.mirror.di.module.StorageModule;
import com.rosterloh.mirror.di.module.UtilModule;
import com.rosterloh.mirror.views.MainView;
import com.rosterloh.mirror.views.SetupView;

public class MirrorApplication extends Application {

    private ApplicationComponent applicationComponent;
    private SetupComponent setupComponent;
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule())
                .storageModule(new StorageModule())
                .mqttModule(new MqttModule())
                .utilModule(new UtilModule())
                .build();
    }

    public SetupComponent createSetupComponent(SetupView view) {
        setupComponent = applicationComponent.plus(new SetupModule(view));
        return setupComponent;
    }

    public MainComponent createMainComponent(MainView view) {
        mainComponent = applicationComponent.plus(new MainModule(view));
        return mainComponent;
    }

    public void releaseSetupComponent() {
        setupComponent = null;
    }

    public void releaseMainComponent() {
        mainComponent = null;
    }
}
