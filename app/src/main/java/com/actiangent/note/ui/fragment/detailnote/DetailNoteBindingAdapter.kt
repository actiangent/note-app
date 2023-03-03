package com.actiangent.note.ui.fragment.detailnote

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setTitleChanged")
fun setTitleChanged(editText: EditText, viewModel: DetailNoteViewModel) {
    editText.doOnTextChanged { text, _, _, _ ->
        viewModel.setNoteTitle(text.toString())
    }
}

@BindingAdapter("app:setContentChanged")
fun setContentChanged(editText: EditText, viewModel: DetailNoteViewModel) {
    editText.doOnTextChanged { text, _, _, _ ->
        viewModel.setNoteContentText(text.toString())
    }
}