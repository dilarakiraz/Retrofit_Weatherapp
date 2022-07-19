package com.dilara.weatherretrofit.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dilara.weatherretrofit.R
import com.dilara.weatherretrofit.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

private val TAG="MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var viewmodel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        //gelen verileri sharedpreferences çok yer kaplamadan bellekte tutar
        SET = GET.edit()

        viewmodel = ViewModelProvider(this).get(MainViewModel::class.java)
        //viewmodel nesnesi oluşturmamızı sağlar viewmodel prowider. şu anda sürümden kaldırıldı.
        //import androidx.lifecycle.ViewModelProviders kütüphanesini import androidx.lifecycle.ViewModelProvider ile değiştirdim.
        //viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java) ifadesini viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java) ile değiştirdim
        //gradledan eski sürümü değiştirdim ve sorun kalmadı.

        var cName = GET.getString("cityName", "bingöl")?.lowercase() //küçük harfe çevirir
        edt_city_name.setText(cName)
        viewmodel.refreshData(cName!!) //veri geldikçe yeniler

        getLiveData()

        constraintlayout.setOnRefreshListener{
            ll_data.visibility = View.GONE //görünmez
            tv_error.visibility = View.GONE
            pb_loading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.lowercase()
            edt_city_name.setText(cityName)
            viewmodel.refreshData(cityName!!)
            constraintlayout.isRefreshing = false
        }
        img_search_city.setOnClickListener {
            val cityName = edt_city_name.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG,"onCreate: " +cityName) //log.i ile sunucuya başarılı şekilde bağlandığımızı gösterir
        }

    }

    private fun getLiveData() {

        //observe ile data dan gelen değerleri gözlemleyebiliriz
        viewmodel.weather_data.observe(this, Observer { data ->
            data?.let {
                //null değilse
                ll_data.visibility = View.VISIBLE //görünür

                tv_city_code.text = data.sys.country.toString()
                tv_city_name.text = data.name.toString()

                Glide.with(this) //glide kütüphanesini dahil ettikten sonra resimleri göstermek iççin kullanabilirz
                    .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(img_weather_pictures)

                tv_degree.text = data.main.temp.toString() + "°C"

                tv_humidity.text = data.main.humidity.toString() + "%"
                tv_wind_speed.text = data.wind.speed.toString()
                tv_lat.text = data.coord.lat.toString()
                tv_lon.text = data.coord.lon.toString()

            }
        })

        viewmodel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    tv_error.visibility = View.VISIBLE
                    pb_loading.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    tv_error.visibility = View.GONE
                }
            }
        })

        viewmodel.weather_loading.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }
            }
        })

    }

    }
