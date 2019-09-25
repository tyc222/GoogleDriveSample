package com.example.mstar_sample.Network

import com.example.mstar_sample.Constant
import com.example.mstar_sample.Model.CloudServerResponse
import com.example.mstar_sample.Model.DriveResponse
import com.example.mstar_sample.Model.FileResponse
import com.example.mstar_sample.Model.TokenResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IRetrofit {

    // Request refresh token
    @POST("oauth2/v4/token")
    @FormUrlEncoded
    fun requestRefreshToken(
            @Field("client_id") clientId:String,
            @Field("client_secret") clientSecret:String,
            @Field("code") code:String,
            @Field("grant_type") grantType:String
    ): Observable<TokenResponse>

    // Request access token
    @POST("oauth2/v4/token")
    @FormUrlEncoded
    fun requestAccessToken(
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String,
        @Field("refresh_token") code:String,
        @Field("grant_type") grantType:String): Observable<TokenResponse>

    // Request user drive folder and files
    @GET("drive/v3/files/{id}")
    fun requestDriveFiles(
        @Header("authorization") auth:String,
        @Path("id") fileID:String?,
        @Query("q") name_mimeType_sharedWithMe_parent:String?,
        @Query("fields") fields:String?): Observable<DriveResponse>

    // Request refresh token from Cloud Server

    @PUT("gdrive/share")
    fun requestRefreshTokenFromCloudServer(
        @Body body:JSONObject): Observable<CloudServerResponse>

    companion object {
        fun createDrive() : IRetrofit {

            var logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            var httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.DRIVE_BASE_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(IRetrofit::class.java)
        }

        fun createCloudServer () :IRetrofit {

            var logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            var httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.CLOUD_SERVER_BASE_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(IRetrofit::class.java)
    }



}
}

