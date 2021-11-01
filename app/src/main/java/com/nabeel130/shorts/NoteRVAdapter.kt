package com.nabeel130.shorts

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nabeel130.shorts.Entity.Note

class NoteRVAdapter(private val listener: INotesAdapter): RecyclerView.Adapter<ViewHolderOfNote>() {
    private val allNotes = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfNote {
        val holder = ViewHolderOfNote(LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false))
        holder.deleteBtn.setOnClickListener {
            listener.onDeleteItemClicked(allNotes[holder.adapterPosition])
        }
        holder.noteText.setOnClickListener{
            listener.onTextItemClicked(allNotes[holder.adapterPosition])
        }
        holder.noteText.movementMethod = ScrollingMovementMethod.getInstance()
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolderOfNote, position: Int) {
        val currNote = allNotes[position]
        holder.noteText.text = currNote.text
    }

    override fun getItemCount(): Int = allNotes.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(notes: ArrayList<Note>){
        allNotes.clear()
        allNotes.addAll(notes)
        notifyDataSetChanged()
    }
}

class ViewHolderOfNote(item: View): RecyclerView.ViewHolder(item){
    val noteText: TextView = item.findViewById(R.id.textViewOfItem)
    val deleteBtn: ImageView = item.findViewById(R.id.deleteButtonOfItem)
}

interface INotesAdapter{
    fun onDeleteItemClicked(note: Note)
    fun onTextItemClicked(note: Note)
}