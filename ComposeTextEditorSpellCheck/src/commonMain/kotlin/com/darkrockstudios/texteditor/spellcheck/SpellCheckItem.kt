package com.darkrockstudios.texteditor.spellcheck

import com.darkrockstudios.texteditor.spellcheck.api.Correction
import com.darkrockstudios.texteditor.state.WordSegment

/**
 * Represents a spell check item that can be either a word-level misspelling or a sentence-level correction.
 */
sealed class SpellCheckItem {
	/** A word-level misspelling identified by its [WordSegment]. */
	data class MisspelledWord(val segment: WordSegment) : SpellCheckItem()

	/** A sentence-level issue described by a [Correction]. */
	data class SentenceIssue(val correction: Correction) : SpellCheckItem()
}