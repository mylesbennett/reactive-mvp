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
            apiService.getPost(view.postId)
                .subscribeOn(Schedulers.io())
                .map { posts -> posts[0] }
                .flatMap { post ->
                    Observable.zip(
                        Observable.just(post.title),
                        Observable.just(post.body),
                        apiService.getUser(post.userId).subscribeOn(Schedulers.io()).map { it[0].username },
                        apiService.getComments(post.id).subscribeOn(Schedulers.io()).map { it.size },
                        Function4<String?, String?, String?, Int, PostDetails> { title, body, name, comments ->
                            PostDetails(title, body, name, comments)
                        }
                    )
                }
                .observeOn(view.scheduler)
                .subscribe { view.showDetails(it) }
        )
    }
}