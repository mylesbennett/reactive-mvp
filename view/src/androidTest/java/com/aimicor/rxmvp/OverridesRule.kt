package com.aimicor.rxmvp

import androidx.test.platform.app.InstrumentationRegistry
import com.aimicor.rxmvp.view.MainApplication
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    private val app by lazy { InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MainApplication }

    override fun before() {
        app.bindingsOverride = bindings
    }

    override fun after() {
        app.bindingsOverride = {}
    }
}
