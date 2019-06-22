package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.new_edit_product

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
import kotlinx.android.synthetic.main.product_editor_fragment.*

class EditProductFragment : BaseFragment() {

    private lateinit var binding: com.dudas.sportanalytic.databinding.ProductEditorFragmentBinding
    private lateinit var editProductFragmentViewModel: EditProductFragmentViewModel

    companion object {
        fun newInstance() = EditProductFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        editProductFragmentViewModel = ViewModelProviders
            .of(this@EditProductFragment,
                EditProductFragmentViewModelFactory(sportAnalyticService,
                    preferences,
                    connector)
            )
            .get(EditProductFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.product_editor_fragment, container, false)
        binding.lifecycleOwner = this@EditProductFragment
        binding.viewModel = editProductFragmentViewModel

        editProductFragmentViewModel.productId.value = this.arguments!!.getString(DataEditConstants.PRODUCT_ID)!!

        if (this.arguments != null) {
            if (editProductFragmentViewModel.onChangeOrientation.value == null) {
                editProductFragmentViewModel.editProduct(this.arguments?.getString(
                    DataEditConstants.PRODUCT_ID)!!)
                editProductFragmentViewModel.onChangeOrientation.postValue(true)
            }
            binding.productInsertSave.isEnabled = true
            binding.productInsertSave.text = getString(R.string.save)
        }

        editProductFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                product_progress_bar.visibility = View.VISIBLE
                sv_new_edit_product.visibility = View.GONE
            } else {
                product_progress_bar.visibility = View.GONE
                sv_new_edit_product.visibility = View.VISIBLE
                editProductFragmentViewModel.closeFragment.postValue(true)
            }
        })

        editProductFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        editProductFragmentViewModel.enableInsertUpdateButton.observe(this, Observer {
            product_insert_save.isEnabled = it
        })

        editProductFragmentViewModel.closeFragment.observe(this, Observer {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            fragmentManager?.popBackStack()
        })

        return binding.root
    }
}