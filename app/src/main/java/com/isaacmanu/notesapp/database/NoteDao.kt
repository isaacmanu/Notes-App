package com.isaacmanu.notesapp.database

import androidx.room.*
import com.isaacmanu.notesapp.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from note where id = :id")
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * from note ORDER BY id ASC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from note where content LIKE :string")
    fun searchDatabase(string: String): Flow<List<Note>>



}