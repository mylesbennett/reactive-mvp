package com.aimicor.rxmvp.presenter

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface Presenter<in VIEW : Presenter.View> {
    fun attach(view: VIEW)
    fun detach(view: VIEW)
    interface View {
        val scheduler : Scheduler
    }
}

abstract class AbstractPresenter<in VIEW : Presenter.View> :
    Presenter<VIEW> {

    private lateinit var viewSubscriptions: CompositeDisposable

    override fun attach(view: VIEW) {
        viewSubscriptions = CompositeDisposable()
        onAttach(view)
    }

    override fun detach(view: VIEW) {
        this.viewSubscriptions.dispose()
    }

    protected fun unsubscribeOnDetach(vararg subscriptions: Disposable) {
        viewSubscriptions.addAll(*subscriptions)
    }

    protected abstract fun onAttach(view: VIEW)
}