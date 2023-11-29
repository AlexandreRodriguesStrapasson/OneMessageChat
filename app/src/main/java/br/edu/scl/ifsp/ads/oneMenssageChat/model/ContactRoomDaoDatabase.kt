package br.edu.scl.ifsp.ads.oneMenssageChat.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactRoomDaoDatabase: RoomDatabase() {
    abstract fun getContactRoomDao(): ContactRoomDao
}