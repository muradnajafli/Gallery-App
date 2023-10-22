package com.example.galleryapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galleryapp.data.model.PhotoItem
import com.example.galleryapp.data.network.ApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

import io.reactivex.rxjava3.subscribers.ResourceSubscriber
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    val photoResponse: MutableLiveData<List<PhotoItem>> = MutableLiveData()
    val errorMessage: MutableLiveData<String?> = MutableLiveData()
    fun getImagesFromApi(page: Int) {
        apiService.getPhotos(page, 30)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ResourceSubscriber<Response<List<PhotoItem>>>() {
                override fun onNext(response: Response<List<PhotoItem>>) {
                    if (response.isSuccessful) {
                        photoResponse.value = response.body()
                    } else {
                        errorMessage.value = "ERROR CODE: ${response.code()}"
                    }
                }

                override fun onError(e: Throwable) {
                    errorMessage.value = "${e.message}"
                }

                override fun onComplete() {

                }
            })
    }
}