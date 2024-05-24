package com.example.htmlwrapper.Wrappers

import org.jsoup.Jsoup
import java.util.regex.Pattern

class HtmlWrapperParagraph : HtmlWrapper {

    private fun getStartSpan(index: Int) = "<span id=\"tts${index}\">"

    override fun wrapHtmlWithSpan(html: String): String {
        val document = Jsoup.parse(html)
        val paragraphs = document.select(TAG_PARAGRAPH)
        var sentenceIndex = 1

        paragraphs.forEach { paragraph ->
            val paragraphText = paragraph.html()
            val matcher = Pattern.compile(
                PATTERN_SENTENCE
            ).matcher(paragraph.html())
            val result = StringBuilder()
            var lastIndex = 0

            while (matcher.find()) {
                val end = matcher.end()
                result.append(getStartSpan(index = sentenceIndex++))
                result.append(paragraphText.substring(lastIndex, matcher.end(1)).trim())
                result.append(END_SPAN)
                lastIndex = end
            }
            // Добавляем оставшийся текст, если есть
            if (lastIndex < paragraphText.length) {
                result.append(getStartSpan(index = sentenceIndex++))
                result.append(paragraphText.substring(lastIndex).trim())
                result.append(END_SPAN)
            }
            paragraph.html(result.toString())
        }

        return paragraphs.joinToString("\n") { it.outerHtml() }
    }

    companion object {
        private const val END_SPAN = "</span>"
        private const val TAG_PARAGRAPH = "p"
        private const val PATTERN_SENTENCE = "([.!?])\\s*"
    }
}