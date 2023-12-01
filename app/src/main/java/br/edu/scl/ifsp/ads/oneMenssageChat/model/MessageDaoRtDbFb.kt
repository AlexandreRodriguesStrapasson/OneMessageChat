package br.edu.scl.ifsp.ads.oneMenssageChat.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MessageDaoRtDbFb: MessageDao{
    companion object{
        private const val MESSAGE_LIST_ROOT_NODE = "MessageList"
    }

    private val contactRtDbFbReference = Firebase.database.getReference(MESSAGE_LIST_ROOT_NODE)

    //Simula uma consulta ao Realtime Database
    private val MessageList: MutableList <Contact> = mutableListOf()

    init{
        contactRtDbFbReference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()

                contact?.also{newContact ->
                    if(!MessageList.any{it.id == newContact.id}) {
                        MessageList.add(newContact)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val contact: Contact? = snapshot.getValue<Contact>()

                contact?.also{editContact ->
                    MessageList.apply {
                        this[indexOfFirst { editContact.id == it.id }] = editContact
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact: Contact? = snapshot.getValue<Contact>()

                contact?.also{
                    MessageList.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        contactRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactMap = snapshot.getValue<Map<String, Contact>>()

                MessageList.clear()
                contactMap?.values?.also {
                    MessageList.addAll(it)
                }


            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun createContact(contact: Contact): Int {
        createOrUpdateContact(contact)
        return 1
    }

    override fun retrieveContact(id: Int): Contact? {
        return MessageList[MessageList.indexOfFirst { it.id == id }]
    }

    override fun retrieveContacts(): MutableList<Contact> = MessageList

    override fun updateContact(contact: Contact): Int {
        createOrUpdateContact(contact)
        return 1
    }

    override fun deleteContact(id: Int): Int {
        contactRtDbFbReference.child(id.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateContact(contact: Contact) = contactRtDbFbReference.child(contact.id.toString()).setValue(contact)
}
