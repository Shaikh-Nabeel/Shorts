package com.nabeel130.shorts.DBHandler

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nabeel130.shorts.Entity.Note
import com.nabeel130.shorts.Entity.PrivateNote

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun fetchAllNotes(): LiveData<List<Note>>

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM notes_table WHERE text LIKE '%' || :search ||'%' LIMIT 1")
    fun searchNote(search: String): LiveData<Note>

    //private note table functions

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pNote: PrivateNote)

    @Delete
    suspend fun delete(pNote: PrivateNote)

    @Query("SELECT * FROM private_note_table ORDER BY pId ASC")
    fun fetchAllPrivateNotes(): LiveData<List<PrivateNote>>


    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(pNote: PrivateNote)
}