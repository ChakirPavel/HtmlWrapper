package com.example.htmlwrapper

import com.example.htmlwrapper.Wrappers.HtmlWrapperClassic


fun main(){
    val wrapper = HtmlWrapperClassic()

    val html = """
        <p><a href="http://Как бы ссылка"><strong>Ссылка</strong> </a> Начало текста. <a href="http://Как бы ссылка">Ссылка2.</a>Продолжение текста</p><br><br><p></p><p>И конец текста</p>
    """.trimIndent()

    val result = wrapper.wrapHtmlWithSpan(html)

    print("Result: $result")
}