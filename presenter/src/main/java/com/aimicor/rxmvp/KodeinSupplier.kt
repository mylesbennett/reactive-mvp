package com.aimicor.rxmvp

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

internal var bindingsOverride: Kodein.MainBuilder.() -> Unit = {}

internal val kodein = Kodein.lazy {
    bind<ApiService>() with singleton { ApiService.create() }
    bindingsOverride()
}