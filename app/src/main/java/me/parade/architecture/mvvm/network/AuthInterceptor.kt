package me.parade.architecture.mvvm.network

import me.parade.architecture.mvvm.util.ext.getSpValue
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author : parade
 * date : 2020/8/25
 * description :
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = getSpValue("token","")

        request = request.newBuilder().header("Token", if (token.isEmpty()) "" else token)
            .header("Client", "ANDROID").build()

        return chain.proceed(request)
    }
}