package com.actiangent.note.ui.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeNoteListAdapter: HomeNoteListAdapter

    private val isScrollInProgress = MutableStateFlow(false)
    private val listItemPosition = MutableStateFlow(0)
    private val searchBarQueryIsEmpty = MutableStateFlow(true)

    private val showSearchBar =
        combine(
            isScrollInProgress,
            listItemPosition,
            searchBarQueryIsEmpty
        ) { isScrollInProgress, listItemPosition, searchBarQueryIsEmpty ->
            !isScrollInProgress or (listItemPosition < 3) or !searchBarQueryIsEmpty
        }.stateIn(
            lifecycleScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = this@HomeFragment.viewModel
            lifecycleOwner = this@HomeFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBarEditText()
        setupNoteListRecyclerView()
        setupAddNoteFab()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { uiState ->
                    uiState.snackbarMessage?.let { stringResId ->
                        showSnackbar(getString(stringResId))
                        viewModel.snackbarMessageShown()
                    }

                    homeNoteListAdapter.submitList(uiState.notes)
                    whichContentVisible(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                showSearchBar.collectLatest { showSearchBar ->
                    animateSearchBarOnScroll(showSearchBar)
                }
            }
        }
    }

    private fun animateSearchBarOnScroll(isVisible: Boolean) {
        val searchBar = binding.searchbar.root
        val transition: Transition = Slide(Gravity.TOP).apply {
            duration = 350
            addTarget(searchBar)
        }

        TransitionManager.beginDelayedTransition(searchBar.parent as ViewGroup, transition)
        searchBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupSearchBarEditText() {
        binding.searchbar.searchQueryEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearchQuery(text.toString())
            searchBarQueryIsEmpty.value = text.toString().isEmpty()
        }
    }

    private fun setupNoteListRecyclerView() {
        homeNoteListAdapter = HomeNoteListAdapter { noteId -> navigateToDetailNote(noteId) }
        binding.recyclerViewNotes.apply {
            val orientation = resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            val lm = StaggeredGridLayoutManager(spanCount, RecyclerView.VERTICAL)
                .also { layoutManager = it }
            adapter = ConcatAdapter(HeaderNoteListAdapter(), homeNoteListAdapter)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val pos = lm.findFirstVisibleItemPositions(null)
                    listItemPosition.value = pos.first()
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isScrollInProgress.value = (newState != RecyclerView.SCROLL_STATE_IDLE)
                }
            })
        }
    }

    private fun setupAddNoteFab() {
        binding.addNoteFab.setOnClickListener { navigateToDetailNote(emptyNote.id) }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireContext(), binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun whichContentVisible(uiState: HomeNoteUiState) {
        if (uiState.notes.isEmpty() and uiState.searchQuery.isNotBlank()) {
            binding.recyclerViewNotes.visibility = View.INVISIBLE
            binding.emptyNotesContent.visibility = View.INVISIBLE
            binding.emptySearchedNotesContent.visibility = View.VISIBLE
        } else if (uiState.notes.isEmpty()) {
            binding.recyclerViewNotes.visibility = View.INVISIBLE
            binding.emptyNotesContent.visibility = View.VISIBLE
            binding.emptySearchedNotesContent.visibility = View.INVISIBLE
        } else {
            binding.recyclerViewNotes.visibility = View.VISIBLE
            binding.emptyNotesContent.visibility = View.INVISIBLE
            binding.emptySearchedNotesContent.visibility = View.INVISIBLE
        }
    }

    private fun navigateToDetailNote(noteId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(noteId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}