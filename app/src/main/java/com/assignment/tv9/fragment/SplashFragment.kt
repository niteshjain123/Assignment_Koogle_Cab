package com.assignment.tv9.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.assignment.tv9.MainActivity
import com.assignment.tv9.R
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(getString(R.string.web_client_id))
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).sendDataToAnalytics(this.javaClass.simpleName);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        Glide.with(requireActivity())
            .load(R.drawable.logo)
            .into(view.findViewById(R.id.tv9_logo_image))
        return view
    }

    override fun onResume() {
        Handler().postDelayed({
            val currentUser: FirebaseUser? = auth.getCurrentUser()
            if(currentUser!=null || AccessToken.getCurrentAccessToken()?.token!=null)
            {
                Navigation.findNavController(requireView()).navigate(R.id.splash_to_dashboard)
            }
            else
            {
                Navigation.findNavController(requireView()).navigate(R.id.splash_to_login)
            }
        }, 1000)

        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
}