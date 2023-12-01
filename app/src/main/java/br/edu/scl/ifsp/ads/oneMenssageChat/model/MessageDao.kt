package br.edu.scl.ifsp.ads.oneMenssageChat.model

interface MessageDao {
    fun createMsg(contact: Contact): Int
    fun retrieveMsg(id: Int): Contact?
    fun retrieveMsg(): MutableList<Contact>
    fun updateMsg(contact: Contact): Int
    fun deleteMsg(id: Int): Int
}