package com.example.noteapp.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.FragmentHomeBinding
import com.example.noteapp.ui.fragments.home.adapter.NoteAdapter
import com.example.noteapp.utils.initRec
import com.example.noteapp.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeContract.View {

    //binding
    private lateinit var binding: FragmentHomeBinding

    //others
    @Inject
    lateinit var noteAdapter: NoteAdapter

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
    }

    //init pinned rec data
    override fun loadPinnedNotes(notes: List<NoteEntity>) {
        noteAdapter.setData(notes)
        binding.pinRec.initRec(
            noteAdapter,
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        )
    }

    //init upcoming rec data
    override fun loadUpcomingNotes(notes: List<NoteEntity>) {
        noteAdapter.setData(notes)
        binding.upcomingRec.initRec(
            noteAdapter,
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

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}