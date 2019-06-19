package com.dudas.sportanalytic.ui.data_edit.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.databinding.LocationEditFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.location_edit_fragment.*
import javax.inject.Inject

class DataEditLocationFragment: BaseFragment(), LocationEditAdapter.AdapterCallBack {

    private lateinit var binding: LocationEditFragmentBinding
    private lateinit var locationEditAdapter: LocationEditAdapter
    private lateinit var locationsEditFragmentViewModel: DataEditLocationFragmentViewModel
    @Inject
    lateinit var locationsEditFragmentViewModelFactory: LocationsEditFragmentViewModelFactory

    companion object {
        fun newInstance() = DataEditLocationFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        locationsEditFragmentViewModel = ViewModelProviders
            .of(this@DataEditLocationFragment, locationsEditFragmentViewModelFactory)
            .get(DataEditLocationFragmentViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.location_edit_fragment, container, false)
        binding.lifecycleOwner = this@DataEditLocationFragment
        binding.viewModel = locationsEditFragmentViewModel

        locationsEditFragmentViewModel.fetchLocationData()

        locationsEditFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                progress_bar_location.visibility = View.VISIBLE
                location_list.visibility = View.GONE
            } else {
                progress_bar_location.visibility = View.GONE
                setAdapter()
                location_list.visibility = View.VISIBLE
            }
        })

        locationsEditFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        locationsEditFragmentViewModel.deleteFab.observe(this, Observer {
            multipleDeleteClick(it)
        })

        locationsEditFragmentViewModel.deleteConfirmFab.observe(this, Observer {
            if (it) {
                multipleDeleteConfirmClick()
            }
        })

        binding.fabNew.setOnClickListener{
            locationsEditFragmentViewModel.deleteFab.postValue(false)
            /*fragmentManager!!
                .beginTransaction()
                .replace(R.id.main_content, NewEditLocationFragment.newInstance())
                .addToBackStack(null)
                .commit()*/
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        /*if(locationsEditFragmentViewModel.deletedlocationList.value != null){
            locationEditAdapter.deleteLocationList = locationsEditFragmentViewModel.deletedLocationList.value!!
        }
        locationEditAdapter.updateListAndRefresh(locationsEditFragmentViewModel.getAllNonDeletedLocations())*/
    }

    override fun onDestroy() {
        super.onDestroy()
        //locationsEditFragmentViewModel.deletedLocationList.value = locationEditAdapter.deleteLocationList
    }

    private fun setAdapter() {
        locationEditAdapter = LocationEditAdapter(
            activity!!,
            preferences,
            connector.locationDao().getAllLocations(),
            eventBus,
            this)
        binding.rvLocations.adapter = locationEditAdapter
        binding.rvLocations.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.rvLocations.layoutManager = LinearLayoutManager(activity)
    }

    private fun multipleDeleteClick(checkBoxVisibility: Boolean) {
        /*locationEditAdapter.checkBox = checkBoxVisibility
        locationEditAdapter.notifyDataSetChanged()
        if (checkBoxVisibility) {
            fab_delete_confirm.show()
            fab_delete.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        } else {
            fab_delete_confirm.hide()
            fab_delete.setImageResource(android.R.drawable.ic_menu_delete)
            locationsEditFragmentViewModel.deletedLocationList.postValue(null)
        }*/
    }

    private fun multipleDeleteConfirmClick(){
        /*locationsEditFragmentViewModel.delete(locationEditAdapter.deleteLocationList)
        locationEditAdapter.deleteLocationList.clear()
        locationEditAdapter.updateListAndRefresh(locationsEditFragmentViewModel.getAllNonDeletedLocations())*/
    }

    override fun onSpecificlocationClick(locationId: String) {
        /*val newEditLocationFragment = NewEditLocationFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(DataEditConstants.LOCATION_ID, locationId)
        newEditLocationFragment.arguments = bundle
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, newEditLocationFragment)
            .addToBackStack(null)
            .commit()*/
    }
}