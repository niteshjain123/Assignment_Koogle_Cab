package com.assignment.tv9.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.assignment.tv9.MainActivity
import com.assignment.tv9.R
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager
    lateinit var progressBar: ProgressBar

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
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        progressBar = view.findViewById<ProgressBar>(R.id.progressbarLogin)
        view.findViewById<SignInButton>(R.id.google_button).setOnClickListener {
            showProgressBar()
            signInGoogle()
        }
        arrayOf<String?>("email")
        view?.findViewById<LoginButton>(R.id.login_button)?.let {}
        view?.findViewById<LoginButton>(R.id.login_button)?.setOnClickListener {
            showProgressBar()
            signInFacebook()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful)
        {
            val account :  GoogleSignInAccount? = task.result
            if(account != null){
                updateUi(account)
            }
        }
        else {
            Toast.makeText(activity,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInFacebook(){
            requireView().findViewById<LoginButton>(R.id.login_button).registerCallback(
                    callbackManager,
                    object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    hideProgressBar()
                }

                override fun onError(error: FacebookException) {
                    hideProgressBar()
                }
            },
        )
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                hideProgressBar()
                Navigation.findNavController(requireView()).navigate(R.id.login_to_dashboard)
            }
            .addOnFailureListener{
                hideProgressBar()
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT,).show()
            }
    }

    private fun updateUi(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful)
            {
                hideProgressBar()
                Navigation.findNavController(requireView()).navigate(R.id.login_to_dashboard)
            }
            else{
                hideProgressBar()
                Toast.makeText(activity,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }


}