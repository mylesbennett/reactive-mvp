package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.ApiService
import com.aimicor.rxmvp.PostSummary
import com.aimicor.rxmvp.model.Post
import com.nhaarman.mockito_kotlin.argumentCaptor
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mockito.Mockito.*

class MainPresenterTest {
    private val apiService = mock(ApiService::class.java)
    private val view = mock(MainView::class.java)
    private val presenter = MainPresenter()
    private val getAllPosts = PublishSubject.create<List<Post>>()
    private val itemClick = PublishSubject.create<Int>()
    private val postListSummaryCaptor = argumentCaptor<List<PostSummary>>()

    @Rule
    @JvmField
    val presenterOverridesRule = OverridesRule {
        bind<ApiService>(overrides = true) with provider { apiService }
    }

    @Before
    fun `set up`() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        `when`(view.scheduler).thenReturn(Schedulers.io())
        `when`(apiService.getAllPosts()).thenReturn(getAllPosts)
        `when`(view.itemClick()).thenReturn(itemClick)
        presenter.attach(view)
    }

    @After
    fun `tear down`() {
        presenter.detach(view)
    }

    @Test
    fun `list returned when posts retrieved`() {
        getAllPosts.onNext(postList)

        verify(view).setPosts(postListSummaryCaptor.capture())
        val list = postListSummaryCaptor.firstValue
        assertEquals(2, list.size)
        assertEquals("title1", list[0].title)
        assertEquals(34, list[0].postId)
        assertEquals("title2", list[1].title)
        assertEquals(78, list[1].postId)
    }

    @Test
    fun `item detail requested when item click event received`() {
        verify(view, never()).showItemDetail(anyInt())
        itemClick.onNext(123)
        verify(view).showItemDetail(123)
    }
}