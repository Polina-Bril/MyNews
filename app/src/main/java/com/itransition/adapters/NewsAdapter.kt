package com.itransition.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itransition.R
import com.itransition.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

//to show Articles in RecyclerView
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    //callback for asynch List Differ  to Update of Articles only with changes, not all. And to not stop for that Main Thread
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //because the are no id of article in API
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    //will run asynhronise, don't stopping Main
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            fabi.setOnClickListener {
                onHeartClickListener?.let { it(article) }//it reffers to onHartClickListener Lambda function
            }
            setOnClickListener {
                onItemClickListener?.let { it(article) }//it reffers to onItemClickListener Lambda function
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    //save to open wright article onclick
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    private var onHeartClickListener: ((Article) -> Unit)? = null

    //save to open wright article onclick
    fun setOnHeartClickListener(listener: (Article) -> Unit) {
        onHeartClickListener = listener
    }
}