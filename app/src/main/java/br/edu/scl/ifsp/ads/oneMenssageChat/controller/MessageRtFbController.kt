package br.edu.scl.ifsp.ads.oneMenssageChat.controller

import android.os.Message
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact
import br.edu.scl.ifsp.ads.oneMenssageChat.model.MessageDaoRtDbFb
import br.edu.scl.ifsp.ads.oneMenssageChat.view.MainActivity

class MessageRtFbController(private val mainActivity: MainActivity) {
    private val messageDaoImpl: MessageDaoRtDbFb = MessageDaoRtDbFb()

    fun insertContact(contact: Contact) {
        Thread {
            messageDaoImpl.createMsg(contact)
        }.start()
    }

    fun getContact(id: Int) = messageDaoImpl.retrieveMsg(id)

    fun getContacts() {
        Thread {
            val returnList = messageDaoImpl.retrieveMsg()
            val message = Message()
            message.data.putParcelableArray(
                Constant.MSG_ARRAY,
                returnList.toTypedArray()
            )
            mainActivity.updateContactListHandler.sendMessage(message)
        }.start()
    }

    fun editContact(contact: Contact) {
        Thread {
            messageDaoImpl.updateMsg(contact)
        }.start()
    }
    fun removeContact(contact: Contact) {
        Thread {
            contact.id?.also {
                messageDaoImpl.deleteMsg(it)
            }
        }.start()
    }
}