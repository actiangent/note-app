package com.actiangent.note.ui.screen.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.actiangent.note.R
import com.actiangent.note.data.model.Note
import com.actiangent.note.data.model.emptyNote
import com.actiangent.note.ui.components.NoteItem
import com.actiangent.note.ui.components.SearchBar
import com.actiangent.note.ui.theme.NotesAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToDetailNote: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    homeScreenState: ScaffoldState = rememberScaffoldState()
) {
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
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                modifier = modifier.testTag("addNoteButton")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_content_description)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsState()
        val showSearchBar: Boolean by remember {
            derivedStateOf {
                !listState.isScrollInProgress or (listState.firstVisibleItemIndex < 3) or
                        uiState.searchQuery.isNotBlank()
            }
        }

        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            if (uiState.searchQuery.isNotBlank() and uiState.notes.isEmpty()) {
                EmptySearchedHomeContent(modifier = modifier)
            } else {
                HomeContent(
                    notes = uiState.notes,
                    onClick = navigateToDetailNote,
                    listState = listState,
                    modifier = modifier
                )
            }

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
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::setQuery,
                    clearQuery = viewModel::clearQuery,
                    onDrawerIconClick = {
                        coroutineScope.launch { homeScreenState.drawerState.open() }
                    }
                )
            }

            uiState.snackbarMessage?.let { message ->
                val snackbarText = stringResource(message)
                LaunchedEffect(homeScreenState, viewModel, message, snackbarText) {
                    homeScreenState.snackbarHostState.showSnackbar(snackbarText)
                    viewModel.snackbarMessageShown()
                }
            }
        }
    }

    if (homeScreenState.drawerState.isOpen) {
        BackHandler {
            coroutineScope.launch { homeScreenState.drawerState.close() }
        }
    }
}

@Composable
fun HomeContent(
    notes: List<Note>,
    listState: LazyStaggeredGridState,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    EmptyContent(
        isContentEmpty = notes.isEmpty(),
        emptyContent = { EmptyHomeContent() }
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
                .semantics(mergeDescendants = true) { testTag = "noteList" }
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(modifier = modifier.padding(top = 64.dp))
            }
            items(items = notes, key = { note ->
                note.id
            }) { note ->
                NoteItem(note = note, onClick = onClick)
            }
        }
    }
}

@Composable
fun EmptyHomeContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_note_24),
            contentDescription = stringResource(
                id = R.string.empty_notes_content_description
            ),
            tint = MaterialTheme.colors.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.empty_notes_message),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun EmptySearchedHomeContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.empty_searched_notes_message),
            color = MaterialTheme.colors.primary,
            fontSize = 16.sp
        )
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
fun EmptyContent(
    isContentEmpty: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (isContentEmpty) {
        emptyContent()
    } else {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    NotesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeContent(
                notes = listOf(
                    Note("Note title 1", "Note content 1", "1990-01-01 00:00", 1),
                    Note("Note title 2", "Note content 2", "1990-01-01 00:00", 2),
                    Note("Note title 3", "Note content 3", "1990-01-01 00:00", 3),
                ),
                listState = rememberLazyStaggeredGridState(),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentEmptyPreview() {
    NotesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeContent(
                notes = emptyList(),
                listState = rememberLazyStaggeredGridState(),
                onClick = {}
            )
        }
    }
}