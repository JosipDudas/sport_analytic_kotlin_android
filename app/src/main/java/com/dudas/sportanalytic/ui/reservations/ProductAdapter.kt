package com.dudas.sportanalytic.ui.reservations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import kotlinx.android.synthetic.main.product_activity_popup.view.*
import kotlinx.android.synthetic.main.product_edit_list.view.*

class ProductAdapter(var product : List<Product>?,
                     val context: Context,
                     val callBack: CallBack
) : androidx.recyclerview.widget.RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var productItemList = mutableListOf<Product>()

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun getItemCount(): Int {
        return product?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (product!= null) {
            holder.productDetails.text = context.getString(R.string.product_list,
                product!![position].name)
        }
        setOnItemClickListener(holder.cbProduct, product!![position])
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val cbProduct = itemView.cb_product
        val productDetails = itemView.txt_product_name!!
    }

    private fun setOnItemClickListener(cbProduct: CheckBox, product: Product) {
        cbProduct.setOnClickListener {
            if (cbProduct.isChecked) {
                productItemList.add(product)
            } else{
                productItemList.remove(product)
            }
        }
    }

    fun confirm() {
        callBack.addToReservation(productItemList)
    }

    interface CallBack {
        fun addToReservation(productItemList: List<Product>)
    }
}