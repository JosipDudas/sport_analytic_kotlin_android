package com.dudas.sportanalytic.ui.login

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.test.espresso.idling.CountingIdlingResource
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.ActivityLoginBinding
import com.dudas.sportanalytic.ui.BaseActivity
import com.dudas.sportanalytic.ui.main.MainActivity
import com.dudas.sportanalytic.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    val mIdlingRes = CountingIdlingResource("login", true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        val loginViewModel = ViewModelProviders
                .of(this@LoginActivity, LoginViewModelFactory(preferences, sportAnalyticService, connector))
                .get(LoginViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            this.lifecycleOwner = this@LoginActivity
            this.viewModel = loginViewModel
        }

        loginViewModel.enableLoginButton.observe(this, Observer {
            nd_login.isEnabled = it
        })

        loginViewModel.progress.observe(this, Observer {
            if (it) {
                mIdlingRes.increment()
            } else {
                mIdlingRes.decrement()
            }
            toolbarProgress.visibility = if(it) View.VISIBLE else View.GONE
        })

        loginViewModel.error.observe(this, Observer {
            toast(it.message.toString())
        })

        loginViewModel.loginIsSuccess.observe(this, Observer {
            if (it) {
                startActivity(intentFor<MainActivity>().clearTask().newTask())
            }
        })
        loginViewModel.startRegistrationActivity.observe(this, Observer {
            if(it) {
                startActivity<RegistrationActivity>()
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (preferences.getUser()!=null) {
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }
    }

    override fun onStart() {
        super.onStart()
        if (preferences.getUser()!=null) {
            startActivity(intentFor<MainActivity>().clearTask().newTask())
        }
    }

    override fun getToolbarName(): String {
        return getString(R.string.app_name)
    }
}