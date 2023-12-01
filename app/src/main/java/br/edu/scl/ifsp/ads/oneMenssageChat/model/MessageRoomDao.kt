package br.edu.scl.ifsp.ads.oneMenssageChat.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MessageRoomDao {
    companion object {
        private const val CONTACT_TABLE = "contact"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
    }

    @Insert
    fun createContact(contact: Contact)
    @Query("SELECT * FROM $CONTACT_TABLE WHERE $ID_COLUMN = :id")
    fun retrieveContact(id: Int): Contact?
    @Query("SELECT * FROM $CONTACT_TABLE ORDER BY $NAME_COLUMN")
    fun retrieveContacts(): MutableList<Contact>
    @Update
    fun updateContact(contact: Contact): Int
    @Delete
    fun deleteContact(contact: Contact): Int
}