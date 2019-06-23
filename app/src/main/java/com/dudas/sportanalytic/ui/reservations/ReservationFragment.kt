package com.dudas.sportanalytic.ui.reservations

import android.annotation.SuppressLint
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
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.ui.BaseFragment
import kotlinx.android.synthetic.main.product_activity_popup.*
import kotlinx.android.synthetic.main.reservation_fragment.*
import java.util.*


class ReservationFragment : BaseFragment(), ProductCategoriesAdapter.CallBack, ProductAdapter.CallBack{

    private lateinit var binding: com.dudas.sportanalytic.databinding.ReservationFragmentBinding
    private lateinit var reservationFragmentViewModel: ReservationFragmentViewModel
    private lateinit var dialog: Dialog
    private lateinit var productAdapter: ProductAdapter
    private lateinit var picker: DatePickerDialog
    private lateinit var pickerDay: DatePickerDialog

    companion object {
        fun newInstance() = ReservationFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reservationFragmentViewModel = ViewModelProviders
            .of(this@ReservationFragment, ReservationFragmentViewModelFactory(sportAnalyticService,
                connector,
                preferences))
            .get(ReservationFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.reservation_fragment, container, false)
        binding.lifecycleOwner = this@ReservationFragment
        binding.viewModel = reservationFragmentViewModel

        reservationFragmentViewModel.onCreate()

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

        reservationFragmentViewModel.popUpProgress.observe(this, Observer {
            if(!it) {
                productAdapter.product = reservationFragmentViewModel.products.value
                productAdapter.notifyDataSetChanged()
                dialog.products_progress_bar.visibility = View.GONE
                dialog.rl_products.visibility = View.VISIBLE
            }
        })

        binding.btnFrom.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    btn_from.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, (monthOfYear + 1))
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    reservationFragmentViewModel.from.value = calendar.time
                },
                year,
                month,
                day
            )
            picker.show()
        }

        binding.btnTo.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            pickerDay = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    binding.btnTo.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, (monthOfYear + 1))
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    reservationFragmentViewModel.from.value = calendar.time
                },
                year,
                month,
                day
            )
            pickerDay.show()
        }

        return binding.root
    }

    override fun onResume() {
        reservationFragmentViewModel.onResume()
        super.onResume()
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

    override fun chooseProducts(productCategory: ProductCategories) {
        productAdapter = ProductAdapter(null, context!!, this)
        productAdapter.productItemList.clear()
        reservationFragmentViewModel.getProductForCategory(productCategory.id)
        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.product_activity_popup)
        dialog.recycler_view_pay_products.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        productAdapter = ProductAdapter(
            null,
            activity!!,
            this)

        dialog.recycler_view_pay_products.adapter = productAdapter

        dialog.btn_close.setOnClickListener {
            dialog.dismiss()
            reservationFragmentViewModel.popupWindowIsOpen.postValue(false)
        }

        dialog.btn_confirm_product.setOnClickListener {
            productAdapter.confirm()
            viewGone()
            dialog.cancel()
        }

        dialog.products_progress_bar.visibility = View.VISIBLE
        dialog.rl_products.visibility = View.GONE

        dialog.show()
    }

    override fun addToReservation(productItemList: List<Product>) {
        // TODO
    }
}