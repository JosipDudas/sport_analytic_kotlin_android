package com.dudas.sportanalytic.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.ActivityMainBinding
import com.dudas.sportanalytic.ui.BaseDrawerActivity

class MainActivity : BaseDrawerActivity(), MainActivityViewModel.CallBack {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainActivityViewModel(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.app_name)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun getToolbarName(): String {
        return getString(R.string.app_name)
    }
}