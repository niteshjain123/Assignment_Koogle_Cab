package com.assignment.koogle.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.assignment.koogle.R
import com.assignment.koogle.model.Dashboard.HelpList
import java.text.SimpleDateFormat
import java.util.*

class DashboardListRVAdapter(private val context: Context) :
    RecyclerView.Adapter<DashboardListRVAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<HelpList>() {
        override fun areItemsTheSame(oldItem: HelpList, newItem: HelpList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HelpList, newItem: HelpList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_list, parent, false)
     )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val picItem = differ.currentList[position]

        val createdOn = SpannableString("Created On : "+convertFormatOfDate(picItem.createdAt.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMMM dd, yyyy"))
        createdOn.setSpan(ForegroundColorSpan(Color.BLACK), 0,12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        val updatedOn = SpannableString("Updated On : "+convertFormatOfDate(picItem.updatedAt.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMMM dd, yyyy"))
        updatedOn.setSpan(ForegroundColorSpan(Color.BLACK), 0,12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        holder.itemView.findViewById<TextView>(R.id.tvTitle).setText(picItem.title!!)
        holder.itemView.findViewById<TextView>(R.id.tvCreatedOn).setText(createdOn)
        holder.itemView.findViewById<TextView>(R.id.tvUpdateOn).setText(updatedOn)

        holder.itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.dashboardItem)
            .setOnClickListener(View.OnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", differ.currentList.get(position).id!!)
                bundle.putString("title", differ.currentList.get(position).title.toString())
                Navigation.findNavController(holder.itemView).navigate(R.id.dashboard_to_details,bundle)
            })
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun convertFormatOfDate(dateString: String, currentFormat: String, desiredFormat: String): String {
        val currentSdf = SimpleDateFormat(currentFormat, Locale.getDefault())
        val currentDate: Date? = currentSdf.parse(dateString)
        val desiredSdf = SimpleDateFormat(desiredFormat, Locale.getDefault())
        return desiredSdf.format(currentDate!!)
    }

}