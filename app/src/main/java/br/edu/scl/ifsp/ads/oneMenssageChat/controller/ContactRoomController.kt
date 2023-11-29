package br.edu.scl.ifsp.ads.oneMenssageChat.controller

import android.os.Message
import androidx.room.Room
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.CONTACT_ARRAY
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactRoomDao
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactRoomDao.Companion.CONTACT_DATABASE_FILE
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactRoomDaoDatabase
import br.edu.scl.ifsp.ads.oneMenssageChat.view.MainActivity

class ContactRoomController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactRoomDao by lazy {
        Room.databaseBuilder(mainActivity, ContactRoomDaoDatabase::class.java, CONTACT_DATABASE_FILE)
            .build().getContactRoomDao()
    }

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
            getContacts()
        }.start()
    }

    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)

    fun getContacts() {
        Thread {
            val returnList = contactDaoImpl.retrieveContacts()
            val message = Message()
            message.data.putParcelableArray(
                CONTACT_ARRAY,
                returnList.toTypedArray()
            )
            mainActivity.updateContactListHandler.sendMessage(message)
        }.start()
    }

    fun editContact(contact: Contact) {
        Thread {
            contactDaoImpl.updateContact(contact)
            getContacts()
        }.start()
    }
    fun removeContact(contact: Contact) {
        Thread {
            contactDaoImpl.deleteContact(contact)
            getContacts()
        }.start()
    }
}