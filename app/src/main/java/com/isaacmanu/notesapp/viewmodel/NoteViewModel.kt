package com.isaacmanu.notesapp.viewmodel

import androidx.lifecycle.*
import com.isaacmanu.notesapp.database.NoteDao
import com.isaacmanu.notesapp.model.Note
import kotlinx.coroutines.launch
import java.text.DateFormat

class NoteViewModel(private val noteDao: NoteDao): ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    private fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    private fun getNewNote(title: String, content: String, date: String): Note {
        return Note(
            title = title,
            noteContent = content,
            dateLastEdited = date
        )
    }

    fun addNewNote(title: String, content: String, date: String) {
        val newNote = getNewNote(title, content, date)
        insertNote(newNote)
    }

    fun titleOrContentBlank(title: String, content: String): Boolean {
        if (title.isBlank() && content.isBlank()) {
            return false
        }
        return true

    }

    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    fun searchNotes(string: String) : LiveData<List<Note>> {
        return noteDao.searchDatabase(string).asLiveData()
    }

    fun updateNote(
        id: Int,
        title: String,
        content: String,
        dateLastEdited: String
    ) {
        val updatedNote = Note(id, title, content, dateLastEdited)
        updateNote(updatedNote)

    }

    private fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    //Function takes UNIX timestamp and returns formatted date
    //Perhaps could be extension of Note class
    fun getDateTime(): String {
        val dateTime = System.currentTimeMillis()
        return DateFormat.getDateTimeInstance().format(dateTime)
    }


}

class NoteViewModelFactory(private val noteDao: NoteDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}
