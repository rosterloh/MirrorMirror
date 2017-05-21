package com.rosterloh.mirror.di;

import com.rosterloh.mirror.ui.mirror.MirrorFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    /*@ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();
*/
    @ContributesAndroidInjector
    abstract MirrorFragment contributeMirrorFragment();
}
