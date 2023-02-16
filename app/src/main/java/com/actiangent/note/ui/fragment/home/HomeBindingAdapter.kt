package com.actiangent.note.ui.fragment.home

import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter

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
fun onClickClearQuery(imageView: ImageView, viewModel: HomeViewModel) =
    imageView.setOnClickListener {
        viewModel.clearQuery()
    }

