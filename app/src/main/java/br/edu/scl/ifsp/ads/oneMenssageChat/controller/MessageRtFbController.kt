package br.edu.scl.ifsp.ads.oneMenssageChat.controller

import android.os.Message
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactDaoRtDbFb
import br.edu.scl.ifsp.ads.oneMenssageChat.view.MainActivity

class MessageRtFbController(private val mainActivity: MainActivity) {
    private val messageDaoImpl: ContactDaoRtDbFb = ContactDaoRtDbFb()

    fun insertContact(contact: Contact) {
        Thread {
            messageDaoImpl.createContact(contact)
        }.start()
    }

    fun getContact(id: Int) = messageDaoImpl.retrieveContact(id)

    fun getContacts() {
        Thread {
            val returnList = messageDaoImpl.retrieveContacts()
            val message = Message()
            message.data.putParcelableArray(
                Constant.CONTACT_ARRAY,
                returnList.toTypedArray()
            )
            mainActivity.updateContactListHandler.sendMessage(message)
        }.start()
    }

    fun editContact(contact: Contact) {
        Thread {
            messageDaoImpl.updateContact(contact)
        }.start()
    }
    fun removeContact(contact: Contact) {
        Thread {
            contact.id?.also {
                messageDaoImpl.deleteContact(it)
            }
        }.start()
    }
}