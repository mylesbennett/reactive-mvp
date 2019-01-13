package com.aimicor.rxmvp

import com.aimicor.rxmvp.presenter.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

val presenterBindings: Kodein.MainBuilder.() -> Unit = {
    bind<Presenter<MainView>>() with provider { MainPresenter() }
    bind<Presenter<DetailView>>() with provider { DetailPresenter() }
}