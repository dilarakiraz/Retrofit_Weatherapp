package com.dilara.weatherretrofit.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dilara.weatherretrofit.model.WeatherModel
import com.dilara.weatherretrofit.service.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private val TAG="MainViewModel"
class MainViewModel {
    private val weatherApiService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()//livedata gözlemlenebilir bir veri tutma sınıfıdır
    val weather_error = MutableLiveData<Boolean>()
    val weather_loading = MutableLiveData<Boolean>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {

        weather_loading.value = true
        disposable.add(
            weatherApiService.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {

                    override fun onSuccess(t: WeatherModel) { //başarılıysa
                        weather_data.value = t
                        weather_error.value = false
                        weather_loading.value = false
                        Log.d(TAG, "onSuccess: Success")//hata ayıklamak için.hataları yazdırır
                    }

                    override fun onError(e: Throwable) { //başarısızsa
                        weather_error.value = true
                        weather_loading.value = false
                        Log.e(TAG, "onError: " + e) //catch ifadesinde Log.e kullanılır.hatayı kaydetmek için
                    }

                })
        )

    }
}