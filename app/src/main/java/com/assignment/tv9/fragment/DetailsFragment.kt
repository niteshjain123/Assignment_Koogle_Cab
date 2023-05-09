package com.assignment.tv9.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.assignment.tv9.MainActivity
import com.assignment.tv9.R
import com.assignment.tv9.repository.AppRepository
import com.assignment.tv9.util.Constants
import com.assignment.tv9.util.Resource
import com.assignment.tv9.viewModel.AdViewModel
import com.assignment.tv9.viewModel.DetailsPageViewModel
import com.assignment.tv9.viewModel.ViewModelProviderFactory
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Tag
import org.jsoup.select.Elements
import org.jsoup.select.Evaluator
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random


class DetailsFragment : Fragment() {

    private lateinit var viewModelDetails: DetailsPageViewModel
    private lateinit var adViewModel: AdViewModel
    lateinit var detailsTitle: TextView
    lateinit var linearLayout: LinearLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).sendDataToAnalytics(this.javaClass.simpleName);
    }

    var articalId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_details, container, false)
        Constants.counterForInterstitialAd++;
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        activity?.setTitle(R.string.details)

        linearLayout = view.findViewById<LinearLayout>(R.id.linearLayout)
        progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        articalId = requireArguments().getInt("id")
        setupViewModel()
        return view
    }

    private fun setupViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application, AppRepository())
        viewModelDetails = ViewModelProvider(this, factory).get(DetailsPageViewModel::class.java)
        adViewModel = ViewModelProvider(requireActivity(),factory).get(AdViewModel::class.java)
        adViewModel.loadInterstitialAd()
        if (Constants.counterForInterstitialAd>=3){
            adViewModel.mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdClicked() {}
                override fun onAdDismissedFullScreenContent() {adViewModel.updateAdShown()}
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {adViewModel.loadInterstitialAd()}
                override fun onAdImpression() {}
                override fun onAdShowedFullScreenContent() {}
            }

            adViewModel.mInterstitialAd?.show(requireActivity())
        }
        progressBar.visibility = View.VISIBLE
        getDetails()
    }

    private fun getDetails() {
        viewModelDetails.detailsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    response.data?.let { dashboardDataList ->
                        dashboardDataList.content?.rendered?.let {
                            parse(it)
                        }
                    }
                }
                is Resource.Error -> {response.message?.let { _->}}
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun addBlankTag(formattedElems:ArrayList<Element>, index:Int, elementType:String):Int{
        if(formattedElems.size == index+1){
            if(!formattedElems[index].tagName().equals(elementType)){
                val el = Element(Tag.valueOf(elementType), "")
                formattedElems.add(el)
                return index+1
            }
            else return index
        }
        else{
            val el = Element(Tag.valueOf(elementType), "")
            formattedElems.add(el)
            return index
        }
    }

    private fun parse(rendered: String) {
        val doc: Document = Jsoup.parse(rendered)
        val elements: Elements = doc.select("body>*")
        for (x in elements) {
            var xElems = arrayListOf<Node>()
            xElems.addAll(x.childNodes())
            if(!x.`is`(JsoupEvaluator.EVAL_P.evaluator)){
                xElems.clear()
                xElems.add(x)
            }
            val formattedElems = arrayListOf<Element>()
            var index = 0;
            for(j in xElems){
                if(j is TextNode){
                    index= addBlankTag(formattedElems,index,"p")
                    formattedElems[index].append(j.text())
                }
                else if(j is Element){
                    if(
                        j.`is`(JsoupEvaluator.EVAL_P.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_H3.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_H2.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_A.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_BOLD.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_STRONG.evaluator) ||
                        j.`is`(JsoupEvaluator.EVAL_H1.evaluator)
                    ){
                        index= addBlankTag(formattedElems,index,"p")
                        formattedElems[index].append(j.outerHtml())
                    }
                    else{
                        if(j.`is`(JsoupEvaluator.EVAL_BLOCKQUOTE.evaluator)){
                            index= addBlankTag(formattedElems,index,"block")
                            formattedElems[index].append(j.outerHtml())
                            j.nextElementSibling()?.let {
                                formattedElems[index].append(it.outerHtml())
                            }
                        }
                        else if(j.`is`(JsoupEvaluator.EVAL_SCRIPT.evaluator)){
                            if(formattedElems.size>0 && formattedElems[index].tagName().equals("block")){
                                index= addBlankTag(formattedElems,index,"block")
                                formattedElems[index].append(j.outerHtml())
                            }
                            else{
                                index= addBlankTag(formattedElems,index,"scblock")
                                formattedElems[index].append(j.outerHtml())
                            }
                        }
                        else{
                            index= addBlankTag(formattedElems,index,"randblock_${ThreadLocalRandom.current().nextInt(1, 150000000 + 1)}")
                            formattedElems[index].append(j.outerHtml())
                        }
                    }
                }
            }

            for(j in formattedElems){
                if(
                    j.`is`(JsoupEvaluator.EVAL_P.evaluator)
                ){
                    addTextView(parseParagraph(j));
                }
                else{
                    if(j.`is`(JsoupEvaluator.EVAL_BLOCK.evaluator) || j.`is`(JsoupEvaluator.EVAL_SCRIPT_BLOCK.evaluator)){
                        addWebView(j.html().toString())
                    }
                    else{
                        addTextView(j.text())
                    }
                }
            }
        }
    }

    private fun parseParagraph(x: Element):SpannableStringBuilder {
        val elements = x.childNodes()
        if(elements.count()==1 && elements[0] is TextNode){
            return SpannableStringBuilder(x.text())
        }

        val spannableStringBuilder = SpannableStringBuilder();
        for (j in elements){
            if(j is TextNode){
                spannableStringBuilder.append(j.text())
            }
            else if(j is Element){
                val spannable = parseParagraph(j);
                if (j.`is`(JsoupEvaluator.EVAL_STRONG.evaluator) || j.`is`(JsoupEvaluator.EVAL_BOLD.evaluator)){
                    spannable.append(" ")
                    spannable.setSpan(StyleSpan(Typeface.BOLD),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                else if(j.`is`(JsoupEvaluator.EVAL_H1.evaluator)){
                    spannable.append(" ")
                    spannable.setSpan(RelativeSizeSpan(2.5f),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    spannable.setSpan(StyleSpan(Typeface.BOLD),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                else if(j.`is`(JsoupEvaluator.EVAL_H2.evaluator)){
                    spannable.append(" ")
                    spannable.setSpan(RelativeSizeSpan(1.8f),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    spannable.setSpan(StyleSpan(Typeface.BOLD),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                else if(j.`is`(JsoupEvaluator.EVAL_H3.evaluator)){
                    spannable.append(" ")
                    spannable.setSpan(RelativeSizeSpan(1.3f),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    spannable.setSpan(StyleSpan(Typeface.BOLD),0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                else if(j.`is`(JsoupEvaluator.EVAL_A.evaluator)){
                    spannable.append(" ")
                    val link = j.attr("href")
                    val clickableSpannable = object : ClickableSpan() {
                        override fun onClick(view: View) {
                            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                                "http://$link"
                            }
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            startActivity(browserIntent)
                        }
                    }
                    spannable.setSpan(ForegroundColorSpan(Color.BLUE),0,spannable.length-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    spannable.setSpan(clickableSpannable,0,spannable.length-1,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }

                spannableStringBuilder.append(spannable)
            }
        }
        return spannableStringBuilder;
    }

    enum class JsoupEvaluator(val evaluator: Evaluator){
        EVAL_P(Evaluator.Tag("p")),
        EVAL_BLOCKQUOTE(Evaluator.Tag("blockquote")),
        EVAL_BLOCK(Evaluator.Tag("block")),
        EVAL_SCRIPT_BLOCK(Evaluator.Tag("scblock")),
        EVAL_H3(Evaluator.Tag("h3")),
        EVAL_H2(Evaluator.Tag("h2")),
        EVAL_H1(Evaluator.Tag("h1")),
        EVAL_A(Evaluator.Tag("a")),
        EVAL_STRONG(Evaluator.Tag("strong")),
        EVAL_SCRIPT(Evaluator.Tag("script")),
        EVAL_BOLD(Evaluator.Tag("b")),
    }


    private fun addTextView(data: SpannableStringBuilder) {
        val textView = TextView(requireActivity())
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.gravity = Gravity.CENTER
        textView.setText(data, TextView.BufferType.SPANNABLE)
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        textView.letterSpacing = 0.02F
        textView.movementMethod = (LinkMovementMethod.getInstance())
        linearLayout.addView(textView)
    }

    private fun addTextView(data: String) {
        val textView = TextView(requireActivity())
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )
        textView.gravity = Gravity.CENTER
        textView.text = data
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        textView.letterSpacing = 0.02F
        linearLayout.addView(textView)
    }


    private fun addWebView(value:String) {
        val item: LinearLayout = layoutInflater.inflate(com.assignment.tv9.R.layout.item_twitter, null) as LinearLayout
        linearLayout.addView(item)
        val webView = item.findViewById<WebView>(com.assignment.tv9.R.id.webView)
        webView.visibility = View.INVISIBLE //so that we can show progress bar util every thing is loaded
        val progressBar = item.findViewById<ProgressBar>(R.id.progress_loader)
        if (!TextUtils.isEmpty(value)) {
            val webSettings = webView.settings
            webView.setWebChromeClient(WebChromeClient())
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.loadsImagesAutomatically = true
            webSettings.defaultTextEncodingName = "UTF-8"
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL)
            webSettings.setUseWideViewPort(false)
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            webView.setScrollContainer(false);
            webView.setOnTouchListener { v: View?, event: MotionEvent -> event.action == MotionEvent.ACTION_MOVE }
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    Handler().postDelayed({
                        if (progressBar != null) {
                            progressBar.visibility = View.GONE
                        }
                        webView.visibility = View.VISIBLE
                    }, 3000)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }


                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (!TextUtils.isEmpty(url)) {
                        val uri = Uri.parse(url)
                        val twitter = Intent(Intent.ACTION_VIEW, uri)
                        twitter.setPackage("com.twitter.android")
                        try {
                            startActivity(twitter)
                        } catch (e: ActivityNotFoundException) {
                            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))
                        }
                    }
                    return true
                }
            }
            try {
                webView.loadDataWithBaseURL("https://twitter.com", value, "text/html", "utf-8", null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            if (progressBar != null)
                progressBar.visibility = View.GONE
        }
    }
}