package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private val posts = List(10) {
        Post(
            id = it.toLong(),
            author = "Нетология. Университет интернет-профессий будущего",
            content = "№$it Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 1_099_995,
            views = 439
        )
    }
    private val data: MutableLiveData<List<Post>> = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        data.value = data.value?.map {
            if (id == it.id) {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
                )
            } else {
                it
            }
        }
    }

    override fun shareById(id: Long) {
        data.value = data.value?.map {
            if (id == it.id) {
                it.copy(shares = it.shares + 1)
            } else {
                it
            }
        }
    }
}