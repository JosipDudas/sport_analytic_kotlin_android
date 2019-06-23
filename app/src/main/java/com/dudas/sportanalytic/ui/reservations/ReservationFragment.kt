package com.dudas.sportanalytic.ui.reservations

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.product_activity_popup.*
import kotlinx.android.synthetic.main.reservation_fragment.*

class ReservationFragment : BaseFragment(), ProductCategoriesAdapter.CallBack{

    private lateinit var binding: com.dudas.sportanalytic.databinding.ReservationFragmentBinding
    private lateinit var reservationFragmentViewModel: ReservationFragmentViewModel

    companion object {
        fun newInstance() = ReservationFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reservationFragmentViewModel = ViewModelProviders
            .of(this@ReservationFragment, ReservationFragmentViewModelFactory(sportAnalyticService,
                connector,
                preferences))
            .get(ReservationFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.reservation_fragment, container, false)
        binding.lifecycleOwner = this@ReservationFragment
        binding.viewModel = reservationFragmentViewModel

        reservationFragmentViewModel.productsList.observe(this, Observer {
            if (it != null) {
                recycler_view_products.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                recycler_view_products.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 3)
                recycler_view_products.adapter =
                    ProductCategoriesAdapter(reservationFragmentViewModel.productsList.value!!, context!!, this)
                ll_progress_bar.visibility = View.GONE
                relative_layout_products.visibility = View.VISIBLE
            } else {
                isNotLoggedIn()
            }
        })

        reservationFragmentViewModel.buttonView.observe(this, Observer {
            if(it == true){
                viewVisible()
            } else {
                viewGone()
            }
        })

        reservationFragmentViewModel.lastSelection.observe(this, Observer { setButtonText(it) })

        reservationFragmentViewModel.popupWindowIsOpen.observe(this, Observer {
            if (it) {
                payment()
            }
        })

        reservationFragmentViewModel.onCreate()

        return binding.root
    }

    override fun onResume() {
        reservationFragmentViewModel.onResume()
        super.onResume()
    }

    private fun payment() {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.product_activity_popup)
        dialog.recycler_view_pay_products.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        dialog.recycler_view_pay_products.adapter = ProductAdapter(
            connector.productDao().getAllProducts(),
            activity!!)

        dialog.btn_close.setOnClickListener {
            dialog.dismiss()
            reservationFragmentViewModel.popupWindowIsOpen.postValue(false)
        }

        dialog.btn_confirm.setOnClickListener {
            viewGone()
            dialog.cancel()
        }

        dialog.show()
    }

    private fun isNotLoggedIn() {
        layout_no_product_created.visibility = View.VISIBLE
        relative_layout_products.visibility = View.GONE
    }

    private fun viewVisible() {
        cl_buttons_group.visibility = View.VISIBLE
    }

    private fun viewGone() {
        cl_buttons_group.visibility = View.GONE
    }

    private fun setButtonText(lastSelection: LastSelection) {
        /*btn_last_selection.text = getString(R.string.last_selection, lastSelection.quantityProduct, lastSelection.productName)
        btn_payment.text = getString(R.string.total, lastSelection.total)*/
    }

    override fun createInvoice(product: ProductCategories) {
        /*viewVisible()
        reservationFragmentViewModel.createInvoice(product)*/
    }
}