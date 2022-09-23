package com.isaacmanu.notesapp

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isaacmanu.notesapp.databinding.FragmentNoteBinding
import com.isaacmanu.notesapp.model.Note
import com.isaacmanu.notesapp.viewmodel.NoteViewModel
import com.isaacmanu.notesapp.viewmodel.NoteViewModelFactory

class NoteFragment : Fragment() {

    private val navigationsArgs: NoteFragmentArgs by navArgs()
    lateinit var note: Note

    //Creates a viewmodel instance using viewmodelfactory
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database
                .noteDao()
        )
    }

    //Binding object for fragment_note layout file
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        return binding.root
    }



    private fun bind(note: Note) {
        binding.apply {
            title.setText(note.title)
            content.setText(note.noteContent)
        }
    }

    private fun titleOrContentBlank(): Boolean {
        return viewModel.titleOrContentBlank(
            binding.title.text.toString(),
            binding.content.text.toString()
        )
    }

    private fun deleteNote() {
        viewModel.deleteNote(note)
        this.findNavController().navigateUp()
    }


    /*
    Using the binding variable saves the text from the title
    and content fields to database as long there is a valid entry in either field.
    Also calls getDateTime from viewmodel and saves the response
    */
    private fun saveNote() {
        if (titleOrContentBlank()) {
            viewModel.addNewNote(
                binding.title.text.toString(),
                binding.content.text.toString(),
                viewModel.getDateTime()
            )
            val action = NoteFragmentDirections.actionNoteFragmentToNotesListFragment()
            findNavController().navigate(action)
        }

    }

    private fun updateNote() {

        if (titleOrContentBlank()) {
            viewModel.updateNote(
                this.navigationsArgs.itemId,
                this.binding.title.text.toString(),
                this.binding.content.text.toString(),
                viewModel.getDateTime()
            )
        }
        val action = NoteFragmentDirections.actionNoteFragmentToNotesListFragment()
        this.findNavController().navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationsArgs.itemId


        viewModel.retrieveNote(id).observe(this.viewLifecycleOwner) { newNote ->
            note = newNote
            bind(note)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.top_app_bar_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.save_button -> {
                        updateNote()
                        true
                    }
                    R.id.delete_button -> {
                        deleteNote()
                        true
                    }
                    else -> false
                }
                // Handle the menu selection
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        /*
        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)


        binding.topAppBar.setNavigationOnClickListener { view ->
            this.findNavController().navigateUp()
        }

         */




        binding.fab.setOnClickListener{
            saveNote()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}