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

    @GET("posts/")
    fun getAllPosts(): Observable<List<Post>>

    @GET("posts")
    fun getPost(@Query("id") postId: Int?): Observable<List<Post>>

    @GET("users")
    fun getUser(@Query("id") userId: Int?): Observable<List<User>>

    @GET("comments")
    fun getComments(@Query("postId") postId: Int?): Observable<List<Comment>>

    companion object {
        fun create(): ApiService = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build().create(ApiService::class.java)
    }
}