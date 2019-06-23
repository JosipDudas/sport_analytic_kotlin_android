package com.dudas.sportanalytic.ui.reservations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.ProductCategories
import kotlinx.android.synthetic.main.product_categories_list_items.view.*

class ProductCategoriesAdapter(private val items : List<ProductCategories>,
                     val context: Context,
                     val callBack: CallBack)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ProductCategoriesAdapter.ViewHolder>() {
    override fun getItemCount() = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(context).inflate(R.layout.product_categories_list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productButtonLayout.button_product.text = items[position].name
        setOnItemClickListener(holder.productButtonLayout.button_product, null,items[position])
    }

    private fun setOnItemClickListener(button: Button?, imageButton: ImageButton?, product: ProductCategories) {
        if (button != null) button.setOnClickListener {
            callBack.createInvoice(product)
        } else imageButton?.setOnClickListener {
            callBack.createInvoice(product)
        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val productButtonLayout = itemView.ll_product_button!!
    }

    interface CallBack{
        fun createInvoice(product: ProductCategories)
    }

}