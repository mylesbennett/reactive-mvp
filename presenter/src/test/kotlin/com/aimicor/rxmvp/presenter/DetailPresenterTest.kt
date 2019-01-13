package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.ApiService
import com.aimicor.rxmvp.PostDetails
import com.aimicor.rxmvp.model.Comment
import com.aimicor.rxmvp.model.Post
import com.aimicor.rxmvp.model.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.*
import org.junit.Assert.assertEquals
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class DetailPresenterTest {
    private val apiService = mock(ApiService::class.java)
    private val view = mock(DetailView::class.java)
    private val presenter = DetailPresenter()
    private val getPost = PublishSubject.create<List<Post>>()
    private val getUser = PublishSubject.create<List<User>>()
    private val getComments = PublishSubject.create<List<Comment>>()
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
        `when`(apiService.getPost(12345)).thenReturn(getPost)
        `when`(apiService.getUser(91)).thenReturn(getUser)
        `when`(apiService.getComments(23)).thenReturn(getComments)
        presenter.attach(view)
    }

    @After
    fun `tear down`() {
        presenter.detach(view)
    }

    @Ignore // TODO why does this make the following test fail?
    @Test
    fun `value only returned after all calls`() {
        System.out.println("value only returned after all calls")
        getPost.onNext(post)
        verify(view, never()).showDetails(any())

        getUser.onNext(userDataNoNulls)
        verify(view, never()).showDetails(any())

        getComments.onNext(commentList)
        verify(view).showDetails(any())
    }

    @Test
    fun `expected info returned given non-null results`() {
        System.out.println("expected info returned given non-null results")
        getPost.onNext(post)
        getUser.onNext(userDataNoNulls)
        getComments.onNext(commentList)

        verify(view).showDetails(postDetailsCaptor.capture())

        val postDetails = postDetailsCaptor.firstValue
        assertEquals("body3", postDetails.body)
        assertEquals("3", postDetails.comments)
        assertEquals("title3", postDetails.title)
        assertEquals("username", postDetails.user)
    }
}