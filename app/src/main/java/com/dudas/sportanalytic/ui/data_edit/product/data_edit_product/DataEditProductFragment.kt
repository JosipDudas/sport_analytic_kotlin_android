package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product

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
import com.dudas.sportanalytic.databinding.ProductEditFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product.EditListProductFragment
import kotlinx.android.synthetic.main.product_edit_fragment.*

class DataEditProductFragment : BaseFragment(), ProductEditAdapter.AdapterCallBack {

    private lateinit var binding: ProductEditFragmentBinding
    private lateinit var productEditAdapter: ProductEditAdapter
    private lateinit var productsEditFragmentViewModel: DataEditProductFragmentViewModel

    companion object {
        fun newInstance() = DataEditProductFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productsEditFragmentViewModel = ViewModelProviders
            .of(this@DataEditProductFragment, DataEditProductsFragmentViewModelFactory(connector, preferences, sportAnalyticService))
            .get(DataEditProductFragmentViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.product_edit_fragment, container, false)
        binding.lifecycleOwner = this@DataEditProductFragment
        binding.viewModel = productsEditFragmentViewModel

        productsEditFragmentViewModel.fetchProductData()

        productsEditFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                progress_bar_product.visibility = View.VISIBLE
                product_list.visibility = View.GONE
            } else {
                progress_bar_product.visibility = View.GONE
                setAdapter()
                product_list.visibility = View.VISIBLE
            }
        })

        productsEditFragmentViewModel.error.observe(this, Observer {
            if(it.message.toString() != "null") {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
    }

    private fun setAdapter() {
        productEditAdapter = ProductEditAdapter(
            activity!!,
            preferences,
            connector.productCategoriesDao().getAllProductCategories(),
            eventBus,
            this)
        binding.rvProduct.adapter = productEditAdapter
        binding.rvProduct.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.rvProduct.layoutManager = LinearLayoutManager(activity)
    }

    override fun onSpecificproductClick(productId: String) {
        val newEditProductFragment = EditListProductFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(DataEditConstants.PRODUCT_CATEGORIES_ID, productId)
        newEditProductFragment.arguments = bundle
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.main_content, newEditProductFragment)
            .addToBackStack(null)
            .commit()
    }
}