package com.nabeel130.shorts

import androidx.lifecycle.LiveData
import com.nabeel130.shorts.DBHandler.NoteDao
import com.nabeel130.shorts.Entity.Note
import com.nabeel130.shorts.Entity.PrivateNote

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.fetchAllNotes()
    val allPrivateNote: LiveData<List<PrivateNote>> = noteDao.fetchAllPrivateNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

    fun searchNote(search: String): LiveData<Note> {
        return noteDao.searchNote(search)
    }

    suspend fun pInsert(pNote: PrivateNote){
        noteDao.insert(pNote)
    }

    suspend fun pDelete(pNote: PrivateNote){
        noteDao.delete(pNote)
    }

    suspend fun pUpdate(pNote: PrivateNote){
        noteDao.updateNote(pNote)
    }
}