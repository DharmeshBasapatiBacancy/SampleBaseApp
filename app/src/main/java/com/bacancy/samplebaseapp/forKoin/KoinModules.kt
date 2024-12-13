package com.bacancy.samplebaseapp.forKoin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Single instance of Repository
    single<UserRepository> { UserRepositoryImpl() }

    // Factory for ViewModel
    viewModel { UserViewModel(get()) }
}