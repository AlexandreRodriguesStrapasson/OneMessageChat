package br.edu.scl.ifsp.ads.oneMenssageChat.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.oneMenssageChat.R
import br.edu.scl.ifsp.ads.oneMenssageChat.databinding.TileContactBinding
import br.edu.scl.ifsp.ads.oneMenssageChat.model.Contact

class MessageAdapter(context: Context, private val contactList: MutableList<Contact>):
    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        var tcb: TileContactBinding? = null

        var contactTileView = convertView
        if(contactTileView == null) {
            tcb = TileContactBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            contactTileView = tcb.root

            val tileContactHolder = TileContactHolder(tcb.topicTv, tcb.messageTv)
            contactTileView.tag = tileContactHolder
        }
        val holder = contactTileView.tag as TileContactHolder
        holder.nameTv.setText(contact.name)
        holder.emailTv.setText(contact.email)

        tcb?.topicTv?.text = contact.name
        tcb?.messageTv?.text = contact.email

        return contactTileView
    }

    private data class TileContactHolder(val nameTv: TextView, val emailTv: TextView)
}