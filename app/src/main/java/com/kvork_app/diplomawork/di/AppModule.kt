package com.kvork_app.diplomawork.di

import com.kvork_app.diplomawork.model.repository.RequestRepository
import org.koin.dsl.module

val appModule = module {

    single<RequestRepository> {
        get()
    }

}