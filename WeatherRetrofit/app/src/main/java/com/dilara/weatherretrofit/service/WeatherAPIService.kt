package com.dilara.weatherretrofit.service

import com.dilara.weatherretrofit.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {
    //https://api.openweathermap.org/data/2.5/weather?q=bing%C3%B6l&APPID=c15916b577def8881210284eaa6ef12e

    private val BASE_URL = "http://api.openweathermap.org/" //kullandığım verinin base urlsıni tanımladım

    private val api = Retrofit.Builder()//kullanacağım retrpfiti oluşturdum
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(cityName: String): Single<WeatherModel> {
        return api.getData(cityName)
    }
}