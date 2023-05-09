package com.assignment.tv9.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.assignment.tv9.R
import com.assignment.tv9.repository.AppRepository
import com.assignment.tv9.util.Resource
import com.assignment.tv9.util.Utils.hasInternetConnection
import retrofit2.Response
import java.io.IOException
import androidx.lifecycle.viewModelScope
import com.assignment.tv9.model.Details.DetailsData
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class DetailsPageViewModel (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val detailsData: MutableLiveData<Resource<DetailsData>> = MutableLiveData()

    init {
        getDetailsData()
    }

    fun getDetailsData() = viewModelScope.launch {
        val random = ThreadLocalRandom.current().nextInt(1, 12 + 1);
        fetchDetails(random)
    }


    private suspend fun fetchDetails(apiCall:Int) {
        detailsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(this.app)) {
                val response:Response<DetailsData>
                if(apiCall in 1..4)
                {
                    response = appRepository.getDetailsPageDataApi1()
                }
                else if(apiCall in 5..8){
                    response = appRepository.getDetailsPageDataApi2()
                }
                else{
                    response = appRepository.getDetailsPageDataApi3()
                }
                detailsData.postValue(handlePicsResponse(response))
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

    private fun handlePicsResponse(response: Response<DetailsData>): Resource<DetailsData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}