package com.dudas.sportanalytic.ui.main

import android.annotation.SuppressLint
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
import com.dudas.sportanalytic.databinding.MainFragmentBinding
import com.dudas.sportanalytic.ui.BaseFragment
import com.dudas.sportanalytic.utils.getDateFormatForReservationDate
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.ll_progress_bar
import kotlinx.android.synthetic.main.reservation_fragment.*
import java.sql.Date
import java.util.*

class MainFragment: BaseFragment(), ReservationAdapter.CallBack{
    lateinit var mainFragmentViewModel: MainFragmentViewModel
    lateinit var binding: MainFragmentBinding
    lateinit var reservationAdapter: ReservationAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainFragmentViewModel = ViewModelProviders
            .of(this@MainFragment, MainFragmentViewModelFactory(preferences, connector, sportAnalyticService))
            .get(MainFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this@MainFragment
        binding.viewModel = mainFragmentViewModel

        mainFragmentViewModel.progress.observe(this, Observer {
            if (it) {
                ll_progress_bar.visibility = View.VISIBLE
                layout_registering.visibility =View.GONE
            } else {
                ll_progress_bar.visibility = View.GONE
                layout_registering.visibility =View.VISIBLE
                txt_main_fragment.visibility = View.GONE
                recycler_view_reservations.visibility = View.VISIBLE
            }
        })

        mainFragmentViewModel.error.observe(this, Observer {
            Toast.makeText(context!!, it.message, Toast.LENGTH_LONG).show()
        })

        mainFragmentViewModel.createAdapter.observe(this, Observer {
            if (it) {
                setAdapter()
                mainFragmentViewModel.setAdapterToFalse()
            }
        })

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (preferences.getLocation() != null) {
            txt_main_fragment.visibility = View.GONE
            recycler_view_reservations.visibility = View.VISIBLE
            // Show all reservations
            mainFragmentViewModel.getReservations()
        } else {
            mainFragmentViewModel.setAdapterToFalse()
            ll_progress_bar.visibility = View.GONE
            layout_registering.visibility = View.VISIBLE
            txt_main_fragment.visibility = View.VISIBLE
            recycler_view_reservations.visibility = View.GONE
            txt_main_fragment.text = getString(R.string.location_is_unknown)
        }
    }

    private fun setAdapter() {
        val reservationList = mainFragmentViewModel.getReservationList()
        reservationAdapter = ReservationAdapter(
            activity!!,
            preferences,
            reservationList,
            connector.reservationItemDao().getAllReservationItems(),
            connector.productDao().getAllProducts(),
            this)
        binding.recyclerViewReservations.adapter = reservationAdapter
        binding.recyclerViewReservations.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.recyclerViewReservations.layoutManager = LinearLayoutManager(activity)
    }
}