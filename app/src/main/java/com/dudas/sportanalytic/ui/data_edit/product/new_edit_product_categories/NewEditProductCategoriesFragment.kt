package com.dudas.sportanalytic.ui.data_edit.product.new_edit_product_categories

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
import com.dudas.sportanalytic.databinding.ProductCategoriesNewEditFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.product_categories_new_edit_fragment.*

class NewEditProductCategoriesFragment : BaseFragment() {

    private lateinit var binding: ProductCategoriesNewEditFragmentBinding
    private lateinit var newEditProductCategoriesFragmentViewModel: NewEditProductCategoriesFragmentViewModel

    companion object {
        fun newInstance() = NewEditProductCategoriesFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newEditProductCategoriesFragmentViewModel = ViewModelProviders
            .of(this@NewEditProductCategoriesFragment,
                NewEditProductCategoriesFragmentViewModelFactory(sportAnalyticService,
                    preferences,
                    connector)
            )
            .get(NewEditProductCategoriesFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.product_categories_new_edit_fragment, container, false)
        binding.lifecycleOwner = this@NewEditProductCategoriesFragment
        binding.viewModel = newEditProductCategoriesFragmentViewModel

        if (this.arguments != null) {
            if (newEditProductCategoriesFragmentViewModel.onChangeOrientation.value == null) {
                newEditProductCategoriesFragmentViewModel.editProductCategories(this.arguments?.getString(
                    DataEditConstants.PRODUCT_CATEGORIES_ID)!!)
                newEditProductCategoriesFragmentViewModel.onChangeOrientation.postValue(true)
            }
            binding.productCategoriesInsertSave.isEnabled = true
            binding.productCategoriesInsertSave.text = getString(R.string.save)
        } else {
            if(newEditProductCategoriesFragmentViewModel.productCategories.value == null) {
                newEditProductCategoriesFragmentViewModel.newProductCategories()
            }
        }

        newEditProductCategoriesFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                product_categories_progress_bar.visibility = View.VISIBLE
                sv_new_edit_product_categories.visibility = View.GONE
            } else {
                product_categories_progress_bar.visibility = View.GONE
                sv_new_edit_product_categories.visibility = View.VISIBLE
                newEditProductCategoriesFragmentViewModel.closeFragment.postValue(true)
            }
        })

        newEditProductCategoriesFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        newEditProductCategoriesFragmentViewModel.enableInsertUpdateButton.observe(this, Observer {
            product_categories_insert_save.isEnabled = it
        })

        newEditProductCategoriesFragmentViewModel.closeFragment.observe(this, Observer {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            fragmentManager?.popBackStack()
        })

        return binding.root
    }
}