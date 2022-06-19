package com.deva.submission_intermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deva.submission_intermediate.databinding.ItemRowStoriesBinding
import com.deva.submission_intermediate.model.UserModel
import com.deva.submission_intermediate.response.ListStoryItem
import com.deva.submission_intermediate.view.detail.DetailActivity

class ListStoriesAdapter :
    PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowStoriesBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(private val binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(data: ListStoryItem) {
                binding.tvNameItem.text = data.name
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(binding.imgStoriesItem)

                itemView.setOnClickListener {

                    val i = Intent(itemView.context,DetailActivity::class.java)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(binding.tvNameItem,"name"),
                            Pair(binding.imgStoriesItem,"story"),
                            Pair(binding.imgUserItem,"profile"),
                        )

                    val user = UserModel(
                        data.id,
                        data.name!!,
                        data.description!!,
                        data.photoUrl!!,
                        data.createdAt!!,
                        data.lat.toString(),
                        data.lon.toString()
                    )
                    i.putExtra(DetailActivity.EXTRA_USER,user)
                    itemView.context.startActivity(i, optionsCompat.toBundle())
                }
            }
        }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}