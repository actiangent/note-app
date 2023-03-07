package com.actiangent.note.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.actiangent.note.data.model.Note
import com.actiangent.note.ui.theme.NotesAppTheme

fun NoteItem(
    @Composable
    note: Note,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val outline = MaterialTheme.colors.secondary

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, outline),
        elevation = 0.dp,
        modifier = modifier
            .clickable { onClick(note.id) }
    ) {
        Column(
            modifier = modifier.padding(12.dp)
        ) {
            Text(
                text = note.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = modifier.padding(bottom = 8.dp)
            )
            Text(
                text = note.contentText,
                maxLines = 9,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    NotesAppTheme {
        Surface {
            NoteItem(
                note = Note("Note title", "Note content", "1990-01-01 00:00", 1),
                onClick = {}
            )
        }
    }
}