package com.rosterloh.mirror.di.component;

import com.rosterloh.mirror.main.MainActivity;
import com.rosterloh.mirror.di.PerActivity;
import com.rosterloh.mirror.di.module.MainModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
