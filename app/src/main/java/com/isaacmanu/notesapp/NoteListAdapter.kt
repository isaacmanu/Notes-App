package com.isaacmanu.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isaacmanu.notesapp.databinding.NoteListItemBinding
import com.isaacmanu.notesapp.model.Note

//List adapter with item click listener
class NoteListAdapter(private val onNoteClicked: (Note) -> Unit): ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback) {


    class NoteViewHolder(private var binding: NoteListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.title.text = note.title
            binding.content.text = note.noteContent

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(NoteListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteItem = getItem(position)
        holder.itemView.setOnClickListener {
            onNoteClicked(noteItem)
        }
        holder.bind(noteItem)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteContent == newItem.noteContent
        }

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

}