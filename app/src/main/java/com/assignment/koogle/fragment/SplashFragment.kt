package com.assignment.koogle.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.assignment.koogle.R
import com.bumptech.glide.Glide


class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        Glide.with(requireActivity())
            .load(R.drawable.koogle_logo)
            .into(view.findViewById(R.id.logoImage))
        return view
    }

    override fun onResume() {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                Navigation.findNavController(requireView()).navigate(R.id.splash_to_dashboard)
            }
        },1000)
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}