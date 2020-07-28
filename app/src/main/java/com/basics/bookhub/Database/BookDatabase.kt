package com.basics.bookhub.Database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookEntity::class],version = 1)
abstract class BookDatabase:RoomDatabase() {

    abstract fun bookDao():BookDao

}