package com.dudas.sportanalytic.ui.data_edit.location.new_edit_location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.constants.DataEditConstants
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.location_new_edit_fragment.*

class NewEditLocationFragment: BaseFragment() {

    private lateinit var binding: com.dudas.sportanalytic.databinding.LocationNewEditFragmentBinding
    private lateinit var newEditLocationFragmentViewModel: NewEditLocationFragmentViewModel

    companion object {
        fun newInstance() = NewEditLocationFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newEditLocationFragmentViewModel = ViewModelProviders
            .of(this@NewEditLocationFragment,
                NewEditLocationFragmentViewModelFactory(sportAnalyticService,
                    preferences,
                    connector))
            .get(NewEditLocationFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.location_new_edit_fragment, container, false)
        binding.lifecycleOwner = this@NewEditLocationFragment
        binding.viewModel = newEditLocationFragmentViewModel

        if (this.arguments != null) {
            if (newEditLocationFragmentViewModel.onChangeOrientation.value == null) {
                newEditLocationFragmentViewModel.editLocation(this.arguments?.getString(DataEditConstants.LOCATION_ID)!!)
                newEditLocationFragmentViewModel.onChangeOrientation.postValue(true)
            }
            binding.npInsertSave.isEnabled = true
            binding.npInsertSave.text = getString(R.string.save)
        } else {
            if(newEditLocationFragmentViewModel.location.value == null) {
                newEditLocationFragmentViewModel.newLocation()
            }
        }

        newEditLocationFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                location_progress_bar.visibility = View.VISIBLE
                sv_new_edit_location.visibility = View.GONE
            } else {
                location_progress_bar.visibility = View.GONE
                sv_new_edit_location.visibility = View.VISIBLE
                newEditLocationFragmentViewModel.closeFragment.postValue(true)
            }
        })

        newEditLocationFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        newEditLocationFragmentViewModel.enableInsertUpdateButton.observe(this, Observer {
            np_insert_save.isEnabled = it
        })

        newEditLocationFragmentViewModel.closeFragment.observe(this, Observer {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            fragmentManager?.popBackStack()
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }
}