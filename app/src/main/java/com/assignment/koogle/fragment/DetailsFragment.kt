package com.assignment.koogle.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.assignment.koogle.R
import com.assignment.koogle.repository.AppRepository
import com.assignment.koogle.util.Resource
import com.assignment.koogle.viewModel.DetailsPageViewModel
import com.assignment.koogle.viewModel.ViewModelProviderFactory

class DetailsFragment : Fragment() {

    private lateinit var viewModelDetails: DetailsPageViewModel
    lateinit var progressBar: ProgressBar
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    var helpId: Int = 0
    var title: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_details, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        webView = view.findViewById<WebView>(R.id.webView)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        helpId = requireArguments().getInt("id")
        title = requireArguments().getString("title")!!
        activity?.setTitle(title)
        setupViewModel()
        return view
    }

    private fun setupViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application, AppRepository())
        viewModelDetails = ViewModelProvider(this, factory).get(DetailsPageViewModel::class.java)
        viewModelDetails.getDetailsData(helpId)
        progressBar.visibility = View.VISIBLE
        getDetailsData()
    }

    private fun getDetailsData() {
        viewModelDetails.detailsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    response.data?.let { detailsData ->
                            webView.webViewClient = WebViewClient()
                            webView.loadData(detailsData.helpDescriptionDetails.get(0).helpDescription.toString(),"text/html", "UTF-8")
                            webView.settings.javaScriptEnabled = true
                            webView.settings.setSupportZoom(true)
                    }
                }
                is Resource.Error -> {response.message?.let { _->}}
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}