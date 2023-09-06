package com.example.noteapp.utils

import androidx.fragment.app.Fragment
import com.example.noteapp.ui.fragments.home.HomeContract
import com.example.noteapp.ui.fragments.note.NoteContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object ViewContractModuleFragment {

    @Provides
    @FragmentScoped
    fun provideHomeContractView(fragment: Fragment): HomeContract.View = fragment as HomeContract.View

    @Provides
    @FragmentScoped
    fun provideNoteContractView( fragment: Fragment ) :NoteContract.View = fragment as NoteContract.View

}