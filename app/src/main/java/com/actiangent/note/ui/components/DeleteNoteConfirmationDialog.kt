package com.actiangent.note.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.actiangent.note.R

@Composable
fun DeleteNoteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .semantics(mergeDescendants = true) { contentDescription = "Confirm Delete Note" }
                .testTag("deleteConfirmDialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.confirm_deletion),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = stringResource(id = R.string.confirm_deletion_text),
                    style = MaterialTheme.typography.body2
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(onClick = onConfirm) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                }
            }
        }
    }
}