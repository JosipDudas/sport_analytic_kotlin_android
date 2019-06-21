package com.dudas.sportanalytic.ui.data_edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.data_edit.location.DataEditLocationFragment
import com.dudas.sportanalytic.ui.data_edit.product.DataEditProductCategoriesFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.DataEditProductFragment
import kotlinx.android.synthetic.main.data_edit_main_fragment.view.*

class DataEditMainFragment: BaseFragment(){

    private lateinit var model: DataEditMainFragmentViewModel
    private lateinit var binding: com.dudas.sportanalytic.databinding.DataEditMainFragmentBinding
    private lateinit var dataEditOptionButtons: Array<String>

    companion object {
        fun newInstance() = DataEditMainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.data_edit_main_fragment, container, false)

        model = ViewModelProviders.of(activity!!).get(DataEditMainFragmentViewModel::class.java)

        binding.viewModel = model

        dataEditOptionButtons = resources.getStringArray(R.array.data_edit_array)

        binding.root.lv_data_edit_main.adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, dataEditOptionButtons)

        binding.root.lv_data_edit_main.setOnItemClickListener { _, _, position, _ ->
            dataEditAction(position)
        }

        return binding.root
    }

    private fun dataEditAction(optionPosition: Int) {
        when(optionPosition){
            // Location edit option
            0 -> {
                if (preferences.getUser()!!.position == "user") {
                    Toast.makeText(context!!, R.string.admin_alert, Toast.LENGTH_LONG).show()
                } else {
                    openFragment(DataEditLocationFragment.newInstance())
                }
            }
            // Product Categories edit option
            1 -> {
                if (preferences.getLocation() == null) {
                    Toast.makeText(context!!, R.string.location_message, Toast.LENGTH_LONG).show()
                }else {
                    openFragment(DataEditProductCategoriesFragment.newInstance())
                }
            }
            // Products edit option
            2 -> {
                if (preferences.getLocation() == null) {
                    Toast.makeText(context!!, R.string.location_message, Toast.LENGTH_LONG).show()
                }else {
                    openFragment(DataEditProductFragment.newInstance())
                }
            }
        }
    }

    private fun openFragment(fragmentToOpen: Fragment) {
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, fragmentToOpen)
            .addToBackStack(null)
            .commit()
    }
}