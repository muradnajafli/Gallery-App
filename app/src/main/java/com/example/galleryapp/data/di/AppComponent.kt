package com.example.galleryapp.data.di

import com.example.galleryapp.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ContextModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}