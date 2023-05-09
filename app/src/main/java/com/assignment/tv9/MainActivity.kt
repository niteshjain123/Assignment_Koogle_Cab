package com.assignment.tv9

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment.tv9.util.Constants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private var analytics: FirebaseAnalytics?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        analytics = Firebase.analytics
        Constants.counterForInterstitialAd=0
    }
    public fun sendDataToAnalytics(fragmentName:String){
        val bundle = Bundle()
        if (analytics==null) analytics=Firebase.analytics
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, fragmentName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, fragmentName)
        analytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }


}