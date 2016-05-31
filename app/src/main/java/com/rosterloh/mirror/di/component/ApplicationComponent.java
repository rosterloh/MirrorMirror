package com.rosterloh.mirror.di.component;

import com.rosterloh.mirror.di.module.AppModule;
import com.rosterloh.mirror.di.module.MainModule;
import com.rosterloh.mirror.di.module.ServiceModule;
import com.rosterloh.mirror.di.module.SetupModule;
import com.rosterloh.mirror.di.module.StorageModule;
import com.rosterloh.mirror.di.module.UtilModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, StorageModule.class, UtilModule.class, ServiceModule.class})
public interface ApplicationComponent {

    SetupComponent plus(SetupModule setupModule);

    MainComponent plus(MainModule mainModule);
}
