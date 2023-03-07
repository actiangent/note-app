package com.actiangent.note.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.actiangent.note.R
import com.actiangent.note.ui.theme.NotesAppTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    clearQuery: () -> Unit,
    onDrawerIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colors.secondary
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = background,
                shape = RoundedCornerShape(128.dp, 128.dp, 128.dp, 128.dp)
            )
    ) {
        if (query.isBlank()) {
            IconButton(onClick = onDrawerIconClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                    contentDescription = stringResource(id = R.string.drawer_content_description),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        } else {
            Box(modifier = modifier.padding(24.dp))
        }

        InputEditText(
            value = query,
            placeHolderString = stringResource(id = R.string.search_note),
            onValueChange = onQueryChange,
            contentTextStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            hintTextStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin
            ),
            modifier = modifier
                .weight(1F)
                .semantics(mergeDescendants = true) { testTag = "noteSearchInputField" }
        )

        if (query.isNotBlank()) {
            IconButton(
                onClick = clearQuery,
                modifier = modifier
                    .semantics(mergeDescendants = true) { testTag = "noteSearchClearQueryButton" }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = stringResource(id = R.string.clear_query_content_description),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun InputEditText(
    value: String,
    onValueChange: (String) -> Unit,
    contentTextStyle: TextStyle,
    hintTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    placeHolderString: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorColor: Color = MaterialTheme.colors.primary,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = contentTextStyle,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        if (contentTextStyle.textAlign == TextAlign.Start)
                            IntOffset(x = 10, y = 0)
                        else
                            IntOffset(x = 0, y = 0)
                    },
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeHolderString,
                        color = hintTextStyle.color,
                        fontSize = hintTextStyle.fontSize
                    )
                }
                innerTextField()
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(cursorColor)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    NotesAppTheme(
        darkTheme = true
    ) {
        SearchBar(
            query = "",
            onQueryChange = {},
            clearQuery = {},
            onDrawerIconClick = {}
        )
    }
}