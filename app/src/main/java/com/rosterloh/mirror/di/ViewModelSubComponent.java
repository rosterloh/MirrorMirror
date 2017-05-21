package com.rosterloh.mirror.di;

import com.rosterloh.mirror.ui.mirror.MirrorViewModel;

import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link com.rosterloh.mirror.viewmodel.MirrorViewModelFactory}. Using this component allows
 * ViewModels to define {@link javax.inject.Inject} constructors.
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }
    MirrorViewModel mirrorViewModel();
    //UserViewModel userViewModel();
    //RepoViewModel repoViewModel();
}
