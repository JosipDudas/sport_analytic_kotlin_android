package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.ProductCategories
import com.dudas.sportanalytic.preferences.MyPreferences
import kotlinx.android.synthetic.main.product_categories_edit_list.view.*
import org.greenrobot.eventbus.EventBus

class ProductEditAdapter (val context: Context,
                          val preferences: MyPreferences,
                          var products: List<ProductCategories>,
                          val eventBus: EventBus,
                          private val adapterCallBack: AdapterCallBack) : RecyclerView.Adapter<ProductEditAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.product_categories_edit_list, p0, false))

    override fun getItemCount(): Int = products.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.product.txt_product_categories_name.text = products[p1].name
        p0.product.txt_product_categories_description.text = products[p1].description

        productListener(p0, p1)
    }

    private fun productListener(p0: ViewHolder, p1: Int) {
        p0.product.setOnClickListener {
            adapterCallBack.onSpecificproductClick(products[p1].id)
        }
    }

    interface AdapterCallBack {
        fun onSpecificproductClick(productId: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product = itemView.rl_product_categories!!
    }
}