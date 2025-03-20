package com.kvork_app.diplomawork.view.ui.theme

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(8)
        val out = StringBuilder()
        var count = 0
        for (char in trimmed) {
            out.append(char)
            count++
            if ((count == 2 || count == 4) && count != trimmed.length) {
                out.append('.')
            }
        }
        val outputText = out.toString()
        val transformedTextLength = outputText.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val effective = trimmed.length
                val clamped = offset.coerceAtMost(effective)
                return when {
                    clamped <= 2 -> clamped
                    clamped <= 4 -> clamped + 1
                    else -> clamped + 2
                }.coerceAtMost(transformedTextLength)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 3 -> 2
                    offset <= 5 -> offset - 1
                    else -> (offset - 2).coerceAtMost(trimmed.length)
                }
            }
        }
        return TransformedText(AnnotatedString(outputText), offsetMapping)
    }
}
