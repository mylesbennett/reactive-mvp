package com.aimicor.rxmvp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aimicor.rxmvp.DefaultScheduler
import com.aimicor.rxmvp.PostDetails
import com.aimicor.rxmvp.PresenterAware
import com.aimicor.rxmvp.PresenterDelegate.Companion.presenterDelegate
import com.aimicor.rxmvp.R
import com.aimicor.rxmvp.presenter.DetailView
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(), DetailView, PresenterAware, DefaultScheduler {
    override val presenterDelegate = presenterDelegate<DetailView>(this)
    override val postId: Int by lazy { arguments!!.getInt(POST_ID_KEY)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_detail, container, false)

    override fun showDetails(details: PostDetails) {
        title.text = details.title
        body.text = details.body
        username.text = details.user
        comments.text = details.comments
    }

    companion object {
        const val POST_ID_KEY = "POST_ID_KEY"
        fun newInstance(postId: Int): Fragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putInt(POST_ID_KEY, postId)
            fragment.arguments = bundle
            return fragment
        }
    }
}