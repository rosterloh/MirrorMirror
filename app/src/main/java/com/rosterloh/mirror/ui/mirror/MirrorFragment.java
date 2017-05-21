package com.rosterloh.mirror.ui.mirror;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.binding.FragmentDataBindingComponent;
import com.rosterloh.mirror.databinding.MirrorFragmentBinding;
import com.rosterloh.mirror.di.Injectable;
import com.rosterloh.mirror.ui.common.NavigationController;
import com.rosterloh.mirror.util.AutoClearedValue;

import javax.inject.Inject;

public class MirrorFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<MirrorFragmentBinding> binding;

    //AutoClearedValue<RepoListAdapter> adapter;

    private MirrorViewModel mirrorViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MirrorFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.mirror_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mirrorViewModel = ViewModelProviders.of(this, viewModelFactory).get(MirrorViewModel.class);
        /*initRecyclerView();
        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, true,
                repo -> navigationController.navigateToRepo(repo.owner.login, repo.name));
        binding.get().repoList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        initSearchInputListener();

        binding.get().setCallback(() -> searchViewModel.refresh());*/
    }
}
