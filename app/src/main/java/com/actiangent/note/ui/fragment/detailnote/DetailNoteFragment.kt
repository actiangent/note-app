package com.actiangent.note.ui.fragment.detailnote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.actiangent.note.R
import com.actiangent.note.databinding.DialogDeleteNoteBinding
import com.actiangent.note.databinding.FragmentNoteDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailNoteFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private var _deleteDialogBinding: DialogDeleteNoteBinding? = null
    private val binding get() = _binding!!
    private val deleteDialogBinding get() = _deleteDialogBinding!!

    private var _deleteDialog: AlertDialog? = null
    private val deleteDialog get() = _deleteDialog!!
    private val showDialog = MutableStateFlow(false)

    private val viewModel: DetailNoteViewModel by viewModels()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            saveNoteAndNavigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false).apply {
            viewModel = this@DetailNoteFragment.viewModel
            lifecycleOwner = this@DetailNoteFragment.viewLifecycleOwner
        }
        _deleteDialogBinding = DialogDeleteNoteBinding.inflate(layoutInflater)

        _deleteDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
                .setView(deleteDialogBinding.root)
                .create()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDetailNoteToolbar()
        setupDeleteNoteDialog()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { uiState ->
                    showDeleteMenu(uiState.isNoteSaved)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                showDialog.collectLatest { showDialog ->
                    if (showDialog) deleteDialog.show()
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher
            .addCallback(
                this, // LifecycleOwner
                onBackPressedCallback
            )
    }

    private fun setupDetailNoteToolbar() {
        binding.detailNoteToolbar.apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_note_menu -> {
                        showDialog.update { true }
                        true
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                saveNoteAndNavigateUp()
            }
        }
    }

    private fun showDeleteMenu(show: Boolean) {
        binding.detailNoteToolbar.menu.findItem(R.id.delete_note_menu).isVisible =
            viewModel.uiState.value.isNoteSaved
    }

    private fun setupDeleteNoteDialog() {
        deleteDialogBinding.apply {
            deleteNoteConfirmTextView.setOnClickListener {
                showDialog.update { false }
                deleteDialog.dismiss()
                deleteNoteAndNavigateUp()
            }
            deleteNoteCancelTextView.setOnClickListener {
                showDialog.update { false }
                deleteDialog.dismiss()
            }
        }
    }

    private fun saveNoteAndNavigateUp() {
        viewModel.saveNote()
        findNavController().navigateUp()
    }

    private fun deleteNoteAndNavigateUp() {
        viewModel.deleteNote()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _deleteDialog = null
        _deleteDialogBinding = null
        _binding = null
    }
}