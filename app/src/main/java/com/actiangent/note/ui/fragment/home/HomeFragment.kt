package com.actiangent.note.ui.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.actiangent.note.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeNoteListAdapter: HomeNoteListAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = this@HomeFragment.viewModel
        }
        _binding!!.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNoteListRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { uiState ->
                    homeNoteListAdapter.submitList(uiState.notes)
                }
            }
        }
    }

    private fun animateSearchBarOnScroll(isVisible: Boolean) {
        val searchBar = binding.searchBar.root
        val transition: Transition = Slide(Gravity.TOP).apply {
            duration = 500
            addTarget(searchBar)
        }

        TransitionManager.beginDelayedTransition(searchBar.parent as ViewGroup, transition)
        searchBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupNoteListRecyclerView() {
        homeNoteListAdapter = HomeNoteListAdapter()
        binding.recyclerViewNotes.apply {
            val orientation = resources.configuration.orientation
            val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            adapter = ConcatAdapter(HeaderNoteListAdapter(), homeNoteListAdapter)
            layoutManager = StaggeredGridLayoutManager(spanCount, RecyclerView.VERTICAL)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        animateSearchBarOnScroll(false)
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        animateSearchBarOnScroll(true)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}