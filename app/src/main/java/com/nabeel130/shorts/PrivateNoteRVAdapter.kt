package com.nabeel130.shorts

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nabeel130.shorts.Entity.PrivateNote

class PrivateNoteRVAdapter(private val listener: IPrivateNotesAdapter): RecyclerView.Adapter<ViewHolderOfPrivateNote>() {
    private val allPrivateNotes = ArrayList<PrivateNote>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfPrivateNote {
        val holder = ViewHolderOfPrivateNote(LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false))
        holder.deleteBtn.setOnClickListener {
            listener.onDeleteItemClicked(allPrivateNotes[holder.adapterPosition])
        }
        holder.noteText.setOnClickListener{
            listener.onTextItemClicked(allPrivateNotes[holder.adapterPosition])
        }
        holder.noteText.movementMethod = ScrollingMovementMethod.getInstance()
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolderOfPrivateNote, position: Int) {
        val currNote = allPrivateNotes[position]
        holder.noteText.text = currNote.pText
    }

    override fun getItemCount(): Int = allPrivateNotes.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(notes: ArrayList<PrivateNote>){
        allPrivateNotes.clear()
        allPrivateNotes.addAll(notes)
        notifyDataSetChanged()
    }
}

class ViewHolderOfPrivateNote(item: View): RecyclerView.ViewHolder(item){
    val noteText: TextView = item.findViewById(R.id.textViewOfItem)
    val deleteBtn: ImageView = item.findViewById(R.id.deleteButtonOfItem)
}

interface IPrivateNotesAdapter{
    fun onDeleteItemClicked(note: PrivateNote)
    fun onTextItemClicked(note: PrivateNote)
}