package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_product

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
import kotlinx.android.synthetic.main.product_new_one_fragment.*

class CreateNewProductFragment : BaseFragment() {

    private lateinit var binding: com.dudas.sportanalytic.databinding.ProductNewOneFragmentBinding
    private lateinit var createNewProductFragmentViewModel: CreateNewProductFragmentViewModel

    companion object {
        fun newInstance() = CreateNewProductFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        createNewProductFragmentViewModel = ViewModelProviders
            .of(this@CreateNewProductFragment,
                CreateNewProductFragmentViewModelFactory(sportAnalyticService,
                    preferences,
                    connector)
            )
            .get(CreateNewProductFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.product_new_one_fragment, container, false)
        binding.lifecycleOwner = this@CreateNewProductFragment
        binding.viewModel = createNewProductFragmentViewModel

        if(createNewProductFragmentViewModel.product.value == null) {
            createNewProductFragmentViewModel.newProduct(this.arguments?.getString(DataEditConstants.PRODUCT_CATEGORIES_ID)!!)
        }

        createNewProductFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                product_progress_bar.visibility = View.VISIBLE
                sv_new_edit_product.visibility = View.GONE
            } else {
                product_progress_bar.visibility = View.GONE
                sv_new_edit_product.visibility = View.VISIBLE
                createNewProductFragmentViewModel.closeFragment.postValue(true)
            }
        })

        createNewProductFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        createNewProductFragmentViewModel.enableInsertUpdateButton.observe(this, Observer {
            product_insert_save.isEnabled = it
        })

        createNewProductFragmentViewModel.closeFragment.observe(this, Observer {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            fragmentManager?.popBackStack()
        })

        return binding.root
    }
}