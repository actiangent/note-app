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
import com.actiangent.note.util.dp


class HomeNoteListAdapter(
    private val onNoteClick: (Int) -> Unit
) : ListAdapter<Note, HomeNoteListAdapter.NoteListViewHolder>(NoteDiffCallback()) {

    class NoteListViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note, onNoteClick: (Int) -> Unit) {
            binding.apply {
                itemNoteContainer.setOnClickListener { onNoteClick(item.id) }

                noteTitle.text = item.title
                noteContent.text = item.contentText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, onNoteClick)
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

    class HeaderNoteListViewHolder(binding: EmptyHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val layoutParams = StaggeredGridLayoutManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 64.dp
        ).apply { isFullSpan = true }

        companion object {
            fun create(parent: ViewGroup): HeaderNoteListViewHolder {
                val binding =
                    EmptyHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HeaderNoteListViewHolder(binding).apply {
                    itemView.layoutParams = layoutParams
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HeaderNoteListViewHolder = HeaderNoteListViewHolder.create(parent)

    override fun onBindViewHolder(holder: HeaderNoteListViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1
}
