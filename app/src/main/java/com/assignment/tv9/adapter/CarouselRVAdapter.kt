package com.assignment.tv9.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.assignment.tv9.R
import com.assignment.tv9.model.Carousel.CarouselSlider
import com.bumptech.glide.Glide
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlin.random.Random

class CarouselRVAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var nativeAd:NativeAd?=null

    public fun setNativeAd(nativeAd: NativeAd){
        this.nativeAd=nativeAd;
        updateViewPageList()
    }
    fun updateViewPageList(){
        val listWAds = ArrayList<CarouselSlider>()
        val currentList = this.differ.currentList
        var i=0;
        if(currentList.size>2 && currentList[2].mobileFeaturedImage!=null){
            for (carousalSlider in currentList){
                if(i>0 && i%2==0){
                    listWAds.add(CarouselSlider(Random(20405).nextInt()))
                }
                listWAds.add(carousalSlider)
                i++
            }
            this.differ.submitList(listWAds)
        }
        else{
            this.differ.submitList(currentList)
        }
    }

    fun updateViewPageList(sliderList: ArrayList<CarouselSlider>) {
        this.differ.submitList(sliderList)
    }


    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val VIEW_TYPE_ADS: Int = 1;
    private val VIEW_TYPE_IMAGE:Int = 2;
    private val differCallback = object : DiffUtil.ItemCallback<CarouselSlider>() {
        override fun areItemsTheSame(oldItem: CarouselSlider, newItem: CarouselSlider): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarouselSlider, newItem: CarouselSlider): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        if(viewType==VIEW_TYPE_ADS){
            return AdsItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_carousel_ad, parent, false))
        }
        else{
            return CarouselItemViewHolder (LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false))
        }
    }

    inner class AdsItemViewHolder(inflate: View) : RecyclerView.ViewHolder(inflate) {
        fun onBind(nativeAd:NativeAd?){
            showNativeAd(nativeAd,itemView)
        }

        private fun showNativeAd(nativeAd: NativeAd?, itemView: View) {
            if (nativeAd==null){
                return;
            }
            itemView.apply {
                nativeAd.apply {
                    //Init Native Ads Vies
                    val adView: NativeAdView = findViewById(R.id.adView)
                    val adMedia: MediaView = findViewById(R.id.adMedia)
                    val adHeadline: TextView = findViewById(R.id.adHeadline)
                    val adBody: TextView = findViewById(R.id.adBody)
                    val adBtnAction: Button = findViewById(R.id.adBtnAction)
                    val adAppIcon: ImageView = findViewById(R.id.adAppIcon)
                    val adPrice: TextView = findViewById(R.id.adPrice)
                    val adStars: RatingBar = findViewById(R.id.adStars)
                    val adStore: TextView = findViewById(R.id.adStore)
                    val adAdvertiser: TextView = findViewById(R.id.adAdvertiser)
                    //Assign position of views inside the native ad view
                    adView.mediaView = adMedia
                    adView.headlineView = adHeadline
                    adView.bodyView = adBody
                    adView.callToActionView = adBtnAction
                    adView.iconView = adAppIcon
                    adView.priceView = adPrice
                    adView.starRatingView = adStars
                    adView.storeView = adStore
                    adView.advertiserView = adAdvertiser
                    //Assign Values to View
                    adView.mediaView?.setMediaContent(mediaContent!!)
                    adView.mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    (adView.headlineView as TextView).text = headline
                    (adView.bodyView as TextView).text = body
                    (adView.callToActionView as Button).text = callToAction
                    (adView.iconView as ImageView).setImageDrawable(icon?.drawable)
                    (adView.priceView as TextView).text = price
                    (adView.storeView as TextView).text = store
                    (adView.starRatingView as RatingBar).rating = starRating!!.toFloat()
                    (adView.advertiserView as TextView).text = advertiser
                    adView.setNativeAd(this)
                }
            }
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val picItem = differ.currentList[position]
        if(picItem.mobileFeaturedImage==null){
            (holder as AdsItemViewHolder).onBind(nativeAd)
        }
        else{
            val carouselImage = holder.itemView.findViewById<AppCompatImageView>(R.id.carousel_image)
            Glide.with(context)
                .load(picItem.mobileFeaturedImage)
                .into(carouselImage)

            holder.itemView.findViewById<AppCompatImageView>(R.id.carousel_image)
                .setOnClickListener(View.OnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id", differ.currentList.get(position).id!!)
                    bundle.putString("name", differ.currentList.get(position).title.toString())
                    Navigation.findNavController(holder.itemView).navigate(R.id.dashboard_to_details,bundle)
                })

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(differ.currentList[position].mobileFeaturedImage==null){
            VIEW_TYPE_ADS
        }
        else VIEW_TYPE_IMAGE
    }

}