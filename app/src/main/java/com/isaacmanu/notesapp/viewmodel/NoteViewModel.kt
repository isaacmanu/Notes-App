package com.isaacmanu.notesapp.viewmodel

import androidx.lifecycle.*
import com.isaacmanu.notesapp.database.NoteDao
import com.isaacmanu.notesapp.model.Note
import kotlinx.coroutines.launch
import java.text.DateFormat

class NoteViewModel(private val noteDao: NoteDao): ViewModel() {

    // LiveData instance to observe a list of all notes in the database
    // Uses .asLiveData() as the source is a flow
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    // Calls insert() function from NoteDao within a coroutine
    private fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    // Creates a Note() object with the passed parameters
    private fun getNewNote(title: String, content: String, date: String): Note {
        return Note(
            title = title,
            noteContent = content,
            dateLastEdited = date
        )
    }

    // To be called by the UI when the user wants to save a note
    // The data entered by the user will be supplied as the parameters
    fun addNewNote(title: String, content: String, date: String) {
        val newNote = getNewNote(title, content, date)
        insertNote(newNote)
    }

    // True if either title or content is a valid string (i.e. not empty or just whitespace)
    fun titleOrContentBlank(title: String, content: String): Boolean {
        if (title.isBlank() && content.isBlank()) {
            return false
        }
        return true

    }

    // Calls getNote() from NoteDao
    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    // Calls delete() from NoteDao
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    fun searchNotes(string: String) : LiveData<List<Note>> {
        return noteDao.searchDatabase(string).asLiveData()
    }

    // Called by the UI to update a note using data entered by the user
    fun updateNote(
        id: Int,
        title: String,
        content: String,
        dateLastEdited: String
    ) {
        val updatedNote = Note(id, title, content, dateLastEdited)
        updateNote(updatedNote)

    }

    // Calls update() from the NoteDao()
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

// Required as a constructor argument is passed into the viewModel
class NoteViewModelFactory(private val noteDao: NoteDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}
