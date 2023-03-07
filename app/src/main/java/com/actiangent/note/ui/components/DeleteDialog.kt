package com.actiangent.note.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.actiangent.note.R
import com.actiangent.note.ui.theme.NotesAppTheme

@Composable
fun DeleteNoteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = modifier
                .semantics(mergeDescendants = true) { testTag = "deleteConfirmDialog" }
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.confirm_deletion),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = modifier.padding(4.dp))
                Text(
                    text = stringResource(id = R.string.confirm_deletion_text),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                    TextButton(onClick = onConfirm) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DeleteNoteConfirmationDialogPreview() {
    NotesAppTheme {
        DeleteNoteConfirmationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}