package com.darkrockstudios.texteditor.state

import com.darkrockstudios.texteditor.CharLineOffset
import com.darkrockstudios.texteditor.TextEditorRange
import com.darkrockstudios.texteditor.richstyle.RichSpan

/**
 * Extension functions for TextEditorState to inspect spans at a given position
 */

/**
 * Returns the [RichSpan]s whose range contains [position].
 */
fun TextEditorState.getRichSpansAtPosition(position: CharLineOffset): Set<RichSpan> {
	return richSpanManager.getAllRichSpans()
		.filter { it.containsPosition(position) }
		.toSet()
}

/**
 * Returns the [RichSpan]s overlapping [range]. Returns an empty set if either
 * endpoint's line is out of bounds.
 */
fun TextEditorState.getRichSpansInRange(range: TextEditorRange): Set<RichSpan> {
	if (range.start.line < 0 || range.start.line >= textLines.size ||
		range.end.line < 0 || range.end.line >= textLines.size
	) {
		return emptySet()
	}
	return richSpanManager.getSpansInRange(range).toSet()
}
