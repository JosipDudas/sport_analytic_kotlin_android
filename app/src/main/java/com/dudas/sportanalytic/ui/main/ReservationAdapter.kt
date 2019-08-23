package com.dudas.sportanalytic.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.database.entities.Reservation
import com.dudas.sportanalytic.database.entities.ReservationItem
import com.dudas.sportanalytic.preferences.MyPreferences
import com.dudas.sportanalytic.utils.getDateFormatForReservationDate
import kotlinx.android.synthetic.main.reservations_list.view.*
import java.sql.Date
import java.util.*

class ReservationAdapter (val context: Context,
                                    val preferences: MyPreferences,
                                    var reservation: List<Reservation>,
                                    val reservationItems: List<ReservationItem>,
                                    val products: List<Product>,
                                    val callBack: CallBack) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.reservations_list, p0, false))

    override fun getItemCount(): Int = reservation.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var productList = ""
        p0.reservationFrom.text = getDateFormatForReservationDate(reservation[p1].from!!)
        p0.reservationTo.text = getDateFormatForReservationDate(reservation[p1].to!!)
        p0.reservationDescription.text = reservation[p1].description
        for (i in 0 until reservationItems.size) {
            if(reservationItems[i].reservation_id == reservation[p1].id) {
                products.forEach{
                    if (reservationItems[i].product_id == it.id) {
                        productList += it.name + "\n"
                    }
                }
            }
        }
        p0.reservationProductsList.text = productList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationFrom = itemView.txt_from_data
        val reservationTo = itemView.txt_to_data
        val reservationDescription= itemView.txt_description_data
        val reservationProductsList = itemView.txt_products_list
    }

    interface CallBack {

    }
}