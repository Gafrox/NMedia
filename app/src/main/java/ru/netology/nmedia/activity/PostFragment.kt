package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.Counter
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    companion object {
        var Bundle.textId: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        val id = arguments?.textId?.toLong()
        bind(binding, id)
        viewModel.data.observe(viewLifecycleOwner) { bind(binding, id) }
        return binding.root
    }

    private fun bind(binding: FragmentPostBinding, id: Long?) {
        val post = viewModel.getPostById(id!!)
        binding.apply {
            author.text = post.author
            published.text = post.published
            inputText.text = post.content
            share.text = Counter.toCount(post.shares)
            view.text = Counter.toCount(post.views)
            like.isChecked = post.likedByMe
            like.text = Counter.toCount(post.likes)
            if (!post.video.isNullOrBlank()) {
                video.visibility = View.VISIBLE
            } else video.visibility = View.GONE
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                viewModel.deleteById(post.id)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.edit(post)
                                findNavController().navigate(
                                    R.id.action_postFragment_to_newPostFragment,
                                    Bundle().apply { textArg = post.content }
                                )
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            like.setOnClickListener {
                viewModel.likeById(post.id)
            }
            share.setOnClickListener {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
            video.setOnClickListener {
                if (post.video.isNullOrBlank()) return@setOnClickListener
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }
        }
    }
}