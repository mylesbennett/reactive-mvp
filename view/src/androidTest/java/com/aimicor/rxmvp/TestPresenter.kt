package com.aimicor.rxmvp

import com.aimicor.rxmvp.presenter.Presenter
import java.util.concurrent.CountDownLatch

open class TestPresenter : Presenter<Presenter.View> {
    val attachLatch = CountDownLatch(1)
    val detachLatch = CountDownLatch(1)
    override fun attach(view: Presenter.View) = attachLatch.countDown()
    override fun detach(view: Presenter.View) = detachLatch.countDown()
}