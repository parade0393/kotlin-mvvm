package me.parade.architecture.mvvm.network

import com.safframework.http.interceptor.LoggingInterceptor
import me.parade.architecture.mvvm.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author : parade
 * date : 2020 07 2020/7/14
 * description :
 */
class RetrofitClient() {
    companion object{
        fun getInstance() = SingletonHolder.INSTANCE
        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder{
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl("")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(LoggingInterceptor.Builder().loggable(BuildConfig.DEBUG).request().requestTag("Request").response().responseTag("Response").build())
            .connectTimeout(10L,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .build()
    }

    fun<T> create(service:Class<T>):T{
        return retrofit.create(service)
    }
}