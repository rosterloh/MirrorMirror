package com.rosterloh.mirror.di.component;

import com.rosterloh.mirror.activity.SetupActivity;
import com.rosterloh.mirror.di.PerActivity;
import com.rosterloh.mirror.di.module.SetupModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = SetupModule.class)
public interface SetupComponent {

    void inject(SetupActivity setupActivity);
}