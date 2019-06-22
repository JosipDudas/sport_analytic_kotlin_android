package com.dudas.sportanalytic.ui.data_edit.product.data_edit_product.edit_product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Product
import com.dudas.sportanalytic.preferences.MyPreferences
import kotlinx.android.synthetic.main.product_edit_list.view.*
import org.greenrobot.eventbus.EventBus

class ProductEditListAdapter (val context: Context,
                              val preferences: MyPreferences,
                              var products: List<Product>,
                              val eventBus: EventBus,
                              private val adapterCallBack: AdapterCallBack) : RecyclerView.Adapter<ProductEditListAdapter.ViewHolder>(){

    var deleteProductList: MutableList<Product> = mutableListOf()
    var checkBox = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(context).inflate(
            R.layout.product_edit_list, p0, false))

    override fun getItemCount(): Int = products.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.product.txt_product_name.text = products[p1].name

        if (checkBox) {
            deleteFabIsPressed(p0, p1)
        } else {
            deleteCancelFabIsPressed(p0)
        }

        productListener(p0, p1)

        checkBoxListener(p0, p1)
    }

    private fun deleteFabIsPressed(p0: ViewHolder, p1: Int) {
        p0.product.isEnabled = false
        p0.product.cb_product.isEnabled = true
        p0.product.cb_product.visibility = View.VISIBLE
        for(i in 0 until deleteProductList.size) {
            if (deleteProductList[i].id == products[p1].id) {
                p0.product.cb_product.isChecked = true
            }
        }
    }

    private fun deleteCancelFabIsPressed(p0: ViewHolder) {
        p0.product.isEnabled = true
        p0.product.cb_product.isEnabled = false
        p0.product.cb_product.visibility = View.GONE
        p0.product.cb_product.isChecked = false
        deleteProductList.clear()
    }

    private fun productListener(p0: ViewHolder, p1: Int) {
        p0.product.setOnClickListener {
            adapterCallBack.onSpecificproductClick(products[p1].id)
            deleteProductList.clear()
        }
    }

    private fun checkBoxListener(p0: ViewHolder, p1: Int){
        p0.product.cb_product.setOnClickListener{
            if (p0.product.cb_product.isChecked) {
                deleteProductList.add(products[p1])
            } else {
                var removeproductFromMutableList: Product? = null
                deleteProductList.forEach {
                    if (it.id == products[p1].id) {
                        removeproductFromMutableList = it
                    }
                }
                if (removeproductFromMutableList != null) {
                    deleteProductList.remove(removeproductFromMutableList!!)
                } else {

                }
            }
        }
    }

    fun updateListAndRefresh(updatedproductsList: List<Product>) {
        products = updatedproductsList
        this.notifyDataSetChanged()
    }

    interface AdapterCallBack {
        fun onSpecificproductClick(productId: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product = itemView.rl_product!!
    }
}