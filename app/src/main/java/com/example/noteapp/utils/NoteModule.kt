package com.example.noteapp.utils

import android.content.Context
import androidx.room.Room
import com.example.noteapp.data.db.NoteDatabase
import com.example.noteapp.data.model.NoteEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context :Context) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun provideDao(db :NoteDatabase) = db.noteDao()
    
    @Provides
    fun provideNoteEntity() = NoteEntity()

}