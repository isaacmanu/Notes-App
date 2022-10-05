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

    /*
    Creates a viewmodel instance using viewmodelfactory
    A viewmodel factory is required as we are passing a constructor argument into the viewmodel
    Namely the NoteDao.
    */
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database
                .noteDao()
        )
    }

    /*
    Binding object for fragment_note layout file
    This setup is to prevent the binding variable persisting in other fragments
    */
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



    //Function to bind the Note data taken from the database to the UI
    private fun bind(note: Note) {
        binding.apply {
            title.setText(note.title)
            content.setText(note.noteContent)
        }
    }

    /*
   Function to determine whether the user has input text to the title
   or contents EditText's
   */
    private fun titleOrContentBlank(): Boolean {
        return viewModel.titleOrContentBlank(
            binding.title.text.toString(),
            binding.content.text.toString()
        )
    }

    //Function to delete the note currently open from the database
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

    //Updates note entry in database using data input by user
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


        //Accesses the viewmodel to retrieve the note using the id passed from NotesListFragment
        viewModel.retrieveNote(id).observe(this.viewLifecycleOwner) { newNote ->
            note = newNote
            bind(note)
        }

        /*
        Setup menu items using MenuHost API
        This implementation is lifecycle aware so the menu items only persist
        when the fragment state is RESUMED or higher.
        */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

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
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fab.setOnClickListener{
            saveNote()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}