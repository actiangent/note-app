package com.actiangent.note.ui.fragment.home

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:clearQuery")
fun onClickClearQuery(imageView: ImageView, viewModel: HomeViewModel) =
    imageView.setOnClickListener {
        viewModel.clearQuery()
    }