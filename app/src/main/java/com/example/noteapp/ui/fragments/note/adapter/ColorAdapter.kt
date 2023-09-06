package com.example.noteapp.ui.fragments.note.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemColorBinding
import com.example.noteapp.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ColorAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    //binding
    private lateinit var binding: ItemColorBinding

    inner class ColorViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: Utils.Color) {
            binding.apply {
                root.setBackgroundColor(Utils().setBackgroundColor(item, context))
                // send color note fragment
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bindViews(differ.currentList[position])
    }

    //on item click listener
    private var onItemClickListener :((color :Utils.Color) -> Unit)? = null
    fun itemClickListener( listener: (color :Utils.Color) -> Unit ){
        onItemClickListener = listener
    }

    //color differ callback
    private val differCallback = object : DiffUtil.ItemCallback<Utils.Color>() {
        override fun areItemsTheSame(oldItem: Utils.Color, newItem: Utils.Color): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Utils.Color, newItem: Utils.Color): Boolean {
            return oldItem == newItem
        }

    }

    //differ
    val differ = AsyncListDiffer(this, differCallback)
}