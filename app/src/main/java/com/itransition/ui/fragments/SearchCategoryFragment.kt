package com.itransition.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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


class SearchCategoryFragment : Fragment(R.layout.fragment_search_category_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchCategoryFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply{
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchCategoryFragment_to_articleFragment,
                bundle
            )
        }
        newsAdapter.setOnHeartClickListener {
            viewModel.saveArticle(it)
            //to show to user the message that we save the article
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

        etSearchCategory.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View,
                        i: Int,
                        l: Long
                    ) {
                        var category = etSearchCategory.selectedItem.toString()
                        viewModel.searchCategoryNews(category)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        })

        viewModel.searchCategoryNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
              }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchCategoryNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}