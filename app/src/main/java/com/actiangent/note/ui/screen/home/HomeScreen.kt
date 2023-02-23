package com.actiangent.note.ui.screen.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.actiangent.note.R
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.ui.components.NoteItem
import com.actiangent.note.ui.components.SearchBar
import com.actiangent.note.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    notes: List<Note>,
    listState: LazyStaggeredGridState,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val staggeredGridCellsCount = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            3
        }
        else -> {
            2
        }
    }

    LazyVerticalStaggeredGrid(
        state = listState,
        columns = StaggeredGridCells.Fixed(staggeredGridCellsCount),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(start = 4.dp, end = 4.dp)
            .semantics(mergeDescendants = true) { contentDescription = "Note list" }
            .testTag("noteList")
    ) {
        items(staggeredGridCellsCount) {
            Box(modifier = modifier.padding(top = 64.dp))
        }
        items(items = notes, key = { note ->
            note.id
        }) { note ->
            NoteItem(note = note, onClick = onClick)
        }
    }

}

@Composable
fun HomeScreenDrawerContent(
    modifier: Modifier = Modifier
) {
    Divider(
        thickness = 2.dp,
        modifier = modifier.padding(start = 16.dp, top = 48.dp, bottom = 8.dp)
    )
}

@Composable
fun HomeScreen(
    navigateToDetailNote: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val notesState by viewModel.uiState.collectAsState()
    val listState = rememberLazyStaggeredGridState()
    val homeScreenState = rememberScaffoldState()
    val showSearchBar: Boolean by remember {
        derivedStateOf {
            !listState.isScrollInProgress or (listState.firstVisibleItemIndex < 3) or
                    notesState.searchQuery.isNotBlank()
        }
    }

    if (homeScreenState.drawerState.isOpen) {
        BackHandler {
            coroutineScope.launch { homeScreenState.drawerState.close() }
        }
    }

    Scaffold(
        scaffoldState = homeScreenState,
        drawerContent = {
            HomeScreenDrawerContent(
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        floatingActionButton = {
            Button(
                onClick = { navigateToDetailNote(emptyNote.id) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                modifier = modifier
                    .testTag("addNoteButton")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_content_description)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
        ) {
            HomeContent(
                notes = notesState.notes,
                onClick = navigateToDetailNote,
                listState = listState,
                modifier = modifier
            )

            AnimatedVisibility(
                visible = showSearchBar,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 350)
                ),
                exit = fadeOut() + slideOutVertically(
                    animationSpec = tween(durationMillis = 350)
                ),
                modifier = modifier
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
            ) {
                SearchBar(
                    query = notesState.searchQuery,
                    onQueryChange = viewModel::setQuery,
                    clearQuery = viewModel::clearQuery,
                    onDrawerIconClick = {
                        coroutineScope.launch { homeScreenState.drawerState.open() }
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NotesAppTheme {
        HomeScreen(navigateToDetailNote = {})
    }
}