package com.example.htmlwrapper.Wrappers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeVisitor
import java.util.regex.Pattern

class HtmlWrapperClassic : HtmlWrapper {
    private val regex = "(\\.\\.\\.|[.!?])".toRegex()
    private var sentenceIndex = 1
    private val spanWrapper get() = "<span id=\"tts${sentenceIndex}\"></span>"
    private val pattern = Pattern.compile("([.!?])\\s*")

    override fun wrapHtmlWithSpan(html: String): String {
        val doc: Document = Jsoup.parse(html)
        val allTexts = mutableListOf<TextNode>()

        val nodeVisitor = object : NodeVisitor {
            override fun tail(node: Node, depth: Int) = Unit
            override fun head(node: Node, depth: Int) {
                if (node is TextNode && node.text().isNotBlank()) {
                    allTexts.add(node)
                }
            }
        }
        doc.body().traverse(nodeVisitor)
        sentenceIndex = 1
        allTexts.forEach { node ->
            if (regex.containsMatchIn(node.text())) {
                splitTextWithSpans(node)
            } else {
                node.wrap(spanWrapper)
            }
        }


        return doc.body().html()
    }

    private fun splitTextWithSpans(node: TextNode) {
        val matcher = pattern.matcher(node.text())
        val splitedIndexes = mutableListOf<Int>(0)
        while (matcher.find()) {
            splitedIndexes.add(matcher.end() - 1)
        }
        val nodesForWrap = mutableListOf<Node>()
        splitedIndexes.reversed().forEach {
            val splitedText = node.text().substring(it)
            if (splitedText.isNotBlank() && splitedText.trim().length > 1) {
                nodesForWrap.add(node.splitText(it))
            }
        }
        nodesForWrap.reversed().forEach {
            it.wrap(spanWrapper)
            sentenceIndex++
        }
    }
}