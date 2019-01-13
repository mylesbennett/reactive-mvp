package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.bindingsOverride
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    override fun before() {
        bindingsOverride = bindings
    }

    override fun after() {
        bindingsOverride = {}
    }
}
