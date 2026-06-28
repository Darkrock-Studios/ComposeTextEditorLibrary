package com.darkrockstudios.texteditor.state

/**
 * Reports how a rich span was clicked, so a rich-span click handler can tell a
 * touch tap from a left-click and from a right-click (context menu) request.
 */
enum class SpanClickType {
	/** A touch tap on the span. */
	TAP,

	/** A primary (left) mouse-button click. */
	PRIMARY_CLICK,

	/** A secondary (right) mouse-button click, typically a context-menu request. */
	SECONDARY_CLICK,
}