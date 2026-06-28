package com.darkrockstudios.texteditor.state

import androidx.compose.ui.text.SpanStyle
import com.darkrockstudios.texteditor.CharLineOffset
import com.darkrockstudios.texteditor.TextEditorRange

/**
 * A snapshot of the cursor for handlers that react to caret movement.
 *
 * @property position The caret's [CharLineOffset] within the document.
 * @property styles The [SpanStyle]s active at [position].
 * @property selection The selected [TextEditorRange], or `null` when nothing is selected.
 */
data class CursorData(
	val position: CharLineOffset,
	val styles: Set<SpanStyle>,
	val selection: TextEditorRange?,
)
