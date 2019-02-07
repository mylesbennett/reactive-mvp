package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.ApiService
import com.aimicor.rxmvp.PostSummary
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MainPresenter(override val kodein: Kodein = com.aimicor.rxmvp.kodein) :
    AbstractPresenter<MainView>(), KodeinAware {
    private val apiService: ApiService by instance()

    override fun onAttach(view: MainView) {
        unsubscribeOnDetach(
            apiService.getPosts()
                .subscribeOn(Schedulers.io())
                .flatMap {list ->
                    Observable.fromIterable(list)
                        .map { PostSummary(it.id, it.title) }
                        .toList()
                        .toObservable()
                }
                .observeOn(view.scheduler)
                .subscribe { view.setPosts(it) },

            view.itemClick()
                .subscribeOn(view.scheduler)
                .subscribe { view.showItemDetail(it) }
        )
    }
}