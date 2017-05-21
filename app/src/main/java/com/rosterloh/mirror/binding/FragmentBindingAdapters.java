package com.rosterloh.mirror.binding;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {

    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }
    //@BindingAdapter("imageUrl")
    //public void bindImage(ImageView imageView, String url) {
    //    Glide.with(fragment).load(url).into(imageView);
    //}
}
