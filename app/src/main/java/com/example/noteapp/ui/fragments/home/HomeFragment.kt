package com.example.noteapp.ui.fragments.home

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.ui.fragments.home.adapter.NoteAdapter
import com.example.noteapp.utils.NoteQueryType
import com.example.noteapp.utils.initRec
import com.example.noteapp.utils.showView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeContract.View {

    //binding
    private lateinit var binding: FragmentHomeBinding

    //others
    @Inject
    lateinit var upcomingAdapter: NoteAdapter

    @Inject
    lateinit var pinAdapter: NoteAdapter

    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getAllNotePresenter()
        handleAdapterClickListener(pinAdapter)
        handleAdapterClickListener(upcomingAdapter)
    }

    private fun handleAdapterClickListener( adapter :NoteAdapter ){
        adapter.itemClickListener { noteId, queryType ->
            when(queryType){

                NoteQueryType.UPDATE ->{
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoteFragment(noteId))
                }

                NoteQueryType.DELETE ->{
                    presenter.deleteNotePresenter(noteId)
                }
                else -> {}
            }
        }
    }


    //init pinned rec data
    override fun loadPinnedNotes(notes: List<NoteEntity>) {
        pinAdapter.setData(notes)
        binding.pinRec.initRec(
            pinAdapter,
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        )
    }

    //init upcoming rec data
    override fun loadUpcomingNotes(notes: List<NoteEntity>) {
        upcomingAdapter.setData(notes)
        binding.upcomingRec.initRec(
            upcomingAdapter,
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        )
    }

    override fun showLoading(isShown: Boolean) {
        binding.apply {
            homeProgressBar.showView(isShown)
        }
    }

    override fun showEmpty(isShown: Boolean) {
        binding.apply {
            emptyNoteLayout.showView(isShown)
            contentLayout.showView(!isShown)
        }
    }

    override fun visiblePinnedView(isShown: Boolean) {
        binding.apply {
            pinLayout.showView(isShown)
        }
    }

    override fun visibleUpcomingView(isShown: Boolean) {
        binding.apply {
            upcomingLayout.showView(isShown)
        }
    }

    override fun showDeleteMessage() {
        Toast.makeText(requireContext(), "salam", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}