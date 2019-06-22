package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.constants.DataEditConstants
import com.dudas.sportanalytic.databinding.ProductEditListFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_multiple_products.CreateNewMultipleProductsFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.create_new_product.CreateNewProductFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.new_edit_product.EditProductFragment
import kotlinx.android.synthetic.main.product_edit_list_fragment.*

class EditListProductFragment : BaseFragment(), ProductEditListAdapter.AdapterCallBack {

    private lateinit var binding: ProductEditListFragmentBinding
    private lateinit var productEditAdapter: ProductEditListAdapter
    private lateinit var editListProductFragmentViewModel: EditListProductFragmentViewModel

    companion object {
        fun newInstance() = EditListProductFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        editListProductFragmentViewModel = ViewModelProviders
            .of(this@EditListProductFragment, EditListProductFragmentViewModelFactory(connector, preferences,sportAnalyticService))
            .get(EditListProductFragmentViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.product_edit_list_fragment, container, false)
        binding.lifecycleOwner = this@EditListProductFragment
        binding.viewModel = editListProductFragmentViewModel

        editListProductFragmentViewModel.categoryId.value = this.arguments!!.getString(DataEditConstants.PRODUCT_CATEGORIES_ID)!!

        editListProductFragmentViewModel.fetchProductData()

        editListProductFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                progress_bar_product.visibility = View.VISIBLE
                product_list.visibility = View.GONE
            } else {
                progress_bar_product.visibility = View.GONE
                setAdapter()
                product_list.visibility = View.VISIBLE
            }
        })

        editListProductFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        editListProductFragmentViewModel.deleteFab.observe(this, Observer {
            multipleDeleteClick(it)
        })

        editListProductFragmentViewModel.deleteConfirmFab.observe(this, Observer {
            if (it) {
                multipleDeleteConfirmClick()
            }
        })

        binding.fabNew.setOnClickListener{
            editListProductFragmentViewModel.deleteFab.postValue(false)
            showDialog()
        }

        editListProductFragmentViewModel.deleteIsSuccesfullyDone.observe(this, Observer {
            if (it) {
                productEditAdapter.updateListAndRefresh(editListProductFragmentViewModel.getAllNonDeletedProducts())
                productEditAdapter.deleteProductList.clear()
            }
        })

        return binding.root
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.product_create_options))
        builder.setMessage(getString(R.string.product_options))
        builder.setPositiveButton(getString(R.string.first_product_option)){dialog, which ->
            dialog.cancel()
            val createNewProductFragment = CreateNewProductFragment.newInstance()
            val bundle = Bundle()
            bundle.putString(DataEditConstants.PRODUCT_CATEGORIES_ID, editListProductFragmentViewModel.categoryId.value)
            createNewProductFragment.arguments = bundle
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.main_content, createNewProductFragment)
                .addToBackStack(null)
                .commit()
        }

        builder.setNegativeButton(getString(R.string.second_product_option)){dialog,which ->
            dialog.cancel()
            val createNewMultipleProductsFragment = CreateNewMultipleProductsFragment.newInstance()
            val bundle = Bundle()
            bundle.putString(DataEditConstants.PRODUCT_CATEGORIES_ID, editListProductFragmentViewModel.categoryId.value)
            createNewMultipleProductsFragment.arguments = bundle
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.main_content, createNewMultipleProductsFragment)
                .addToBackStack(null)
                .commit()
        }
        builder.setNeutralButton(getString(R.string.cancel)){dialog,_ -> dialog.cancel()}
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if(editListProductFragmentViewModel.deletedProductList.value != null){
            productEditAdapter.deleteProductList = editListProductFragmentViewModel.deletedProductList.value!!
        }
        setAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        editListProductFragmentViewModel.deletedProductList.value = productEditAdapter.deleteProductList
    }

    private fun setAdapter() {
        productEditAdapter = ProductEditListAdapter(
            activity!!,
            preferences,
            connector.productDao().getSpecificProducts(editListProductFragmentViewModel.categoryId.value!!),
            eventBus,
            this)
        binding.rvProducts.adapter = productEditAdapter
        binding.rvProducts.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.rvProducts.layoutManager = LinearLayoutManager(activity)
    }

    private fun multipleDeleteClick(checkBoxVisibility: Boolean) {
        productEditAdapter.checkBox = checkBoxVisibility
        productEditAdapter.notifyDataSetChanged()
        if (checkBoxVisibility) {
            fab_delete_confirm.show()
            fab_delete.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        } else {
            fab_delete_confirm.hide()
            fab_delete.setImageResource(android.R.drawable.ic_menu_delete)
            editListProductFragmentViewModel.deletedProductList.postValue(null)
        }
    }

    private fun multipleDeleteConfirmClick(){
        editListProductFragmentViewModel.deletedProductList.value = productEditAdapter.deleteProductList
        editListProductFragmentViewModel.delete()
    }

    override fun onSpecificproductClick(productId: String) {
        val newEditProductFragment = EditProductFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(DataEditConstants.PRODUCT_ID, productId)
        newEditProductFragment.arguments = bundle
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, newEditProductFragment)
            .addToBackStack(null)
            .commit()
    }
}