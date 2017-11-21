package com.clouway.clickcounter

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ClickCounterServlet : HttpServlet() {

    private var doc = ClickCounterServlet::class.java.getResourceAsStream("index.html").readBytes().toString(Charset.defaultCharset())

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val out = resp.writer
        val session = req.getSession(true)

        if (session.getAttribute("links") == null) {
            session.setAttribute("links", getLinks(doc))
        }

        when {
            req.getParameter("link") !== null -> {
                val links = session.getAttribute("links") as List<Link>
                links
                        .filter { req.getParameter("link") == it.value }
                        .forEach {
                            doc = doc.replace(
                                    "<label id=\"${it.value}\">Clicked: ${it.visits}</label>",
                                    "<label id=\"${it.value}\">Clicked: ${++it.visits}</label>")
                        }
                session.setAttribute("links", links)
            }
            req.getParameter("reset") !== null -> {
                val links = session.getAttribute("links") as List<Link>
                links.forEach {
                    doc = doc.replace(
                            "<label id=\"${it.value}\">Clicked: ${it.visits}</label>",
                            "<label id=\"${it.value}\">Clicked: 0</label>")
                    it.visits = 0
                }
                session.setAttribute("links", links)
            }
        }
        out.print(doc)
    }

    private fun getLinks(doc: String): List<Link> {
        val links = ArrayList<Link>()
        var cursor = doc.indexOf("<a ", ignoreCase = true)
        while (cursor != -1) {
            cursor = doc.indexOf("?link=", cursor) + 6
            links.add(Link(doc.substring(cursor,
                    doc.indexOf("\"", cursor)), 0))
            cursor = doc.indexOf("<a ", cursor + 1, ignoreCase = true)
        }
        return links
    }
}