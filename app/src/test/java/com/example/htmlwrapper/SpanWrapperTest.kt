package com.example.htmlwrapper

import com.example.htmlwrapper.Wrappers.HtmlWrapperClassic
import com.example.htmlwrapper.Wrappers.HtmlWrapperParagraph
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SpanWrapperTest {

    val innerHtml = """
            <p>Это первый параграф. <strong>Основной текст здесь выделен жирным.</strong> Дополнительное предложение с <em>курсивом для акцента.</em></p>
            <p>Это второй параграф. <span style="color: red;">Текст в этом спане окрашен в красный цвет.</span> Еще одно предложение с использованием <mark>маркера для выделения.</mark></p>
            <p>Третий параграф начинается здесь. Он содержит ссылку: <a href="https://example.com">Перейти на Example.com</a>. <small>Этот текст немного меньше остального.</small></p>
        """

    @Test
    fun wrapTextNodesWithSpansClassic() {
        val spanWrapper = HtmlWrapperClassic()
        val resultHtml = spanWrapper.wrapHtmlWithSpan(innerHtml)
        val expectedHtml = """
            <p><span id="tts1">Это первый параграф. </span><strong><span id="tts2">Основной текст здесь выделен жирным.</span></strong><span id="tts3"> Дополнительное предложение с </span><em><span id="tts3">курсивом для акцента.</span></em></p> 
<p><span id="tts4">Это второй параграф. </span><span style="color: red;"><span id="tts5">Текст в этом спане окрашен в красный цвет.</span></span><span id="tts6"> Еще одно предложение с использованием </span><mark><span id="tts6">маркера для выделения.</span></mark></p> 
<p><span id="tts7">Третий параграф начинается здесь.</span><span id="tts8"> Он содержит ссылку: </span><a href="https://example.com"><span id="tts9">Перейти на Example</span><span id="tts10">.com</span></a>. <small><span id="tts11">Этот текст немного меньше остального.</span></small></p>
""".trim()
        assertEquals(expectedHtml, resultHtml)
    }

    @Test
    fun wrapTextNodesWithSpansParagraphs() {
        val spanWrapper = HtmlWrapperParagraph()
        val resultHtml = spanWrapper.wrapHtmlWithSpan(innerHtml)
        val expectedHtml = """
            <p><span id="tts1">Это первый параграф.</span><span id="tts2"><strong>Основной текст здесь выделен жирным.</strong></span><strong><span id="tts3"></span></strong> Дополнительное предложение с <em>курсивом для акцента.<span id="tts4"></span></em></p>
<p><span id="tts5">Это второй параграф.</span><span id="tts6"><span style="color: red;">Текст в этом спане окрашен в красный цвет.</span><span id="tts7"></span> Еще одно предложение с использованием <mark>маркера для выделения.</mark></span><span id="tts8"></span></p>
<p><span id="tts9">Третий параграф начинается здесь.</span><span id="tts10">Он содержит ссылку: <a href="https://example.</span><span id=" tts11">com"&gt;Перейти на Example.</a></span><a href="https://example.</span><span id=" tts11"=""><span id="tts12">com</span></a>.<span id="tts13"><small>Этот текст немного меньше остального.</small></span><small><span id="tts14"></span></small></p>
""".trim()
        assertEquals(expectedHtml, resultHtml)
    }
}