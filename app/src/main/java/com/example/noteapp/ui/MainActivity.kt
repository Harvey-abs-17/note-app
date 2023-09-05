package com.example.noteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.utils.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView( binding.root )

        //nav controller
        val navController = findNavController(R.id.fragmentContainerView)

        binding.apply {
            bottomNav.background = null
            bottomNav.setupWithNavController(navController)
            // manage visibility of bottom app bar and fab
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.noteFragment -> {
                        bottomAppBar.showView(false)
                        addNoteBtn.showView(false)
                    }
                    R.id.homeFragment ->{
                        bottomAppBar.showView(true)
                        addNoteBtn.showView(true)
                    }
                }
            }
            //navigate to note fragment
            addNoteBtn.setOnClickListener {
                navController.navigate(R.id.noteFragment)
            }
        }
    }
}