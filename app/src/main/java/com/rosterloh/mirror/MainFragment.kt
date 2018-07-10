package com.rosterloh.mirror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.rosterloh.mirror.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MirrorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val factory = InjectorUtils.provideMirrorViewModelFactory(requireActivity())
        val mirrorViewModel = ViewModelProviders.of(this, factory)
                    .get(MirrorViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentMainBinding>(
               inflater, R.layout.fragment_main, container, false).apply {
            viewModel = mirrorViewModel
            setLifecycleOwner(this@MainFragment)
        }
        return binding.root
    }
}