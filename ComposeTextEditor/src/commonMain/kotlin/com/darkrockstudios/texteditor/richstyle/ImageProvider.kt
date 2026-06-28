package com.darkrockstudios.texteditor.richstyle

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Resolves image sources (URLs, file paths, resource ids — whatever the host app
 * uses) to [ImageBlockResource] values. Implementations MUST back the returned
 * state with Compose snapshot state so that asynchronous load completion
 * triggers recomposition of the editor's Canvas.
 *
 * The library does not include a default network/disk loader; pick one that
 * fits your platform (Coil on Android, Skiko on Desktop, etc.) and adapt it.
 */
interface ImageProvider {
	/**
	 * Returns a [State] that tracks the current load status for [source]. The
	 * state may start in [ImageBlockResource.Loading] and transition to
	 * [ImageBlockResource.Loaded] or [ImageBlockResource.Failed] later.
	 */
	fun resolve(source: String): State<ImageBlockResource>
}

sealed interface ImageBlockResource {
	data object Loading : ImageBlockResource
	data class Loaded(val bitmap: ImageBitmap) : ImageBlockResource
	data class Failed(val reason: String) : ImageBlockResource
}

/**
 * Synchronous in-memory provider, useful for tests and for hosts that already
 * have decoded bitmaps. Add bitmaps via [put] before they're requested; calls
 * to [resolve] for unknown sources return [ImageBlockResource.Failed].
 */
class InMemoryImageProvider : ImageProvider {
	private val entries = mutableMapOf<String, androidx.compose.runtime.MutableState<ImageBlockResource>>()

	/**
	 * Registers [bitmap] as the loaded result for [source], overwriting any
	 * existing entry. Subsequent [resolve] calls for [source] return
	 * [ImageBlockResource.Loaded].
	 */
	fun put(source: String, bitmap: ImageBitmap) {
		val existing = entries[source]
		if (existing != null) {
			existing.value = ImageBlockResource.Loaded(bitmap)
		} else {
			entries[source] = mutableStateOf(ImageBlockResource.Loaded(bitmap))
		}
	}

	/**
	 * Marks [source] as failed with [reason], overwriting any existing entry.
	 * Subsequent [resolve] calls for [source] return [ImageBlockResource.Failed].
	 */
	fun fail(source: String, reason: String) {
		val existing = entries[source]
		if (existing != null) {
			existing.value = ImageBlockResource.Failed(reason)
		} else {
			entries[source] = mutableStateOf(ImageBlockResource.Failed(reason))
		}
	}

	/**
	 * Returns the tracked [State] for [source]. Unknown sources resolve to
	 * [ImageBlockResource.Failed] and are recorded so later lookups stay stable.
	 */
	override fun resolve(source: String): State<ImageBlockResource> {
		return entries.getOrPut(source) {
			mutableStateOf(ImageBlockResource.Failed("not provided: $source"))
		}
	}
}
