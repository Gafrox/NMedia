package ru.netology.nmedia.repository

import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            it.toDto()
        }
    }

    override fun save(post: Post) = dao.save(PostEntity.fromDto(post))
    override fun likeById(id: Long) = dao.likeById(id)
    override fun shareById(id: Long) = dao.shareById(id)
    override fun deleteById(id: Long) = dao.removeById(id)
}