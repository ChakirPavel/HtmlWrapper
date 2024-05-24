package com.example.htmlwrapper.Wrappers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeVisitor
import java.util.regex.Pattern

class HtmlWrapperClassic : HtmlWrapper {
    private fun getSpanWrapper(index: Int) = "<span id=\"tts${index}\"></span>"

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
        var sentenceIndex = 1
        val regex = regexPattern.toRegex()
        allTexts.forEach { node ->
            if (regex.containsMatchIn(node.text())) {
                returnNodesBySplit(node).forEach {
                    it.wrap(getSpanWrapper(sentenceIndex))
                    sentenceIndex++
                }
            } else {
                node.wrap(getSpanWrapper(sentenceIndex))
            }
        }


        return doc.body().html()
    }

    // Делим строку, если в строке несколько предложений.
    private fun returnNodesBySplit(node: TextNode): List<Node> {
        val matcher = Pattern.compile(patternString).matcher(node.text())
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
        return nodesForWrap.reversed()
    }

    companion object {
        private const val regexPattern = "(\\.\\.\\.|[.!?])"
        private const val patternString = "([.!?])\\s*"
    }
}