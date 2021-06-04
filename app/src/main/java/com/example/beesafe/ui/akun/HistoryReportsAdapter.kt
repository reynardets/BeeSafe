package com.example.beesafe.ui.akun

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.beesafe.R
import com.example.beesafe.model.remote.ReportsResponse
import java.util.*
import kotlin.collections.ArrayList

class HistoryReportsAdapter(context : Context) : RecyclerView.Adapter<HistoryReportsAdapter.HistoryReportsViewHolder>() {

    private val listReports = ArrayList<ReportsResponse>()
    private val geocoder = Geocoder(context, Locale.getDefault())

    inner class HistoryReportsViewHolder(item : View) : RecyclerView.ViewHolder(item){
        var historyConstraint : ConstraintLayout = item.findViewById(R.id.HistoryConstraint)
        var expendableRl : RelativeLayout = item.findViewById(R.id.expandable_rl)
        var tvTanggal : TextView = item.findViewById(R.id.tanggal_tv)
        var tvKategori : TextView = item.findViewById(R.id.kategori_tv)
        var tvLokasi : TextView = item.findViewById(R.id.lokasi_tv)
        var tvDeskripsi : TextView = item.findViewById(R.id.deskripsi_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryReportsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.history_item,parent,false)
        return HistoryReportsViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return listReports.size
    }

    override fun onBindViewHolder(holder: HistoryReportsViewHolder, position: Int) {
        val reports = listReports[position]
        var toggle = false
        holder.expendableRl.visibility = View.GONE
        holder.historyConstraint.setOnClickListener{
            if(toggle) {
                holder.expendableRl.visibility = View.VISIBLE
                toggle = !toggle
            }else {
                holder.expendableRl.visibility = View.GONE
                toggle = !toggle
            }
        }
        val tanggal = reports.datetime.split("T")
        holder.tvTanggal.text = tanggal[0]

        holder.tvKategori.text = reports.category
        holder.tvDeskripsi.text = reports.description

        val addresses: List<Address>
        addresses = geocoder.getFromLocation(reports.location.latitude, reports.location.longitude, 1)
        val address = addresses[0].getAddressLine(0)
        holder.tvLokasi.text = address.toString()
    }

    fun setData(items: ArrayList<ReportsResponse>) {
        listReports.clear()
        listReports.addAll(items)
        notifyDataSetChanged()
    }
}