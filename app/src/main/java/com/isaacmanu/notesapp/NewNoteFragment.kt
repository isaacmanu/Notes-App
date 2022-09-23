package com.isaacmanu.notesapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.isaacmanu.notesapp.databinding.FragmentNewNoteBinding
import com.isaacmanu.notesapp.viewmodel.NoteViewModel
import com.isaacmanu.notesapp.viewmodel.NoteViewModelFactory


class NewNoteFragment : Fragment() {

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database
                .noteDao()
        )
    }

    //Binding object for fragment_note layout file
    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here

                menuInflater.inflate(R.menu.top_app_bar_new_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.save_button -> {
                        saveNote()
                        true
                    } else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        /*
        binding.topAppBar.setNavigationOnClickListener { view ->
            this.findNavController().navigateUp()
        }


        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_button -> {
                    saveNote()
                    true
                } else -> false
            }
        }

         */



        binding.fab.setOnClickListener{
            saveNote()
        }
    }

    private fun titleOrContentBlank(): Boolean {
        return viewModel.titleOrContentBlank(
            binding.title.text.toString(),
            binding.content.text.toString()
        )
    }

    /*
    Using the binding variable saves the text from the title
    and content fields to database as long there is a valid entry in either field.
    Also calls getDateTime from viewmodel and saves the response
    */
    // The fragment needs to get removed from the backstack
    private fun saveNote() {
        if (titleOrContentBlank()) {
            viewModel.addNewNote(
                binding.title.text.toString(),
                binding.content.text.toString(),
                viewModel.getDateTime()
            )
            val action = NewNoteFragmentDirections.actionNewNoteFragmentToNotesListFragment()
            findNavController().navigate(action)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}