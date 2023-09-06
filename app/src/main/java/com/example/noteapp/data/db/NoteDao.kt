package com.example.noteapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteapp.data.model.NoteEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Observable<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE isPinned == :isPinned")
    fun getNotesByPinnedValue( isPinned :Boolean ) :Observable<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewNote(noteEntity: NoteEntity): Completable

}