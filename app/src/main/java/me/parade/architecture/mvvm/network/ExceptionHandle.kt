package me.parade.architecture.mvvm.network

import android.net.ParseException
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import me.parade.architecture.mvvm.util.ext.isValidateJson
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * @author : parade
 * date : 2020/7/12
 * description : 异常类
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        if (e is HttpException) {
            ex = transferException(e)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {
            ex = ResponseThrowable(ERROR.PARSE_ERROR ,e)
        } else if (e is ConnectException) {
            ex = ResponseThrowable(ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = ResponseThrowable(ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else {
            /*ex = if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e)
            else ResponseThrowable(ERROR.UNKNOWN, e)*/
            ex = if(e is ResponseThrowable) e else if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e) else ResponseThrowable(ERROR.UNKNOWN, e)
        }
        return ex
    }

    private fun transferException(e:Throwable): ResponseThrowable {
        val httpException:HttpException = e as HttpException
        val errorResponse: ResultException?
        val string = httpException.response()?.errorBody()?.string()

        if (string!!.isValidateJson()){
            errorResponse = Gson().fromJson(string,ResultException::class.java)
            val message = errorResponse.message
            return ResponseThrowable(1003,message,e)
        }

        return ResponseThrowable(ERROR.HTTP_ERROR, e)
    }
}