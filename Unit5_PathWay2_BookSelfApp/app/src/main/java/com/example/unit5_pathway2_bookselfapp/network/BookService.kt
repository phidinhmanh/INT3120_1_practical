package com.example.unit5_pathway2_bookselfapp.network
import com.example.unit5_pathway2_bookselfapp.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun getPopularBooks(
        @Query("q") query: String = "bestseller",
        @Query("maxResults") maxResults: Int = 20,
        @Query("orderBy") orderBy: String = "relevance",
        @Query("printType") printType: String = "books"
    ): BookResponse

}