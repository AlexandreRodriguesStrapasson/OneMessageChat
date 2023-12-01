package br.edu.scl.ifsp.ads.oneMenssageChat.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.ads.oneMenssageChat.databinding.ActivityContactBinding
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.EXTRA_CONTACT
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Constant.VIEW_CONTACT
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact
import java.util.Random

class MessageActivity : AppCompatActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Contact details"

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let{_receivedContact ->
            val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
            with(acb) {
                if (viewContact) {
                    nameEt.isEnabled = false
                    emailEt.isEnabled = false
                    saveBt.visibility = View.GONE
                }
                nameEt.setText(_receivedContact.name)
                emailEt.setText(_receivedContact.email)
            }
        }

        with(acb) {
            saveBt.setOnClickListener {
                val contact = Contact(
                    id = receivedContact?.id ?: generateId(),
                    name = nameEt.text.toString(),
                    email = emailEt.text.toString()
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_CONTACT, contact)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun generateId(): Int = Random(System.currentTimeMillis()).nextInt()
}