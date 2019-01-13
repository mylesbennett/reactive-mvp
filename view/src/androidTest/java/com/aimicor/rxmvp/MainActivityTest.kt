package com.aimicor.rxmvp

import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.aimicor.rxmvp.presenter.DetailView
import com.aimicor.rxmvp.presenter.MainView
import com.aimicor.rxmvp.presenter.Presenter
import com.aimicor.rxmvp.view.MainActivity
import com.aimicor.rxmvp.view.PostItemAdapter
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val presenter = spy(TestPresenter::class.java)
    private val detailPresenter = spy(TestPresenter::class.java)

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val presenterOverridesRule = OverridesRule {
        bind<Presenter<MainView>>(overrides = true) with provider { presenter }
        bind<Presenter<DetailView>>(overrides = true) with provider { detailPresenter }
    }

    private val activity by lazy { activityRule.activity as MainActivity }
    private val itemClickObserver = TestObserver<Int>()

    @Before
    @UiThreadTest
    fun set_up() {
        activity.itemClick().subscribe(itemClickObserver)
    }

    @After
    fun tear_down() {
        itemClickObserver.dispose()
    }

    @Test
    fun test_attach_and_detach_called() {
        presenter.attachLatch.await(5, TimeUnit.SECONDS)
        verify(presenter).attach(activity)

        activity.finish()
        presenter.detachLatch.await(5, TimeUnit.SECONDS)
        verify(presenter).detach(activity)
    }

    @Test
    fun test_item_click_event() {
        activity.runOnUiThread {
            activity.setPosts(
                listOf(
                    PostSummary(123, "title1"),
                    PostSummary(456, "title2")
                )
            )
        }

        val actionOnItemAtPosition = actionOnItemAtPosition<PostItemAdapter.ViewHolder>(1, click())
        onView(withId(R.id.posts_list)).perform(actionOnItemAtPosition)

        itemClickObserver.assertValueCount(1)
        assertEquals(456, itemClickObserver.values()[0])
    }

    @Test
    fun test_attach_and_detach_on_fragment_called() {
        activity.runOnUiThread { activity.showItemDetail(0) }
        detailPresenter.attachLatch.await(5, TimeUnit.SECONDS)
        val fragment = activity.supportFragmentManager.findFragmentById(R.id.fragment_container) as DetailView
        verify(detailPresenter).attach(fragment)

        activity.supportFragmentManager.popBackStack()
        detailPresenter.detachLatch.await(5, TimeUnit.SECONDS)
        verify(detailPresenter).detach(fragment)
    }
}
