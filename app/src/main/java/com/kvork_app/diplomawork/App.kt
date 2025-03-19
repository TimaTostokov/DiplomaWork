package com.kvork_app.diplomawork

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@App)
        }
    }

}