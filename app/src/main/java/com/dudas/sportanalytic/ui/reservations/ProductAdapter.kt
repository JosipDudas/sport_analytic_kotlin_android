package com.dudas.sportanalytic.ui.reservations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import kotlinx.android.synthetic.main.product_edit_list.view.*

class ProductAdapter(private val product : List<Product>,
                     val context: Context
) : androidx.recyclerview.widget.RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productDetails.text = context.getString(R.string.product_list,
            product[position].name)
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val productDetails = itemView.txt_product_name!!
    }

}