package com.basics.bookhub.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insert(bookEntity: BookEntity)

    @Delete
    fun delete(bookEntity: BookEntity)


    @Query(value = "Select * From books")
    fun getAllBooks():List<BookEntity>


    @Query(value = "Select * From books Where book_id = :bookId")
    fun getBookById(bookId:String):BookEntity
}