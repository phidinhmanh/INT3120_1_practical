package com.example.unit5_pathway2_bookselfapp.data

import com.example.unit5_pathway2_bookselfapp.model.BookResponse
import com.example.unit5_pathway2_bookselfapp.network.BookApiService


interface BookRepository {
    suspend fun getBooks(): BookResponse
}


class BookRepositoryImpl(private val bookApiService: BookApiService) : BookRepository {
    override suspend fun getBooks(): BookResponse {
        return bookApiService.getPopularBooks()
    }
}