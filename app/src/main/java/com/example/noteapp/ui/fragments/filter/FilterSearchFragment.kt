package com.example.noteapp.ui.fragments.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentFilterSearchBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterSearchFragment : BottomSheetDialogFragment() {

    //binding
    private lateinit var binding :FragmentFilterSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}