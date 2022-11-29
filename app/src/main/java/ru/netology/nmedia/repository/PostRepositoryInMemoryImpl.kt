package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun deleteById(id: Long)
    fun save(post: Post)
    fun getPostById(id: Long): Post?
}

class PostRepositoryInMemoryImpl : PostRepository {
    private val currentAuthor = "Netology"
    private val currentTime = "Now"
    private var nextId = 1L
    private var posts = List(10) {
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "№$it Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 1_099_998,
            views = 439,
            video = "https://youtu.be/EQ_btxhpRzU"
        )
    }
    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (id == it.id) {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
                )
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (id == it.id) {
                it.copy(shares = it.shares + 1)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun deleteById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = currentAuthor,
                    likedByMe = false,
                    published = currentTime
                )
            ) + posts
            data.value = posts
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun getPostById(id: Long) = data.value?.find { it.id == id }
}