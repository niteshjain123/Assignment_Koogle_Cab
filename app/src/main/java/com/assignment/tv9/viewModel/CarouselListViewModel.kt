package com.assignment.tv9.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.assignment.tv9.R
import com.assignment.tv9.model.Carousel.CarouselList
import com.assignment.tv9.repository.AppRepository
import com.assignment.tv9.util.Resource
import com.assignment.tv9.util.Utils.hasInternetConnection
import retrofit2.Response
import java.io.IOException
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CarouselListViewModel (
    val app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {

    val picsData: MutableLiveData<Resource<CarouselList>> = MutableLiveData()

    init {
        getPictures()
    }

    fun getPictures() = viewModelScope.launch {
        fetchPics()
    }


    private suspend fun fetchPics() {
        picsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(this.app)) {
                val response = appRepository.getCarouselData()
                picsData.postValue(handlePicsResponse(response))
            } else {
                picsData.postValue(Resource.Error(this.app.getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> picsData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> picsData.postValue(
                    Resource.Error(
                        this.app.getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handlePicsResponse(response: Response<CarouselList>): Resource<CarouselList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}