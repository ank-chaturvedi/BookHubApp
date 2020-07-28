package com.basics.bookhub.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey      var book_id:Int,
    @ColumnInfo(name = "book_name")           var name: String,
    @ColumnInfo(name = "book_author")          var author: String,
    @ColumnInfo(name = "book_rating")          var rating: String,
    @ColumnInfo(name = "book_price")          var price: String,
    @ColumnInfo(name = "book_desc")            var desc:String,
    @ColumnInfo(name = "book_image")          var image:String


)