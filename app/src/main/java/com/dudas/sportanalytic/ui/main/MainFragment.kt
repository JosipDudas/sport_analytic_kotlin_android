package com.dudas.sportanalytic.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.MainFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment: BaseFragment() {
    lateinit var mainFragmentViewModel: MainFragmentViewModel
    lateinit var binding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainFragmentViewModel = ViewModelProviders
            .of(this@MainFragment, MainFragmentViewModelFactory(preferences, connector, sportAnalyticService))
            .get(MainFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this@MainFragment
        binding.viewModel = mainFragmentViewModel

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (preferences.getLocation() != null) {
            txt_main_fragment.text = getString(R.string.location_is) + preferences.getLocation()!!.name
        } else {
            txt_main_fragment.text = getString(R.string.location_is_unknown)
        }
    }
}