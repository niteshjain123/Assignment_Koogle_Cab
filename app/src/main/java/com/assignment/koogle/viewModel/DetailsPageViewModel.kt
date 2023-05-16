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
import com.assignment.koogle.model.Details.DetailsData
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom

class DetailsPageViewModel (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val detailsData: MutableLiveData<Resource<DetailsData>> = MutableLiveData()

    fun getDetailsData(id:Int) = viewModelScope.launch {
        fetchDetails(id)
    }

    private suspend fun fetchDetails(id:Int) {
        detailsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(this.app)) {
                val response:Response<DetailsData>
                response = appRepository.getDetailsPageData(id)
                detailsData.postValue(handleApiResponse(response))
            } else {
                detailsData.postValue(Resource.Error(this.app.getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> detailsData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> detailsData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleApiResponse(response: Response<DetailsData>): Resource<DetailsData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}