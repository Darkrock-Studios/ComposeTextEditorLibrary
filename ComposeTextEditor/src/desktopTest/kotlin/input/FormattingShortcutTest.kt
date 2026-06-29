package input

import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.platform.Clipboard
import com.darkrockstudios.texteditor.CharLineOffset
import com.darkrockstudios.texteditor.TextEditorRange
import com.darkrockstudios.texteditor.input.TextEditorKeyCommandHandler
import com.darkrockstudios.texteditor.input.TextFormattingShortcuts
import com.darkrockstudios.texteditor.markdown.MarkdownConfiguration
import com.darkrockstudios.texteditor.state.TextEditorState
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FormattingShortcutTest {

	private val bold = MarkdownConfiguration.DEFAULT.boldStyle
	private val italic = MarkdownConfiguration.DEFAULT.italicStyle
	private val strikethrough = MarkdownConfiguration.DEFAULT.strikethroughStyle
	private val code = MarkdownConfiguration.DEFAULT.codeStyle

	private val handler = TextEditorKeyCommandHandler()
	private val clipboard = mockk<Clipboard>(relaxed = true)
	private val scope = TestScope()

	private lateinit var state: TextEditorState

	@BeforeTest
	fun setup() {
		state = TextEditorState(
			scope = TestScope(),
			measurer = mockk(relaxed = true),
			initialText = null,
		)
		state.setText("Hello World")
	}

	@OptIn(InternalComposeUiApi::class)
	private fun keyEvent(key: Key, ctrl: Boolean = true, shift: Boolean = false): KeyEvent =
		KeyEvent(
			key = key,
			type = KeyEventType.KeyDown,
			isCtrlPressed = ctrl,
			isShiftPressed = shift,
		)

	private fun selectWorld() {
		state.selector.updateSelection(CharLineOffset(0, 6), CharLineOffset(0, 11))
	}

	private fun dispatch(key: Key, shift: Boolean = false, enabled: Boolean = true): Boolean =
		handler.handleKeyEvent(keyEvent(key, shift = shift), state, clipboard, scope, enabled)

	@Test
	fun `Ctrl+B applies bold to the selection`() {
		selectWorld()
		val consumed = dispatch(Key.B)

		assertTrue(consumed)
		val spans = state.textLines[0].spanStyles
		assertEquals(1, spans.size)
		assertEquals(bold, spans.first().item)
		assertEquals(6, spans.first().start)
		assertEquals(11, spans.first().end)
	}

	@Test
	fun `Ctrl+B toggles bold off when already applied`() {
		selectWorld()
		dispatch(Key.B)
		selectWorld()
		dispatch(Key.B)

		assertTrue(state.textLines[0].spanStyles.none { it.item == bold })
	}

	@Test
	fun `Ctrl+I applies italic to the selection`() {
		selectWorld()
		assertTrue(dispatch(Key.I))
		assertTrue(state.textLines[0].spanStyles.any { it.item == italic })
	}

	@Test
	fun `Ctrl+E applies inline code to the selection`() {
		selectWorld()
		assertTrue(dispatch(Key.E))
		assertTrue(state.textLines[0].spanStyles.any { it.item == code })
	}

	@Test
	fun `Ctrl+Shift+X applies strikethrough and does not cut`() {
		selectWorld()
		assertTrue(dispatch(Key.X, shift = true))
		assertEquals("Hello World", state.textLines[0].text)
		assertTrue(state.textLines[0].spanStyles.any { it.item == strikethrough })
	}

	@Test
	fun `Ctrl+B with no selection sets a pending cursor style`() {
		assertTrue(dispatch(Key.B))
		assertTrue(state.cursor.styles.contains(bold))
		assertTrue(state.textLines[0].spanStyles.isEmpty())
	}

	@Test
	fun `formatting shortcuts do nothing when disabled`() {
		selectWorld()
		val consumed = handler.handleKeyEvent(keyEvent(Key.B), state, clipboard, scope, enabled = false)

		assertFalse(consumed)
		assertTrue(state.textLines[0].spanStyles.isEmpty())
	}

	@Test
	fun `unbound shortcut is ignored`() {
		selectWorld()
		val consumed = handler.handleKeyEvent(
			keyEvent(Key.B),
			state,
			clipboard,
			scope,
			enabled = true,
			formattingShortcuts = TextFormattingShortcuts.None,
		)

		assertFalse(consumed)
		assertTrue(state.textLines[0].spanStyles.isEmpty())
	}
}
