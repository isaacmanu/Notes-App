package com.isaacmanu.notesapp

import android.app.Application
import com.isaacmanu.notesapp.database.NoteRoomDatabase

class NoteApplication: Application() {

    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }
}