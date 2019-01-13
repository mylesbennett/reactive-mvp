package com.aimicor.rxmvp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aimicor.rxmvp.PostSummary
import com.aimicor.rxmvp.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.post_item_layout.view.*

class PostItemAdapter(val context: Context) :
    RecyclerView.Adapter<PostItemAdapter.ViewHolder>() {

    private val postList = mutableListOf<PostSummary>()
    private val clickSubject = PublishSubject.create<Int>()
    val clickEvent: Observable<Int> = clickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.post_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txtPostTitle.text = postList[position].title
    }

    fun setItems(list: List<PostSummary>) {
        postList.clear()
        postList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                clickSubject.onNext(postList[layoutPosition].postId)
            }
        }
    }
}