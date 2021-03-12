package com.leozhi.shinian.di

import com.leozhi.shinian.model.repo.HomeRepository
import com.leozhi.shinian.model.repo.MainRepository
import com.leozhi.shinian.model.repo.MineRepository
import com.leozhi.shinian.view.MainViewModel
import com.leozhi.shinian.view.home.HomeViewModel
import com.leozhi.shinian.view.mine.MineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author leozhi
 * @date 21-2-27
 */
val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MineViewModel(get()) }
}

val repositoryModule = module {
    single { MainRepository() }
    single { HomeRepository() }
    single { MineRepository() }
}

val remoteModule = module {

}

val localModule = module {

}

val appModule = listOf(viewModelModule, repositoryModule, remoteModule, localModule)