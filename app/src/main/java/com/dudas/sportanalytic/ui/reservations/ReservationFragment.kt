package com.dudas.sportanalytic.ui.reservations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
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
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.utils.getDateFormatForReservationDate
import kotlinx.android.synthetic.main.product_activity_popup.*
import kotlinx.android.synthetic.main.reservation_fragment.*
import java.sql.Date
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

        reservationFragmentViewModel.popUpProgress.value = true

        reservationFragmentViewModel.onCreate()

        reservationFragmentViewModel.productsList.observe(this, Observer {
            if (it != null) {
                recycler_view_products.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                recycler_view_products.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 3)
                recycler_view_products.adapter =
                    ProductCategoriesAdapter(reservationFragmentViewModel.productsList.value!!, context!!, this)
            } else {
                isNotLoggedIn()
            }
        })

        reservationFragmentViewModel.popUpProgress.observe(this, Observer {
            if(!it) {
                productAdapter.product = reservationFragmentViewModel.products.value
                productAdapter.notifyDataSetChanged()
                dialog.products_progress_bar.visibility = View.GONE
                dialog.rl_products.visibility = View.VISIBLE
            }
        })

        reservationFragmentViewModel.errorMessage.observe(this, Observer {
            if(it) {
                Toast.makeText(context!!, getString(R.string.reservation_is_not_full), Toast.LENGTH_LONG).show()
            }
        })

        binding.btnFrom.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            picker = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    btn_from.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    reservationFragmentViewModel.reservation.value!!.from = Date.valueOf(getDateFormatForReservationDate(calendar.time))
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
            pickerDay = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    binding.btnTo.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    reservationFragmentViewModel.reservation.value!!.to = Date.valueOf(getDateFormatForReservationDate(calendar.time))
                },
                year,
                month,
                day
            )
            pickerDay.show()
        }

        reservationFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                ll_progress_bar.visibility = View.VISIBLE
            } else {
                ll_progress_bar.visibility = View.GONE
            }
        })

        reservationFragmentViewModel.reservationIsSuccessSaved.observe(this, Observer {
            if(it) {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
                fragmentManager?.popBackStack()
            }
        })

        return binding.root
    }

    private fun isNotLoggedIn() {
        layout_no_product_created.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (reservationFragmentViewModel.reservation.value!!.from!= null) {
            btn_from.text = getDateFormatForReservationDate(reservationFragmentViewModel.reservation.value!!.from!!)
        }
        if (reservationFragmentViewModel.reservation.value!!.to!= null) {
            btn_to.text = getDateFormatForReservationDate(reservationFragmentViewModel.reservation.value!!.to!!)
        }
        reservationFragmentViewModel.popUpProgress.value = true
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

        if (reservationFragmentViewModel.selectedProducts.value != null) {
            productAdapter.productItemList  = reservationFragmentViewModel.selectedProducts.value!!.toMutableList()
            productAdapter.notifyDataSetChanged()
        }

        dialog.recycler_view_pay_products.adapter = productAdapter

        dialog.btn_close.setOnClickListener {
            dialog.dismiss()
            reservationFragmentViewModel.popupWindowIsOpen.postValue(false)
        }

        dialog.btn_confirm_product.setOnClickListener {
            dialog.cancel()
        }

        dialog.products_progress_bar.visibility = View.VISIBLE
        dialog.rl_products.visibility = View.GONE

        dialog.show()
    }

    override fun removeProduct(product: Product) {
        reservationFragmentViewModel.removeProductFromList(product)
    }

    override fun addProduct(product: Product) {
        reservationFragmentViewModel.addToProductList(product)
    }
}