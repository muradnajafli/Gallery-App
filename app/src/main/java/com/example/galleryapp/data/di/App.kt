package com.example.galleryapp.data.di

import android.app.Application


class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .contextModule(ContextModule(this))
            .build()
    }
}