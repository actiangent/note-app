package com.actiangent.note.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
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
import com.actiangent.note.ui.theme.Shapes

@Composable
fun NoteItem(
    note: Note,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = MaterialTheme.colors.background
    val cardContentColor = MaterialTheme.colors.primary
    val outline = MaterialTheme.colors.secondary

    Card(
        shape = Shapes.small,
        backgroundColor = cardColor,
        contentColor = cardContentColor,
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
        NoteItem(
            note = Note(
                id = 0,
                title = "Why is six afraid of seven?",
                contentText =
                """
                    Six hasn't been the same since he left Vietnam. He can seldom close his eyes without opening them again at fear of Charlies lurking in the jungle trees. 
                    Not that you could ever see the bastards, mind you. They were swift, and they knew their way around the jungle like nothing else. 
                    He remembers the looks on the boys' faces as he walked into that village and... oh, Jesus. The memories seldom left him, either. 
                    Sometimes he'd reminisce - even hear - Tex's southern drawl. He remembers the smell of Brooklyn's cigarettes like nothing else. He always kept a pack of Lucky's with him. 
                    The boys are gone, now. He knows that; it's just that he forgets, sometimes. 
                    And, every now and then, the way that seven looks at him with avid concern in his eyes... it makes him think. Sets him on edge. Makes him feel like he's back there... in the jungle.
                """.trimIndent(),
                dateTime = "2012-08-19 18:57"
            ),
            onClick = {}
        )
    }
}