package com.example.htmlwrapper.Wrappers

import org.jsoup.Jsoup
import java.util.regex.Pattern

class HtmlWrapperParagraph : HtmlWrapper {

    private val pattern = Pattern.compile("([.!?])\\s*")
    private val tagParagraph = "p"
    private var sentenceIndex = 1
    private val startSpan get() = "<span id=\"tts${sentenceIndex++}\">"
    private val endSpan get() = "</span>"


    override fun wrapHtmlWithSpan(html: String): String {
        val document = Jsoup.parse(html)
        val paragraphs = document.select(tagParagraph)
        sentenceIndex = 1

        paragraphs.forEach { paragraph ->
            val paragraphText = paragraph.html()
            val matcher = pattern.matcher(paragraph.html())
            val result = StringBuilder()
            var lastIndex = 0

            while (matcher.find()) {
                val end = matcher.end()
                result.append(startSpan)
                result.append(paragraphText.substring(lastIndex, matcher.end(1)).trim())
                result.append(endSpan)
                lastIndex = end
            }
            // Добавляем оставшийся текст, если есть
            if (lastIndex < paragraphText.length) {
                result.append(startSpan)
                result.append(paragraphText.substring(lastIndex).trim())
                result.append(endSpan)
            }
            paragraph.html(result.toString())
        }

        return paragraphs.joinToString("\n") { it.outerHtml() }
    }
}