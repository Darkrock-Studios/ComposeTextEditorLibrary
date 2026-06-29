package com.darkrockstudios.texteditor.input

import androidx.compose.ui.text.SpanStyle
import com.darkrockstudios.texteditor.markdown.MarkdownConfiguration

/**
 * Maps formatting keyboard shortcuts to the [SpanStyle] each one toggles over the
 * selection (or the cursor for subsequent typing). A `null` style leaves that
 * shortcut unbound.
 *
 * The default styles come from [MarkdownConfiguration.DEFAULT] so toggled spans
 * match the markdown toolbar and round-trip through the markdown extension. If you
 * render with a customized [MarkdownConfiguration], pass the matching styles here.
 *
 * Bindings (matching the editor's Ctrl-based shortcut convention):
 * - [bold]: Ctrl+B
 * - [italic]: Ctrl+I
 * - [strikethrough]: Ctrl+Shift+X
 * - [code]: Ctrl+E
 */
data class TextFormattingShortcuts(
	val bold: SpanStyle? = MarkdownConfiguration.DEFAULT.boldStyle,
	val italic: SpanStyle? = MarkdownConfiguration.DEFAULT.italicStyle,
	val strikethrough: SpanStyle? = MarkdownConfiguration.DEFAULT.strikethroughStyle,
	val code: SpanStyle? = MarkdownConfiguration.DEFAULT.codeStyle,
) {
	companion object {
		/** Bold, italic, strikethrough, and inline code bound to the markdown defaults. */
		val Default = TextFormattingShortcuts()

		/** Every formatting shortcut unbound. */
		val None = TextFormattingShortcuts(
			bold = null,
			italic = null,
			strikethrough = null,
			code = null,
		)
	}
}
