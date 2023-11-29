package br.edu.scl.ifsp.ads.oneMenssageChat.controller

import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactDao
import br.edu.scl.ifsp.ads.oneMenssageChat.model.ContactDaoSqlite
import br.edu.scl.ifsp.ads.oneMenssageChat.view.MainActivity

class ContactController(mainActivity: MainActivity) {
    private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)

    fun insertContact(contact: Contact): Int = contactDaoImpl.createContact(contact)
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() = contactDaoImpl.retrieveContacts()
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(id: Int) = contactDaoImpl.deleteContact(id)
}