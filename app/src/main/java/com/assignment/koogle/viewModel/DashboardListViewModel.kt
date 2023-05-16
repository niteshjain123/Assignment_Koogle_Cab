package com.assignment.koogle.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.assignment.koogle.R
import com.assignment.koogle.repository.AppRepository
import com.assignment.koogle.util.Resource
import com.assignment.koogle.util.Utils.hasInternetConnection
import retrofit2.Response
import java.io.IOException
import androidx.lifecycle.viewModelScope
import com.assignment.koogle.model.Dashboard.DashboardList
import kotlinx.coroutines.launch

class DashboardListViewModel (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val dashboardData: MutableLiveData<Resource<DashboardList>> = MutableLiveData()

    init {
        getDashboard()
    }

    fun getDashboard() = viewModelScope.launch {
        fetchDashboardList()
    }

    private suspend fun fetchDashboardList() {
        dashboardData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(this.app)) {
                val response = appRepository.getDashboardListData()
                dashboardData.postValue(handleApiResponse(response))
            } else {
                dashboardData.postValue(Resource.Error(this.app.getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> dashboardData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> dashboardData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleApiResponse(response: Response<DashboardList>): Resource<DashboardList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}