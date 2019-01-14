package com.aimicor.rxmvp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import androidx.recyclerview.widget.DividerItemDecoration
import com.aimicor.rxmvp.PostSummary
import com.aimicor.rxmvp.PresenterAware
import com.aimicor.rxmvp.PresenterDelegate.Companion.presenterDelegate
import com.aimicor.rxmvp.R
import com.aimicor.rxmvp.presenter.MainView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView, PresenterAware {
    override val presenterDelegate = presenterDelegate<MainView>(this)
    private val adapter = PostItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        posts_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        posts_list.adapter = adapter
    }

    override fun setPosts(posts: List<PostSummary>) = adapter.setItems(posts)
    override fun itemClick() = adapter.clickEvent

    override fun showItemDetail(postId: Int) =
        supportFragmentManager.transaction {
            replace(R.id.fragment_container, DetailFragment.newInstance(postId)).addToBackStack(null)
        }
}
