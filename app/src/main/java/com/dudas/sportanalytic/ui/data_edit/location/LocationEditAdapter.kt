package com.dudas.sportanalytic.ui.data_edit.location

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dudas.sportanalytic.R
import com.dudas.sportanalytic.database.entities.Location
import com.dudas.sportanalytic.preferences.MyPreferences
import kotlinx.android.synthetic.main.location_edit_list.view.*
import org.greenrobot.eventbus.EventBus

class LocationEditAdapter (val context: Context,
                           val preferences: MyPreferences,
                           var locations: List<Location>,
                           val eventBus: EventBus,
                           private val adapterCallBack: AdapterCallBack) : RecyclerView.Adapter<LocationEditAdapter.ViewHolder>(){

    var deleteLocationList: MutableList<Location> = mutableListOf()
    var checkBox = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(
        R.layout.location_edit_list, p0, false))

    override fun getItemCount(): Int = locations.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.location.txt_location_name.text = locations[p1].name
        p0.location.txt_location_description.text = locations[p1].description

        if (checkBox) {
            deleteFabIsPressed(p0, p1)
        } else {
            deleteCancelFabIsPressed(p0)
        }

        locationListener(p0, p1)

        checkBoxListener(p0, p1)
    }

    private fun deleteFabIsPressed(p0: ViewHolder, p1: Int) {
        p0.location.isEnabled = false
        p0.location.cb_location.isEnabled = true
        p0.location.cb_location.visibility = View.VISIBLE
        for(i in 0 until deleteLocationList.size) {
            if (deleteLocationList[i].id == locations[p1].id) {
                p0.location.cb_location.isChecked = true
            }
        }
    }

    private fun deleteCancelFabIsPressed(p0: ViewHolder) {
        p0.location.isEnabled = true
        p0.location.cb_location.isEnabled = false
        p0.location.cb_location.visibility = View.GONE
        p0.location.cb_location.isChecked = false
        deleteLocationList.clear()
    }

    private fun locationListener(p0: ViewHolder, p1: Int) {
        p0.location.setOnClickListener {
            adapterCallBack.onSpecificlocationClick(locations[p1].id)
            deleteLocationList.clear()
        }
    }

    private fun checkBoxListener(p0: ViewHolder, p1: Int){
        p0.location.cb_location.setOnClickListener{
            if (p0.location.cb_location.isChecked) {
                deleteLocationList.add(locations[p1])
            } else {
                var removelocationFromMutableList: Location? = null
                deleteLocationList.forEach {
                    if (it.id == locations[p1].id) {
                        removelocationFromMutableList = it
                    }
                }
                if (removelocationFromMutableList != null) {
                    deleteLocationList.remove(removelocationFromMutableList!!)
                } else {

                }
            }
        }
    }

    fun updateListAndRefresh(updatedlocationsList: List<Location>) {
        locations = updatedlocationsList
        this.notifyDataSetChanged()
    }

    interface AdapterCallBack {
        fun onSpecificlocationClick(locationId: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location = itemView.rl_location!!
    }
}
