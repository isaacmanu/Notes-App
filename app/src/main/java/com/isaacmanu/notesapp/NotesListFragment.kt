package com.isaacmanu.notesapp

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.isaacmanu.notesapp.databinding.FragmentNotesListBinding
import com.isaacmanu.notesapp.viewmodel.NoteViewModel
import com.isaacmanu.notesapp.viewmodel.NoteViewModelFactory

class NotesListFragment : Fragment() {

    /*
    Binding object for fragment_note layout file
    This setup is to prevent the binding variable persisting in other fragments
    */
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    /*
    Creates a viewmodel instance using viewmodelfactory
    A viewmodel factory is required as we are passing a constructor argument into the viewmodel
    Namely the NoteDao.
    */
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Setup ListAdapter
        val adapter = NoteListAdapter {
            val action = NotesListFragmentDirections.actionNotesListFragmentToNoteFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        /*
        Access viewmodel to retrieve notes from database for ListAdapter
        Attach observer so any changes are reflected in UI
        */
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it)
            }
        }

        /*
        Setup menu items using MenuHost API
        This implementation is lifecycle aware so the menu items only persist
        when the fragment state is RESUMED or higher.
        */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.top_app_bar_note_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)




        binding.fab.setOnClickListener {
            val action = NotesListFragmentDirections.actionNotesListFragmentToNewNoteFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}