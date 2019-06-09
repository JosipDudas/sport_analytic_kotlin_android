package com.dudas.sportanalytic.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.ActivityRegistrationBinding
import com.dudas.sportanalytic.ui.BaseActivity
import com.dudas.sportanalytic.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_registration.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        registrationViewModel = ViewModelProviders
            .of(this@RegistrationActivity, RegistrationViewModelFactory(preferences, sportAnalyticService, connector))
            .get(RegistrationViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityRegistrationBinding>(this, R.layout.activity_registration).apply {
            this.lifecycleOwner = this@RegistrationActivity
            this.viewModel = registrationViewModel
        }

        registrationViewModel.getCompanies()

        registrationViewModel.companies.observe(this, Observer {
            addItemsOnSpinner()
            addListenerOnSpinnerItemSelection()
        })

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

    private fun addItemsOnSpinner() {
        val companiesName = mutableListOf<String>()
        for(i in 0 until registrationViewModel.companies.value!!.companies!!.size) {
            companiesName.add(registrationViewModel.companies.value!!.companies!![i].name)
        }
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, companiesName
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_company.adapter = dataAdapter
    }

    private fun addListenerOnSpinnerItemSelection() {
        spinner_company.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                registrationViewModel.saveCompanyId(pos)
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

    }
}