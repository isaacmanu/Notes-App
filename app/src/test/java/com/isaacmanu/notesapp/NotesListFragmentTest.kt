package com.isaacmanu.notesapp

import android.content.Context
import com.isaacmanu.notesapp.model.Note
import org.junit.Test
import org.mockito.Mockito.mock

class NotesListFragmentTest {

    private val context = mock(Context::class.java)

    @Test
    fun test_adapter_size() {
        val data = listOf(
            Note(0, "Title One", "Content", "1"),
            Note(1, "Title Two", "Content", "2")
        )

        //Unsure how to construct recyclerview adapter
    }
}