package com.aimicor.rxmvp

import com.aimicor.rxmvp.model.Comment
import com.aimicor.rxmvp.model.Post
import com.aimicor.rxmvp.model.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    fun getPosts(@Query("id") postId: Int? = null): Observable<List<Post>>

    @GET("users")
    fun getUsers(@Query("id") userId: Int? = null): Observable<List<User>>

    @GET("comments")
    fun getComments(@Query("postId") postId: Int? = null): Observable<List<Comment>>

    companion object {
        fun create(): ApiService = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build().create(ApiService::class.java)
    }
}