package com.isaacmanu.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isaacmanu.notesapp.model.Note

//This implementation is adapted from 'Introduction to Room and Flow' codelab hosted on [developer.android.com]

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null


        //Retrieving the database is done within a synchronized block
        //to prevent multiple instances of the database being created
        fun getDatabase(context: Context): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}