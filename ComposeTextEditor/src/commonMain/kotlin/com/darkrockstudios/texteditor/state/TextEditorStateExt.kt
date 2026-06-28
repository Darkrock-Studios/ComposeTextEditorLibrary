package com.darkrockstudios.texteditor.state

import androidx.compose.ui.text.AnnotatedString

/**
 * Inserts [char] as if typed: replaces any active selection, then inserts at the
 * cursor and clears the selection.
 */
fun TextEditorState.insertTypedCharacter(char: Char) {
	if (selector.selection != null) {
		selector.deleteSelection()
	}
	insertCharacterAtCursor(char)
	selector.clearSelection()
}

/**
 * Inserts [string] as if typed: replaces any active selection, then inserts at the
 * cursor and clears the selection.
 */
fun TextEditorState.insertTypedString(string: String) {
	if (selector.selection != null) {
		selector.deleteSelection()
	}
	insertStringAtCursor(string)
	selector.clearSelection()
}

/**
 * Inserts an [AnnotatedString] as if typed, preserving its styling: replaces any
 * active selection, then inserts at the cursor and clears the selection.
 */
fun TextEditorState.insertTypedString(string: AnnotatedString) {
	if (selector.selection != null) {
		selector.deleteSelection()
	}
	insertStringAtCursor(string)
	selector.clearSelection()
}