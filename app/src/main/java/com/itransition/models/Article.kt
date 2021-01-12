package com.itransition.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    //to save the object in DB we need a class db/Converter
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
//to send this class between the different fragments with the nav components (Kotlin do it by himself)
):Serializable
