package com.assignment.koogle.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.koogle.repository.AppRepository

class ViewModelProviderFactory(
    val app: Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardListViewModel::class.java)) {
            return DashboardListViewModel(app, appRepository) as T
        }
        else if (modelClass.isAssignableFrom(DetailsPageViewModel::class.java)) {
            return DetailsPageViewModel(app, appRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}