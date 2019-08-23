package com.dudas.sportanalytic.ui.main

import android.app.ActionBar
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.ActivityMainBinding
import com.dudas.sportanalytic.ui.BaseDrawerActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseDrawerActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        viewModel = ViewModelProviders
            .of(this@MainActivity, MainActivityViewModelFactory(preferences, sportAnalyticService, connector))
            .get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            this.lifecycleOwner = this@MainActivity
            this.viewModel = viewModel
        }

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.app_name)
        }

        viewModel.checkIfUserChooseLocation()

        viewModel.locationMessage.observe(this, Observer {
            if(it) {
                toast(getString(R.string.location_message))
                nav_menu.menu.findItem(R.id.nav_data_edit).isVisible = !it
                nav_menu.menu.findItem(R.id.nav_reports).isVisible = !it
            } else {
                nav_menu.menu.findItem(R.id.nav_data_edit).isVisible = !it
                nav_menu.menu.findItem(R.id.nav_reports).isVisible = !it
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIfUserChooseLocation()
    }

    override fun getToolbarName(): String {
        return getString(R.string.app_name)
    }
}