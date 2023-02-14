package com.actiangent.note.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.actiangent.note.data.model.Note
import com.actiangent.note.databinding.EmptyHeaderBinding
import com.actiangent.note.databinding.ItemNoteBinding


class HomeNoteListAdapter :
    ListAdapter<Note, HomeNoteListAdapter.NoteListViewHolder>(NoteDiffCallback()) {

    inner class NoteListViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.apply {
                noteTitle.text = item.title
                noteContent.text = item.contentText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return this.NoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class HeaderNoteListAdapter :
    RecyclerView.Adapter<HeaderNoteListAdapter.HeaderNoteListViewHolder>() {

    inner class HeaderNoteListViewHolder(private val binding: EmptyHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeaderNoteListAdapter.HeaderNoteListViewHolder {
        val binding = EmptyHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = StaggeredGridLayoutManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            128 // TODO - to dp
        ).apply { isFullSpan = true }
        return HeaderNoteListViewHolder(binding).apply { itemView.layoutParams = layoutParams }
    }

    override fun onBindViewHolder(holder: HeaderNoteListViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1
}
