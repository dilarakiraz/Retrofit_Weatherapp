package com.dilara.weatherretrofit.service

import com.dilara.weatherretrofit.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
//https://api.openweathermap.org/data/2.5/weather?q=bing%C3%B6l&APPID=c15916b577def8881210284eaa6ef12e
interface WeatherAPI {
    //kullanacağımız apı isteklerini yazacağım arayüz oluşturdum
    @GET("data/2.5/weather?q=bing%C3%B6l&APPID=c15916b577def8881210284eaa6ef12e") //bize verilen anahtar değeri aldık
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}