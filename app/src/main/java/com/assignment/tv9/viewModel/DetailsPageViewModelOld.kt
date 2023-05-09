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
import kotlinx.coroutines.launch

class DetailsPageViewModelOld (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

  //  val detailsData: MutableLiveData<Resource<List<DetailsData>>> = MutableLiveData()

    init {
        getDetailsData()
    }

    fun getDetailsData() = viewModelScope.launch {
        fetchDetails()
    }


    private suspend fun fetchDetails() {
        /*detailsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(this.app)) {
                val response = appRepository.getDetailsPageData()
//                detailsData.postValue(handlePicsResponse(response))
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
        }*/
    }

/*    private fun handlePicsResponse(response: Response<List<DetailsData>>): Resource<List<DetailsData>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }*/


}