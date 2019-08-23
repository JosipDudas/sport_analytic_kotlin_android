package com.dudas.sportanalytic.ui.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.constants.EventConstants
import com.dudas.sportanalytic.eventbus.MessageEvent
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment: BaseFragment() {
    private lateinit var binding: com.dudas.sportanalytic.databinding.FragmentLocationBinding
    private lateinit var locationFragmentViewModel: LocationFragmentViewModel

    companion object {
        fun newInstance() = LocationFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        locationFragmentViewModel = ViewModelProviders
            .of(this@LocationFragment, LocationFragmentViewModelFactory(preferences, connector, sportAnalyticService))
            .get(LocationFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        binding.lifecycleOwner = this@LocationFragment
        binding.viewModel = locationFragmentViewModel

        locationFragmentViewModel.getAllLocation()

        locationFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                sv_location_progress_bar.visibility = View.VISIBLE
                sv_location.visibility = View.GONE
            } else {
                sv_location_progress_bar.visibility = View.GONE
                sv_location.visibility = View.VISIBLE
            }
        })

        locationFragmentViewModel.locations.observe(this, Observer {
            addItemsOnSpinner()
            addListenerOnSpinnerItemSelection()
        })

        locationFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        locationFragmentViewModel.saveButton.observe(this, Observer {
            if (it) {
                eventBus.post(MessageEvent(EventConstants.UPDATE_LOCATION, null))
                fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        })

        return binding.root
    }

    private fun addItemsOnSpinner() {
        val locationsName = mutableListOf<String>()
        for(i in 0 until locationFragmentViewModel.locations.value!!.locations!!.size) {
            locationsName.add(locationFragmentViewModel.locations.value!!.locations!![i].name)
        }
        val dataAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item, locationsName
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_location.adapter = dataAdapter

        if (preferences.getLocation() != null) {
            for(i in 0 until locationsName.size){
                if (preferences.getLocation()!!.name == locationsName[i]) {
                    spinner_location.setSelection(i)
                }
            }
        }
    }

    private fun addListenerOnSpinnerItemSelection() {
        spinner_location.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                locationFragmentViewModel.saveLocation(pos)
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

}