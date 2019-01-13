package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.PostDetails

interface DetailView: Presenter.View {
    val postId: Int
    fun showDetails(details: PostDetails)
}