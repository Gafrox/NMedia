package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun onCancel(post: Post) {}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}


class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            inputText.text = post.content
            likeCount.text = counter(post.likes)
            shareCount.text = counter(post.shares)
            viewCount.text = counter(post.views)
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24
            )

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }

    private fun counter(count: Int): String {
        return when {
            (count >= 1_000_000) -> if (count % 1_000_000 > 99_999) "${"%.1f".format(count / 1_000_000.toDouble())}M" else "${count / 1_000_000}M"
            (count in 1000..9_999) -> if (count % 1_000 > 99) "${"%.1f".format(count / 1_000.toDouble())}K" else "${count / 1_000}K"
            (count in 10_000..999_999) -> "${count / 1_000}K"
            else -> count
        }.toString()
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}