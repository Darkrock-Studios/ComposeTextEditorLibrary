package com.darkrockstudios.texteditor

import androidx.compose.ui.text.AnnotatedString
import com.darkrockstudios.texteditor.state.TextEditorState

/**
 * A position in the document, expressed as a zero-based [line] and a [char] offset
 * within that line. This is the editor's native coordinate; selections and spans are
 * built from pairs of these (see [TextEditorRange]).
 *
 * Positions are [Comparable] in reading order, and the [isBefore]/[isAfter] infix
 * operators read naturally at call sites. Convert to and from a flat character index
 * across the whole document with [toCharacterIndex] and [Int.toCharLineOffset].
 */
data class CharLineOffset(
	val line: Int,
	val char: Int,
) : Comparable<CharLineOffset> {
	override fun compareTo(other: CharLineOffset): Int {
		return when {
			line < other.line -> -1
			line > other.line -> 1
			else -> char.compareTo(other.char)
		}
	}

	infix fun isBefore(other: CharLineOffset): Boolean = this < other
	infix fun isAfter(other: CharLineOffset): Boolean = this > other
	infix fun isBeforeOrEqual(other: CharLineOffset): Boolean = this <= other
	infix fun isAfterOrEqual(other: CharLineOffset): Boolean = this >= other
}

/** Returns `this` clamped into addressable positions in [textLines], or (0,0) if empty. */
internal fun CharLineOffset.coerceInto(textLines: List<AnnotatedString>): CharLineOffset {
	if (textLines.isEmpty()) return CharLineOffset(0, 0)
	val safeLine = line.coerceIn(0, textLines.lastIndex)
	val safeChar = char.coerceIn(0, textLines[safeLine].length)
	return if (safeLine == line && safeChar == char) this else CharLineOffset(safeLine, safeChar)
}

/** Converts this position to a flat character index over the whole document. */
fun CharLineOffset.toCharacterIndex(state: TextEditorState): Int {
	return state.getCharacterIndex(this)
}

/** Converts a flat character index over the whole document to a [CharLineOffset]. */
fun Int.toCharLineOffset(state: TextEditorState): CharLineOffset {
	return state.getOffsetAtCharacter(this)
}
