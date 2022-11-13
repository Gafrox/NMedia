package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityPostContentBinding

class PostContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editPost.requestFocus()
        val content = intent?.extras?.getString(Intent.EXTRA_TEXT)
        content?.let { binding.editPost.setText(content) }
        binding.OK.setOnClickListener {
            val intent = Intent()
            val text = binding.editPost.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(Activity.RESULT_OK, intent.apply { putExtra(Intent.EXTRA_TEXT, text) })
            }
            finish()
        }
    }

    object Contract : ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String) =
            Intent(context, PostContentActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT, input)
            }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(Intent.EXTRA_TEXT)
            } else {
                null
            }
    }
}