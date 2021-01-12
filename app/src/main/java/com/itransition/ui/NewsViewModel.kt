package com.itransition.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itransition.models.Article
import com.itransition.models.NewsResponse
import com.itransition.repository.NewsRepository
import com.itransition.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
    val topHeadlinesNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var topHeadlinesNewsPage = 1

    val allNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var allNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    val searchCategoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchCategoryNewsPage = 1

    init {
        getAllNews("а OR о")
    }
    fun getTopHeadlinesNews(countryCode: String) = viewModelScope.launch {
        topHeadlinesNews.postValue(Resource.Loading())
        val response = newsRepository.getTopHeadlinesNews(countryCode, topHeadlinesNewsPage)
        topHeadlinesNews.postValue(handleTopHeadlinesNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }
    fun searchCategoryNews(searchQuery: String) = viewModelScope.launch {
        searchCategoryNews.postValue(Resource.Loading())
        val response = newsRepository.searchCategoryNews(searchQuery, searchCategoryNewsPage)
        searchCategoryNews.postValue(handleSearchCategoryNewsResponse(response))
    }

    fun getAllNews(searchQuery: String) = viewModelScope.launch {
        allNews.postValue(Resource.Loading())
        val response = newsRepository.getAllNews(searchQuery, allNewsPage)
        allNews.postValue(handleAllNewsResponse(response))
    }

    private fun handleTopHeadlinesNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchCategoryNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleAllNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}