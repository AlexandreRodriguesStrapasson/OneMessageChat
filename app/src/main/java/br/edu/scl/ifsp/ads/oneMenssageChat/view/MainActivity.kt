package br.edu.scl.ifsp.ads.oneMenssageChat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.oneMenssageChat.R
import br.edu.scl.ifsp.ads.oneMenssageChat.adapter.MessageAdapter
import br.edu.scl.ifsp.ads.oneMenssageChat.controller.MessageRtFbController
import br.edu.scl.ifsp.ads.oneMenssageChat.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.MSG_ARRAY
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.EXTRA_MSG
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.VIEW_MSG
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Controller
    private val contactController: MessageRtFbController by lazy {
        MessageRtFbController(this)
    }

    // Adapter
    private val messageAdapter: MessageAdapter by lazy {
        MessageAdapter(
            this,
            contactList)
    }

    companion object {
        const val GET_CONTACTS_MSG = 1
        const val GET_CONTACTS_INTERVAL = 2000L
    }

    // Handler
    val updateContactListHandler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == GET_CONTACTS_MSG){
                contactController.getContacts()
                sendMessageDelayed(obtainMessage().apply { what = GET_CONTACTS_MSG },
                    GET_CONTACTS_INTERVAL
                )
            }else{
                msg.data.getParcelableArray(MSG_ARRAY)?.also { contactArray ->
                    contactList.clear()
                    contactArray.forEach {
                        contactList.add(it as Contact)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.toolbarIn.toolbar)
        amb.messageLv.adapter = messageAdapter
        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_MSG)
                contact?.let{_contact ->
                    if (contactList.any { it.id == _contact.id }) {
                        contactController.editContact(_contact)
                    } else {
                        contactController.insertContact(_contact)
                    }
                }
            }
        }

        amb.messageLv.setOnItemClickListener { parent, view, position, id ->
            val contact = contactList[position]
            val viewContactIntent = Intent(this, MessageActivity::class.java)
            viewContactIntent.putExtra(EXTRA_MSG, contact)
            viewContactIntent.putExtra(VIEW_MSG, true)
            startActivity(viewContactIntent)
        }

        registerForContextMenu(amb.messageLv)
        updateContactListHandler.apply {
            sendMessageDelayed(obtainMessage().apply { what = GET_CONTACTS_MSG },
                GET_CONTACTS_INTERVAL
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, MessageActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val contact = contactList[position]
        return when (item.itemId) {
            R.id.removeContactMi -> {
                contactController.removeContact(contact)
                Toast.makeText(this, "Contact removed.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editContactMi -> {
                val editContactIntent = Intent(this, MessageActivity::class.java)
                editContactIntent.putExtra(EXTRA_MSG, contact)
                carl.launch(editContactIntent)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.messageLv)
    }
}