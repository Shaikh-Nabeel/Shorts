package com.nabeel130.shorts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nabeel130.shorts.DBHandler.NoteDatabase
import com.nabeel130.shorts.Entity.Note
import com.nabeel130.shorts.Entity.PrivateNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val allNotes: LiveData<List<Note>>
    val allPrivateNote: LiveData<List<PrivateNote>>

    private val repository: NoteRepository
    init{
        val dao = NoteDatabase.getInstance(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
        allPrivateNote = repository.allPrivateNote
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.updateNote(note)
    }

    fun searchNote(search: String): LiveData<Note> {
        return repository.searchNote(search)
    }

    fun insertPrivateNote(pNote: PrivateNote) = viewModelScope.launch(Dispatchers.IO){
        repository.pInsert(pNote)
    }

    fun deletePrivateNote(pNote: PrivateNote) = viewModelScope.launch(Dispatchers.IO) {
        repository.pDelete(pNote)
    }

    fun updatePrivateNote(pNote: PrivateNote) = viewModelScope.launch(Dispatchers.IO) {
        repository.pUpdate(pNote)
    }
}