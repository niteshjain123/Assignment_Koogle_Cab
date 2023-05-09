package com.assignment.tv9.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.assignment.tv9.repository.AppRepository
import com.assignment.tv9.util.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd

class AdViewModel (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {
    var nativeAd: MutableLiveData<NativeAd> = MutableLiveData()
    public fun loadNativeAd(){
        MobileAds.initialize(getApplication()) {}
        val adLoader = AdLoader.Builder(getApplication(), "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad : NativeAd ->
                this.nativeAd.value= ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    var mInterstitialAd: InterstitialAd? = null
    fun loadInterstitialAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(getApplication(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun updateAdShown() {
        Constants.counterForInterstitialAd=0;
        loadInterstitialAd()
    }


}