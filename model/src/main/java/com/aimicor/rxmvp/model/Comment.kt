package com.aimicor.rxmvp.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("body") val body: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("postId") val postId: Int
)