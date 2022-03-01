package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likedByMe = false,
        likes = 999,
        shares = 1_099_995,
        views = 439
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (!post.likedByMe) post.likes + 1 else post.likes - 1
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(shares = post.shares + 1)
        data.value = post
    }

    override fun counter(count: Int): String {
        return when {
            (count >= 1_000_000) -> if (count % 1_000_000 > 99_999) "${"%.1f".format(count / 1_000_000.toDouble())}M" else "${count / 1_000_000}M"
            (count in 1000..9_999) -> if (count % 1_000 > 99) "${"%.1f".format(count / 1_000.toDouble())}K" else "${count / 1_000}K"
            (count in 10_000..999_999) -> "${count / 1_000}K"
            else -> count
        }.toString()
    }
}