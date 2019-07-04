package com.dudas.sportanalytic.ui.reports

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.ui.reservations.ProductAdapter
import com.dudas.sportanalytic.ui.reservations.ProductCategoriesAdapter
import kotlinx.android.synthetic.main.product_activity_popup.*
import kotlinx.android.synthetic.main.report_fragment.*

class ReportFragment : BaseFragment(), ProductCategoriesAdapter.CallBack, ProductAdapter.CallBack{

    private lateinit var binding: com.dudas.sportanalytic.databinding.ReportFragmentBinding
    private lateinit var reportFragmentViewModel: ReportFragmentViewModel
    private lateinit var dialog: Dialog
    private lateinit var productAdapter: ProductAdapter

    companion object {
        fun newInstance() = ReportFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reportFragmentViewModel = ViewModelProviders
            .of(this@ReportFragment, ReportFragmentViewModelFactory(sportAnalyticService,
                connector,
                preferences)
            )
            .get(ReportFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.report_fragment, container, false)
        binding.lifecycleOwner = this@ReportFragment
        binding.viewModel = reportFragmentViewModel

        reportFragmentViewModel.popUpProgress.value = true

        reportFragmentViewModel.onCreate()

        reportFragmentViewModel.productsList.observe(this, Observer {
            if (it != null) {
                recycler_view_product_groups.layoutManager = LinearLayoutManager(activity)
                recycler_view_product_groups.layoutManager = GridLayoutManager(activity, 3)
                recycler_view_product_groups.adapter =
                    ProductCategoriesAdapter(reportFragmentViewModel.productsList.value!!, context!!, this)
            } else {
                isNotLoggedIn()
            }
        })

        reportFragmentViewModel.popUpProgress.observe(this, Observer {
            if(!it) {
                productAdapter.product = reportFragmentViewModel.products.value
                productAdapter.notifyDataSetChanged()
                dialog.products_progress_bar.visibility = View.GONE
                dialog.rl_products.visibility = View.VISIBLE
            }
        })

        reportFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                ll_progress_bar_report.visibility = View.VISIBLE
            } else {
                ll_progress_bar_report.visibility = View.GONE
            }
        })

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        reportFragmentViewModel.popUpProgress.value = true
    }
    
    private fun isNotLoggedIn() {
        layout_no_product_groups_created.visibility = View.VISIBLE
    }

    override fun chooseProducts(productCategory: ProductCategories) {
        productAdapter = ProductAdapter(null, context!!, this)
        productAdapter.productItemList.clear()
        reportFragmentViewModel.getProductForCategory(productCategory.id)
        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.product_activity_popup)
        dialog.recycler_view_pay_products.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        productAdapter = ProductAdapter(
            null,
            activity!!,
            this)

        if (reportFragmentViewModel.selectedProducts.value != null) {
            productAdapter.productItemList  = reportFragmentViewModel.selectedProducts.value!!.toMutableList()
            productAdapter.notifyDataSetChanged()
        }

        dialog.recycler_view_pay_products.adapter = productAdapter

        dialog.btn_close.setOnClickListener {
            dialog.dismiss()
            reportFragmentViewModel.popupWindowIsOpen.postValue(false)
        }

        dialog.btn_confirm_product.setOnClickListener {
            dialog.cancel()
        }

        dialog.products_progress_bar.visibility = View.VISIBLE
        dialog.rl_products.visibility = View.GONE

        dialog.show()
    }

    override fun removeProduct(product: Product) {
        reportFragmentViewModel.removeProductFromList(product)
    }

    override fun addProduct(product: Product) {
        reportFragmentViewModel.addToProductList(product)
    }
}