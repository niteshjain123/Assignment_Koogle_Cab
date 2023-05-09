package com.assignment.tv9.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.tv9.repository.AppRepository

class ViewModelProviderFactory(
    val app: Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarouselListViewModel::class.java)) {
            return CarouselListViewModel(app, appRepository) as T
        }
        else if (modelClass.isAssignableFrom(DashboardListViewModel::class.java)) {
            return DashboardListViewModel(app, appRepository) as T
        }
        else if (modelClass.isAssignableFrom(DetailsPageViewModel::class.java)) {
            return DetailsPageViewModel(app, appRepository) as T
        }
        else if (modelClass.isAssignableFrom(AdViewModel::class.java)) {
            return AdViewModel(app, appRepository) as T
        }


        throw IllegalArgumentException("Unknown class name")
    }

}