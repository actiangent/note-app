package com.actiangent.note.ui.screen.detailnote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.actiangent.note.R
import com.actiangent.note.data.model.Note
import com.actiangent.note.ui.components.DeleteNoteConfirmationDialog
import com.actiangent.note.ui.theme.NotesAppTheme

@Composable
fun DetailNoteContent(
    title: String,
    contentText: String,
    dateTime: String,
    onTitleChange: (String) -> Unit,
    onContentTextChange: (String) -> Unit,
    isDeleteNoteButtonShown: Boolean,
    deleteNote: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.background,
        cursorColor = MaterialTheme.colors.onSurface,
        focusedIndicatorColor = MaterialTheme.colors.background,
        unfocusedIndicatorColor = MaterialTheme.colors.background,
        disabledIndicatorColor = MaterialTheme.colors.background,
        focusedLabelColor = MaterialTheme.colors.background
    )

    if (showDialog) {
        DeleteNoteConfirmationDialog(onConfirm = deleteNote, onDismiss = { showDialog = false })
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_content_description),
                        modifier = modifier
                    )
                }
            },
            actions = {
                if (isDeleteNoteButtonShown) {
                    IconButton(onClick = { showDialog = true },
                        modifier = modifier
                            .semantics(mergeDescendants = true) {
                                contentDescription = "Delete Note"
                            }
                            .testTag("deleteNoteButton")) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(id = R.string.back_content_description),
                            modifier = modifier
                        )
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            elevation = 0.dp
        )
        TextField(
            value = title,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.note_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            onValueChange = onTitleChange,
            textStyle = MaterialTheme.typography.subtitle1.copy(
                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
            ),
            colors = textFieldColors,
            modifier = modifier
                .width(IntrinsicSize.Min)
                .padding(start = 2.dp)
                .semantics(mergeDescendants = true) { contentDescription = "Note title" }
                .testTag("titleTextField")
        )
        Text(
            text = dateTime,
            fontSize = 14.sp,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Medium,
            ),
            modifier = modifier
                .padding(start = 20.dp)
                .semantics(mergeDescendants = true) { contentDescription = "Note updated at" }
                .testTag("updatedAt")
        )
        TextField(
            value = contentText,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.note), fontSize = 16.sp
                )
            },
            onValueChange = onContentTextChange,
            textStyle = MaterialTheme.typography.subtitle1.copy(
                fontSize = 16.sp
            ),
            colors = textFieldColors,
            modifier = modifier
                .width(IntrinsicSize.Min)
                .padding(start = 4.dp, end = 4.dp)
                .semantics(mergeDescendants = true) { contentDescription = "Note content" }
                .testTag("contentTextField")
        )
    }

    BackHandler { navigateBack() }
}

@Composable
fun DetailNoteScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailNoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        DetailNoteContent(title = uiState.title,
            contentText = uiState.contentText,
            dateTime = uiState.dateTime,
            onTitleChange = viewModel::setNoteTitle,
            onContentTextChange = viewModel::setNoteContentText,
            isDeleteNoteButtonShown = uiState.isNoteSaved,
            deleteNote = {
                viewModel.deleteNote()
                navigateBack()
            },
            navigateBack = {
                viewModel.saveNote()
                navigateBack()
            })
    }
}

@Preview(showBackground = true)
@Composable
fun DetailNoteScreenPreview() {
    val fakeNote = Note(
        id = 3, title = "Doug Dimmadome", contentText = """
            My name is Dummsdaledimmadaledimmadimsdomedudidome Dimsdimmadimmadome, owner of the Duhdimmsdimmadaledimmadimmsdome Dudiduhdimmsdaledimma Dimmsdale Dimmadome! 
            Thank you for locating my long-lost son, Dale Dummadomedimmsmadomedimmadomedimmsdaledudimmadomedougsdaledimmadome, heir to the Dimmsmadomedimmsdomedimmadaledaledimmadoodougdimmadimmsdomedaledome fortune! 
            If there’s anything I can ever do to repay you for your kindness, all you need to do is ask
        """.trimIndent(), dateTime = "2012-2-9 12:51"
    )
    NotesAppTheme {
        DetailNoteContent(title = fakeNote.title,
            contentText = fakeNote.contentText,
            dateTime = fakeNote.dateTime,
            onTitleChange = {},
            onContentTextChange = {},
            isDeleteNoteButtonShown = true,
            deleteNote = {},
            navigateBack = {})
    }
}