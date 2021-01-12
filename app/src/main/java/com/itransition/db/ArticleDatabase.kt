package com.itransition.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itransition.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class ArticleDatabase : RoomDatabase() {
    //don't need the realisation because room create it for us
    abstract fun getArticleDao(): ArticleDao

    // to create a Database. volatile that other threads immediatle see the changes of instance
    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null

        //to sinchronize setting the instance
        private val LOCK = Any()

        //this fun will work only once when object of DB is creating
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()

    }

}
