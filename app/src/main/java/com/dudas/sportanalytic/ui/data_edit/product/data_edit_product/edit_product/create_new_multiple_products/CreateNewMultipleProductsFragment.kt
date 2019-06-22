package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_multiple_products

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
import kotlinx.android.synthetic.main.product_new_multiple_fargment.*

class CreateNewMultipleProductsFragment : BaseFragment() {

    private lateinit var binding: com.dudas.sportanalytic.databinding.ProductNewMultipleFargmentBinding
    private lateinit var createNewMultipleProductsFragmentViewModel: CreateNewMultipleProductsFragmentViewModel

    companion object {
        fun newInstance() = CreateNewMultipleProductsFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        createNewMultipleProductsFragmentViewModel = ViewModelProviders
            .of(this@CreateNewMultipleProductsFragment,
                CreateNewMultipleProductsFragmentViewModelFactory(sportAnalyticService,
                    preferences,
                    connector)
            )
            .get(CreateNewMultipleProductsFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.product_new_multiple_fargment, container, false)
        binding.lifecycleOwner = this@CreateNewMultipleProductsFragment
        binding.viewModel = createNewMultipleProductsFragmentViewModel

        if(createNewMultipleProductsFragmentViewModel.product.value == null) {
            createNewMultipleProductsFragmentViewModel.newProduct(this.arguments?.getString(DataEditConstants.PRODUCT_CATEGORIES_ID)!!)
        }

        createNewMultipleProductsFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                product_progress_bar.visibility = View.VISIBLE
                sv_new_edit_product.visibility = View.GONE
            } else {
                product_progress_bar.visibility = View.GONE
                sv_new_edit_product.visibility = View.VISIBLE
                createNewMultipleProductsFragmentViewModel.closeFragment.postValue(true)
            }
        })

        createNewMultipleProductsFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        createNewMultipleProductsFragmentViewModel.enableInsertUpdateButton.observe(this, Observer {
            product_insert_save.isEnabled = it
        })

        createNewMultipleProductsFragmentViewModel.closeFragment.observe(this, Observer {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            fragmentManager?.popBackStack()
        })

        return binding.root
    }
}