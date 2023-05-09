package com.assignment.tv9.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.assignment.tv9.MainActivity
import com.assignment.tv9.R
import com.assignment.tv9.adapter.CarouselRVAdapter
import com.assignment.tv9.adapter.DashboardListRVAdapter
import com.assignment.tv9.repository.AppRepository
import com.assignment.tv9.services.MyFirebaseMessagingService
import com.assignment.tv9.util.Resource
import com.assignment.tv9.viewModel.AdViewModel
import com.assignment.tv9.viewModel.CarouselListViewModel
import com.assignment.tv9.viewModel.DashboardListViewModel
import com.assignment.tv9.viewModel.ViewModelProviderFactory
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.abs


class DashboardFragment : Fragment() {

    private lateinit var viewModel: CarouselListViewModel
    lateinit var picsAdapter: CarouselRVAdapter

    private lateinit var viewModelDashboard: DashboardListViewModel
    private lateinit var adViewModel:AdViewModel
    lateinit var dashboardListAdapter: DashboardListRVAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(getString(R.string.web_client_id))
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
    }

    lateinit var viewPager:ViewPager2
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).sendDataToAnalytics(this.javaClass.simpleName);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.user_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.logout_menu -> {
                        googleSignInClient.signOut().addOnCompleteListener {
                            FirebaseAuth.getInstance().signOut()
                            LoginManager.getInstance().logOut();
                            Navigation.findNavController(requireView()).navigate(R.id.dashboard_to_splash)
                        }
                        return true
                    }
                    R.id.test_notification ->{
                        MyFirebaseMessagingService().generateNotification("Test Title","Test Description",requireActivity().applicationContext)
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        activity?.setTitle(R.string.dashboard)

        viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvDashBoard)
        progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        picsAdapter = activity?.let { CarouselRVAdapter(it) }!!
        dashboardListAdapter = activity?.let { DashboardListRVAdapter(it) }!!

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))


        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.resources?.let { itemDecoration.setDrawable(it.getDrawable(R.drawable.corner)) }
        recyclerView.addItemDecoration(itemDecoration)


        dashboardListAdapter = activity?.let { DashboardListRVAdapter(it) }!!
        setupViewModel()
        setupDashboardViewModel()

        viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            setPadding(0,0,0,0)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        viewPager.adapter = picsAdapter
        viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))
        }
        viewPager.setPageTransformer(pageTransformer)

        val carouselDecoration = HorizontalMarginItemDecoration(requireActivity(),R.dimen.viewpager_current_item_horizontal_margin)
        viewPager.addItemDecoration(carouselDecoration)

        return view
    }

    private fun setupDashboardViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application, AppRepository())
        viewModelDashboard = ViewModelProvider(this, factory).get(DashboardListViewModel::class.java)
        adViewModel = ViewModelProvider(requireActivity(),factory).get(AdViewModel::class.java)
        showProgressBar()
        getDashboardData()
        adViewModel.loadNativeAd()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory).get(CarouselListViewModel::class.java)
        showProgressBar()
        getPictures()
    }

    private fun getPictures() {
        viewModel.picsData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { carouselList ->
                        picsAdapter.updateViewPageList(carouselList.slider)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun getDashboardData() {
        adViewModel.nativeAd.observe(requireActivity(),Observer{
            if(it!=null){
                picsAdapter.setNativeAd(it)
            }
        })
        viewModelDashboard.dashboardData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { dashboardDataList ->
                        dashboardListAdapter.differ.submitList(dashboardDataList)
                        recyclerView.adapter = dashboardListAdapter
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }


    class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }
    }
}