package com.assignment.koogle.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.koogle.R
import com.assignment.koogle.adapter.DashboardListRVAdapter
import com.assignment.koogle.repository.AppRepository
import com.assignment.koogle.util.Resource
import com.assignment.koogle.viewModel.DashboardListViewModel
import com.assignment.koogle.viewModel.ViewModelProviderFactory


class DashboardFragment : Fragment() {

    private lateinit var viewModelDashboard: DashboardListViewModel
    lateinit var dashboardListAdapter: DashboardListRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        activity?.setTitle(R.string.helplist)

        recyclerView = view.findViewById<RecyclerView>(R.id.rvDashBoard)
        progressBar = view.findViewById<ProgressBar>(R.id.progressbar)

        dashboardListAdapter = activity?.let { DashboardListRVAdapter(it) }!!

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        dashboardListAdapter = activity?.let { DashboardListRVAdapter(it) }!!
        setupDashboardViewModel()
        return view
    }

    private fun setupDashboardViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application, AppRepository())
        viewModelDashboard = ViewModelProvider(this, factory).get(DashboardListViewModel::class.java)
        showProgressBar()
        getDashboardData()
    }

    private fun getDashboardData() {
        viewModelDashboard.dashboardData.observe(requireActivity(), Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { dashboardDataList ->
                        dashboardListAdapter.differ.submitList(dashboardDataList.helpList)
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

}