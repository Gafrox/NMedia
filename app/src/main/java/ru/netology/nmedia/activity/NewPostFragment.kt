package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg?.let(binding.editText::setText)
        binding.editText.focusAndShowKeyboard()
        binding.OK.setOnClickListener {
            val content = binding.editText.text.toString()
            if (content.isNotBlank()) {
                viewModel.editPost(content)
            } else {
                viewModel.cancelEdit()
                Toast.makeText(requireContext(), R.string.error_empty_content, Toast.LENGTH_LONG)
                    .show()
            }
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onResume() {
        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        AndroidUtils.showKeyboard(binding.editText)
        super.onResume()
    }
}