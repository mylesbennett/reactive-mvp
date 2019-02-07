package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.ApiService
import com.aimicor.rxmvp.PostDetails
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DetailPresenter(override val kodein: Kodein = com.aimicor.rxmvp.kodein) :
    AbstractPresenter<DetailView>(), KodeinAware {
    private val apiService: ApiService by instance()

    override fun onAttach(view: DetailView) {
        unsubscribeOnDetach(
            apiService.getPosts(view.postId)
                .subscribeOn(Schedulers.io())
                .map { posts -> posts[0] }
                .flatMap { post ->
                    Observable.zip<String, String, String, String, PostDetails>(
                        Observable.just(post.title),
                        Observable.just(post.body),
                        apiService.getUsers(post.userId).subscribeOn(Schedulers.io()).map { it[0].username },
                        apiService.getComments(post.id).subscribeOn(Schedulers.io()).map { it.size.toString() },
                        Function4 { title, body, name, comments -> PostDetails(title, body, name, comments) }
                    )
                }
                .observeOn(view.scheduler)
                .subscribe { view.showDetails(it) }
        )
    }
}