package com.itransition.repository

import com.itransition.api.RetrofitInstance
import com.itransition.db.ArticleDatabase
import com.itransition.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getTopHeadlinesNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getTopHeadlinesNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun searchCategoryNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchCategoryNews(searchQuery, pageNumber)

    suspend fun getAllNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getAllNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}