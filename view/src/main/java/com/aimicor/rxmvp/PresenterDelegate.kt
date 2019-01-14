package com.aimicor.rxmvp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.aimicor.rxmvp.presenter.Presenter
import com.aimicor.rxmvp.view.MainApplication.Companion.appContext
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

interface PresenterAware : Presenter.View {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
    val presenterDelegate: PresenterDelegate<out Presenter.View>
}

class PresenterDelegate<VIEW : Presenter.View> private constructor() : LifecycleObserver {
    private lateinit var view: VIEW
    private lateinit var presenter: Presenter<VIEW>
    private lateinit var attachedLifecycle: Lifecycle

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun attach() {
        presenter.attach(view)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun detach() {
        presenter.detach(view)
        attachedLifecycle.removeObserver(this)
    }

    fun bind(lifecycle: Lifecycle) {
        attachedLifecycle = lifecycle
        attachedLifecycle.addObserver(this)
    }

    fun bind(view: VIEW) {
        this.view = view
    }

    fun bind(presenter: Presenter<VIEW>) {
        this.presenter = presenter
    }

    companion object : KodeinAware {
        override val kodein: Kodein by closestKodein(appContext)

        fun <VIEW : Presenter.View> presenterDelegate(body: PresenterDelegate<VIEW>.() -> Unit): PresenterDelegate<VIEW> {
            val presenterDelegate = PresenterDelegate<VIEW>()
            body(presenterDelegate)
            return presenterDelegate
        }

        inline fun <reified VIEW : Presenter.View> presenterDelegate(activity: FragmentActivity): PresenterDelegate<VIEW> =
            presenterDelegate {
                bind(activity as VIEW)
                bind(kodein.direct.instance<Presenter<VIEW>>())
                bind(activity.lifecycle)
            }

        inline fun <reified VIEW : Presenter.View> presenterDelegate(fragment: Fragment): PresenterDelegate<VIEW> =
            presenterDelegate {
                bind(fragment as VIEW)
                bind(kodein.direct.instance<Presenter<VIEW>>())
                bind(fragment.lifecycle)
            }
    }
}