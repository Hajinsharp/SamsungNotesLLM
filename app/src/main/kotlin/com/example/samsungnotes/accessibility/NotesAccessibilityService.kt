package com.example.samsungnotes.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import timber.log.Timber

class NotesAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                    // Detect when Samsung Notes app is in focus
                    val packageName = event.packageName?.toString() ?: return
                    if (packageName.contains("samsung") || packageName.contains("notes")) {
                        Timber.d("Samsung Notes content detected")
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        Timber.d("Accessibility service interrupted")
    }

    /**
     * Extract text content from the current accessibility tree
     * This scans for text nodes in Samsung Notes
     */
    fun extractNotesContent(): String {
        val rootNode = rootInActiveWindow ?: return ""
        return extractTextFromNode(rootNode)
    }

    private fun extractTextFromNode(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""

        val stringBuilder = StringBuilder()

        // Get text from current node
        node.text?.let { text ->
            if (text.isNotEmpty()) {
                stringBuilder.append(text).append("\n")
            }
        }

        // Recursively get text from child nodes
        for (i in 0 until (node.childCount ?: 0)) {
            node.getChild(i)?.let { childNode ->
                stringBuilder.append(extractTextFromNode(childNode))
            }
        }

        return stringBuilder.toString().trim()
    }

    companion object {
        private const val TAG = "NotesAccessibilityService"
    }
}
