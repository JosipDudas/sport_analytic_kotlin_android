package com.dudas.sportanalytic.ui.registration

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.ActivityRegistrationBinding
import com.dudas.sportanalytic.ui.BaseActivity
import com.dudas.sportanalytic.ui.login.LoginActivity
import com.dudas.sportanalytic.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_registration.*
import org.jetbrains.anko.*

class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        val registrationViewModel = ViewModelProviders
            .of(this@RegistrationActivity, RegistrationViewModelFactory(preferences, sportAnalyticService, connector))
            .get(RegistrationViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityRegistrationBinding>(this, R.layout.activity_registration).apply {
            this.lifecycleOwner = this@RegistrationActivity
            this.viewModel = registrationViewModel
        }

        registrationViewModel.enableRegisterButton.observe(this, Observer {
            nd_registration.isEnabled = it
        })

        registrationViewModel.registerIsSuccess.observe(this, Observer {
            if (it) {
                toast(getString(R.string.succesfuly_register))
                startActivity<LoginActivity>()
                finish()
            }
        })

        registrationViewModel.progress.observe(this, Observer {
            toolbarProgress.visibility = if(it) View.VISIBLE else View.GONE
        })

        registrationViewModel.error.observe(this, Observer {
            toast(it.message.toString())
        })
    }

    override fun getToolbarName(): String {
        return getString(R.string.app_name)
    }

    override fun onBackPressed() {
        startActivity<LoginActivity>()
        finish()
    }
}