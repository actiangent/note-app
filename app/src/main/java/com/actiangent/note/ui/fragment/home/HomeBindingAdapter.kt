package com.actiangent.note.ui.fragment.home

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.actiangent.note.data.model.Note

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: List<Note>?) {
    val homeNoteListAdapter =
        ((recyclerView.adapter as ConcatAdapter).adapters[1] as HomeNoteListAdapter)
    items?.let {
        homeNoteListAdapter.submitList(items)
    }
}

@BindingAdapter("app:setOnQueryChanged")
fun setOnQueryChanged(editText: EditText, viewModel: HomeViewModel) =
    editText.doOnTextChanged { text, _, _, _ ->
        viewModel.setSearchQuery(text.toString())
    }

@BindingAdapter("app:query")
fun setQueryText(editText: EditText, query: String) = editText.apply {
    setText(query)
    setSelection(query.length)
}

@BindingAdapter("app:clearQuery")
fun oClickClearQuery(imageView: ImageView, viewModel: HomeViewModel) =
    imageView.setOnClickListener {
        viewModel.clearQuery()
    }

