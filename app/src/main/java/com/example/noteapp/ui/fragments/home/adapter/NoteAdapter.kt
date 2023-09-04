package com.example.noteapp.ui.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NoteAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    //binding
    private lateinit var binding: ItemNoteBinding

    //others
    private var noteList = emptyList<NoteEntity>()

    inner class NoteViewHolder : RecyclerView.ViewHolder(binding.root) {

        //bind items view
        fun bindViews(noteItem: NoteEntity) {
            binding.apply {
                noteTitle.text = noteItem.title
                noteDescription.text = noteItem.description
                noteDateTxt.text = "$${noteItem.create_date}, ${noteItem.create_time}"
                // manage note color background
                root.setBackgroundColor(
                    Utils().setBackgroundColor(
                        noteItem.color,
                        context = context
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindViews(noteList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //set rec data
    fun setData(newNoteList: List<NoteEntity>) {
        val differ = DifferCallback(noteList, newNoteList)
        val differCallback = DiffUtil.calculateDiff(differ)
        noteList = newNoteList
        differCallback.dispatchUpdatesTo(this)
    }

    //diffutils
    class DifferCallback(
        private val oldList: List<NoteEntity>,
        private val newList: List<NoteEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }

    }
}