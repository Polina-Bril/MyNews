package com.itransition.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.itransition.R
import com.itransition.adapters.NewsAdapter
import com.itransition.ui.NewsActivity
import com.itransition.ui.NewsViewModel
import com.itransition.util.Resource
import kotlinx.android.synthetic.main.fragment_search_category_news.*
import kotlinx.android.synthetic.main.fragment_topheadlines_news.*

class TopHeadlinesNewsFragment : Fragment(R.layout.fragment_topheadlines_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "TopHeadlineNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_topHeadlinesNewsFragment_to_articleFragment,
                bundle
            )
        }
        newsAdapter.setOnHeartClickListener {
            viewModel.saveArticle(it)
            //to show to user the message that we save the article
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
        etSearchLanguage.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                var language = etSearchLanguage.selectedItem.toString()
                viewModel.getTopHeadlinesNews(language)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        })
        viewModel.topHeadlinesNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvTopHeadlinesNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}