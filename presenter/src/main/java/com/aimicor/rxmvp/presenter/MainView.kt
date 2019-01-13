package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.PostSummary
import io.reactivex.Observable

interface MainView: Presenter.View {
    fun setPosts(posts: List<PostSummary>)
    fun itemClick(): Observable<Int>
    fun showItemDetail(postId: Int)
}