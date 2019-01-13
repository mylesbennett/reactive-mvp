package com.aimicor.rxmvp

import com.aimicor.rxmvp.presenter.Presenter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

interface DefaultScheduler : Presenter.View {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}