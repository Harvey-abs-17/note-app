package com.example.noteapp.ui.fragments.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.FragmentSearchBinding
import com.example.noteapp.ui.fragments.filter.FilterSearchFragment
import com.example.noteapp.ui.fragments.home.adapter.NoteAdapter
import com.example.noteapp.utils.initRec
import com.example.noteapp.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchContract.View {

    //binding
    private lateinit var binding: FragmentSearchBinding

    //others
    @Inject
    lateinit var searchAdapter: NoteAdapter

    @Inject
    lateinit var presenter: SearchPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        //create toolbar menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                //clear menu because of duplicating options
                menu.clear()
                menuInflater.inflate(R.menu.search_menu, menu)
                //access to search view
                val search = menu.findItem(R.id.search_item)
                val searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            if (it.isEmpty()) {
                                showEmptyHintLayout(true)
                            } else {
                                presenter.getSearchData(it)
                            }
                        }
                        return true
                    }
                })
            }
            //manage menu item selection
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when( menuItem.itemId ){
                    R.id.filter_item ->{
                        showFilterFragment()
                    }
                }
                return true
            }

        })
        return binding.root
    }

    @SuppressLint("UseSupportActionBar")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //set toolbar as fragment action bar
            (activity as AppCompatActivity).setSupportActionBar(searchToolBar)
            //navigation
            searchToolBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun showEmptyLayout(isShown: Boolean) {
        binding.apply {
            searchEmptyLayout.showView(isShown)
            searchHintLayout.showView(!isShown)
            searchItemRec.showView(!isShown)
        }
    }

    override fun showEmptyHintLayout(isShown: Boolean) {
        binding.apply {
            searchHintLayout.showView(isShown)
            searchEmptyLayout.showView(!isShown)
            searchItemRec.showView(!isShown)
        }
    }

    override fun loadSearchRecData(notes: List<NoteEntity>) {
        binding.apply {
            searchAdapter.setData(notes)
            searchItemRec.initRec(
                searchAdapter,
                StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            )
            searchItemRec.showView(true)
            searchHintLayout.showView(false)
        }
    }

    override fun showFilterFragment() {
        binding.searchToolBar.menu.findItem(R.id.filter_item).setOnMenuItemClickListener {
            FilterSearchFragment().show(parentFragmentManager, FilterSearchFragment().tag)
            return@setOnMenuItemClickListener true
        }
    }

    override fun showLoading(isShown: Boolean) {
        binding.apply {
            searchLoading.showView(isShown)
            searchEmptyLayout.showView(!isShown)
            searchHintLayout.showView(!isShown)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }


}