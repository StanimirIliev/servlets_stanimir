package com.clouway.clickcounter

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ClickCounterServlet : HttpServlet() {

    private var doc = ClickCounterServlet::class.java.getResourceAsStream("index.html").readBytes().toString(Charset.defaultCharset())

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val initialLinks = ArrayList<Link>()
        initialLinks.addAll(listOf(
                Link("link1", 0),
                Link("link2", 0),
                Link("link3", 0)))
        val out = resp.writer
        val session = req.getSession(true)

        if (session.getAttribute("links") == null) {
            session.setAttribute("links", initialLinks)
            out.print(updateDoc(initialLinks))
            return
        }

        val links = session.getAttribute("links") as List<Link>
        when {
            req.getParameter("link") !== null -> {
                links.filter { req.getParameter("link") == it.value }.forEach {it.visits++}
            }
            req.getParameter("reset") !== null -> {
                links.forEach {it.visits = 0}
            }
        }
        out.print(updateDoc(links))
        session.setAttribute("links", links)
    }

    private fun updateDoc(links: List<Link>): String {
        var replaceString = ""
        links.forEach {
            replaceString +=
                    "   <a href=\"/ClickCounter?link=${it.value}\">${it.value
                            .replaceFirst('l', 'L')}</a>\n" +
                            "   <label>Clicked: ${it.visits}</label><br>\n"
        }
        return doc.replace("<div id=\"container\">",
                "<div id=\"container\">\n$replaceString")
    }
}