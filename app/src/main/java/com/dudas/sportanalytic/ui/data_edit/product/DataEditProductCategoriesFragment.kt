package com.dudas.sportanalytic.ui.data_edit.product

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
import com.dudas.sportanalytic.constants.DataEditConstants
import com.dudas.sportanalytic.databinding.ProductCategorieEditFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.data_edit.product.new_edit_product_categories.NewEditProductCategoriesFragment
import kotlinx.android.synthetic.main.location_edit_fragment.fab_delete
import kotlinx.android.synthetic.main.location_edit_fragment.fab_delete_confirm
import kotlinx.android.synthetic.main.product_categorie_edit_fragment.*
import javax.inject.Inject

class DataEditProductCategoriesFragment: BaseFragment(), ProductCategoriesEditAdapter.AdapterCallBack {

    private lateinit var binding: ProductCategorieEditFragmentBinding
    private lateinit var productEditAdapter: ProductCategoriesEditAdapter
    private lateinit var productsEditFragmentViewModel: DataEditProductCategoriesFragmentViewModel
    @Inject
    lateinit var productsEditFragmentViewModelFactory: ProductsEditFragmentViewModelFactory

    companion object {
        fun newInstance() = DataEditProductCategoriesFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productsEditFragmentViewModel = ViewModelProviders
            .of(this@DataEditProductCategoriesFragment, productsEditFragmentViewModelFactory)
            .get(DataEditProductCategoriesFragmentViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.product_categorie_edit_fragment, container, false)
        binding.lifecycleOwner = this@DataEditProductCategoriesFragment
        binding.viewModel = productsEditFragmentViewModel

        productsEditFragmentViewModel.fetchProductData()

        productsEditFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                progress_bar_product_categories.visibility = View.VISIBLE
                product_categories_list.visibility = View.GONE
            } else {
                progress_bar_product_categories.visibility = View.GONE
                setAdapter()
                product_categories_list.visibility = View.VISIBLE
            }
        })

        productsEditFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        productsEditFragmentViewModel.deleteFab.observe(this, Observer {
            multipleDeleteClick(it)
        })

        productsEditFragmentViewModel.deleteConfirmFab.observe(this, Observer {
            if (it) {
                multipleDeleteConfirmClick()
            }
        })

        binding.fabNew.setOnClickListener{
            productsEditFragmentViewModel.deleteFab.postValue(false)
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.main_content, NewEditProductCategoriesFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        productsEditFragmentViewModel.deleteIsSuccesfullyDone.observe(this, Observer {
            if (it) {
                productEditAdapter.updateListAndRefresh(productsEditFragmentViewModel.getAllNonDeletedProducts())
                productEditAdapter.deleteProductList.clear()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(productsEditFragmentViewModel.deletedProductList.value != null){
            productEditAdapter.deleteProductList = productsEditFragmentViewModel.deletedProductList.value!!
        }
        setAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        productsEditFragmentViewModel.deletedProductList.value = productEditAdapter.deleteProductList
    }

    private fun setAdapter() {
        productEditAdapter = ProductCategoriesEditAdapter(
            activity!!,
            preferences,
            connector.productCategoriesDao().getAllProductCategories(),
            eventBus,
            this)
        binding.rvProductCategoriess.adapter = productEditAdapter
        binding.rvProductCategoriess.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.rvProductCategoriess.layoutManager = LinearLayoutManager(activity)
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
            productsEditFragmentViewModel.deletedProductList.postValue(null)
        }
    }

    private fun multipleDeleteConfirmClick(){
        productsEditFragmentViewModel.deletedProductList.value = productEditAdapter.deleteProductList
        productsEditFragmentViewModel.delete()
    }

    override fun onSpecificproductClick(productId: String) {
        val newEditProductCategoriesFragment = NewEditProductCategoriesFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(DataEditConstants.PRODUCT_CATEGORIES_ID, productId)
        newEditProductCategoriesFragment.arguments = bundle
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, newEditProductCategoriesFragment)
            .addToBackStack(null)
            .commit()
    }
}