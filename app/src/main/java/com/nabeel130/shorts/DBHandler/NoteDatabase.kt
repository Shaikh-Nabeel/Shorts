package com.nabeel130.shorts.DBHandler

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nabeel130.shorts.Entity.Note
import com.nabeel130.shorts.Entity.PrivateNote


@Database(entities = [Note::class, PrivateNote::class], version = 1,exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NoteDao

    companion object{
        private var INSTANCE : NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}