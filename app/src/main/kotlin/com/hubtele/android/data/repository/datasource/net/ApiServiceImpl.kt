package com.hubtele.android.data.repository.datasource.net

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hubtele.android.BuildConfig
import com.hubtele.android.MyApplication
import com.hubtele.android.R
import com.hubtele.android.helper.MyGson
import com.squareup.okhttp.Cache
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Response
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.ErrorHandler;
import retrofit.client.OkClient
import retrofit.converter.GsonConverter
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

object ApiServiceImpl {
    val USER_AGENT = "hubtele/android/${BuildConfig.VERSION_NAME}/${android.os.Build.MODEL}:${android.os.Build.VERSION.RELEASE}:${android.os.Build.MANUFACTURER}:${android.os.Build.PRODUCT}";
    val apiClient: ApiService by lazy { createApiService() }

    private fun createApiService(): ApiService {
        return RestAdapter.Builder()
                .setEndpoint(BuildConfig.API_URL)
                .setLogLevel(if (BuildConfig.DEBUG) RestAdapter.LogLevel.BASIC else RestAdapter.LogLevel.BASIC)
                .setConverter(GsonConverter(MyGson.gson))
                .setClient(createOkClient())
                .setErrorHandler(CustomErrorHandler(MyApplication.getContext()))
                .build().create(ApiService::class.java)
    }

    private fun createOkClient(): OkClient {
        var ok = OkHttpClient()
        val SIZE_OF_CACHE = 10 * 1024 * 1024L;
        try {
            var responseCache = Cache(MyApplication.getContext().cacheDir, SIZE_OF_CACHE)
            ok.setCache(responseCache)
        } catch(e: Exception) {
            Timber.e(e.message);
        }
        ok.networkInterceptors().add(UserAgentInterceptor())
        ok.setReadTimeout(30, TimeUnit.SECONDS)
        ok.setConnectTimeout(30, TimeUnit.SECONDS);
        return OkClient(ok)
    }

    class UserAgentInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder().removeHeader("User-Agent").addHeader("User-Agent", USER_AGENT).build()
            return chain.proceed(requestWithUserAgent)
        }
    }

    class CustomErrorHandler(val ctx: Context) : ErrorHandler {

        override fun handleError(cause: RetrofitError?): Throwable? {

            var errorDescription: String? = null
            //            Timber.e("cause:$cause,${cause?.isNetworkError},${cause?.response}");
            if (cause?.kind == RetrofitError.Kind.NETWORK) {
                errorDescription = ctx.getString(R.string.error_network);
            } else {
                if ((cause?.getResponse() == null)) {
                    errorDescription = ctx.getString(R.string.error_network);
                } else {

                    // Error message handling - return a simple error to Retrofit handlers..
                    try {
                        var errorResponse = cause!!.getBodyAs(ErrorResponse::class.java) as ErrorResponse
                        errorDescription = errorResponse.error?.data?.message;
                        Timber.e(errorResponse.error?.data?.message);
                    } catch (ex: Exception) {
                        try {
                            Timber.e(ex.message);
                            errorDescription = ctx.getString(R.string.error_network)
                        } catch (ex2: Exception) {
                            Timber.e("handle error");
                            errorDescription = ctx.getString(R.string.error_network);
                            Timber.e(ex2.message);
                        }
                    }
                }
            }
            Toast.makeText(ctx, ctx.getString(R.string.error_network), Toast.LENGTH_SHORT).show()
            return Exception(errorDescription)
        }
    }

    class ErrorResponse {
        var error: Error? = null

        class Error {
            var data: Data? = null

            class Data {
                var message: String? = null
            }
        }
    }
}
