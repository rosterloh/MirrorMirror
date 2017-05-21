package com.rosterloh.mirror.ui.common;

import android.support.v4.app.FragmentManager;

import com.rosterloh.mirror.MainActivity;
import com.rosterloh.mirror.R;
import com.rosterloh.mirror.ui.mirror.MirrorFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {

    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToMirror() {
        MirrorFragment mirrorFragment = new MirrorFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, mirrorFragment)
                .commitAllowingStateLoss();
    }
/*
    public void navigateToRepo(String owner, String name) {
        RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }*/
}
