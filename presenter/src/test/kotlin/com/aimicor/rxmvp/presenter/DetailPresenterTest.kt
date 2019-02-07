package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.ApiService
import com.aimicor.rxmvp.PostDetails
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DetailPresenterTest {
    private val apiService = mock(ApiService::class.java)
    private val view = mock(DetailView::class.java)
    private val presenter = DetailPresenter()
    private val postDetailsCaptor = argumentCaptor<PostDetails>()

    @Rule
    @JvmField
    val presenterOverridesRule = OverridesRule {
        bind<ApiService>(overrides = true) with provider { apiService }
    }

    @Before
    fun `set up`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        `when`(view.scheduler).thenReturn(Schedulers.io())
        `when`(view.postId).thenReturn(12345)
    }

    @After
    fun `tear down`() {
        presenter.detach(view)
    }

    @Test
    fun `expected info returned given non-null results`() {
        `when`(apiService.getPosts(12345)).thenReturn(Single.just(post))
        `when`(apiService.getUsers(91)).thenReturn(Single.just(userDataNoNulls))
        `when`(apiService.getComments(23)).thenReturn(Single.just(commentList))

        presenter.attach(view)

        verify(view).showDetails(postDetailsCaptor.capture())
        val postDetails = postDetailsCaptor.firstValue
        assertEquals("body3", postDetails.body)
        assertEquals("3", postDetails.comments)
        assertEquals("title3", postDetails.title)
        assertEquals("username", postDetails.user)
    }
}
