package com.assignment.tv9.adapter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.assignment.tv9.R
import com.assignment.tv9.model.Dashboard.DashboardList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DateFormat
import java.time.format.DateTimeFormatter

class DashboardListRVAdapter(private val context: Context) :
    RecyclerView.Adapter<DashboardListRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<DashboardList>() {
        override fun areItemsTheSame(oldItem: DashboardList, newItem: DashboardList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DashboardList, newItem: DashboardList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CarouselItemViewHolder (
        /*if(viewType==itemAD){
            LayoutInflater.from(parent.context).inflate(R.layout.item_google_ads, parent, false)
        }else*/
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_list, parent, false)

     )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val picItem = differ.currentList[position]

        holder.itemView.findViewById<TextView>(R.id.tvTitle).setText(picItem.title!!.rendered)
        holder.itemView.findViewById<TextView>(R.id.tvShortDec).setText(picItem.shortHeadline)
        holder.itemView.findViewById<TextView>(R.id.tvLongDec).setText(picItem.excerpt!!.rendered.toString())


        Glide.with(context)
            .load(picItem.mobileFeaturedImage)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.tv9_logo))
            .into( holder.itemView.findViewById<AppCompatImageView>(R.id.imageDashboard))

        holder.itemView.findViewById<TextView>(R.id.tvDate).setText(picItem.date?.substring(0,10))
        holder.itemView.findViewById<TextView>(R.id.tvAuthor).setText("Author: "+ picItem.embedded!!.author.get(0).name )


        holder.itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.dashboardItem)
            .setOnClickListener(View.OnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", differ.currentList.get(position).id!!)
                bundle.putString("name", differ.currentList.get(position).title.toString())
                Navigation.findNavController(holder.itemView).navigate(R.id.dashboard_to_details,bundle)
            })

    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}