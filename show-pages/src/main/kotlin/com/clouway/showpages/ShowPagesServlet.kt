package com.clouway.showpages

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ShowPagesServlet : HttpServlet() {

    private var doc = ShowPagesServlet::class.java.getResourceAsStream("index.html").readBytes()
            .toString(Charset.defaultCharset())
    private var pages = getPages(doc)

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val out = resp.writer
        if (req.getParameter("pageName") !== null) {
            pages.filter { it.name == req.getParameter("pageName") }.forEach {
                out.print(it.doc)
            }
        } else {
            out.print(doc)
        }
    }

    private fun getPages(doc: String): List<Page> {
        val pages = ArrayList<Page>()
        var cursor = doc.indexOf("<a ", ignoreCase = true)
        while (cursor != -1) {
            cursor = doc.indexOf("?pageName=", cursor) + 10
            val pageName = doc.substring(cursor, doc.indexOf("\"", cursor))
            pages.add(Page(pageName, ShowPagesServlet::class.java.getResourceAsStream(pageName + ".html")
                    .readBytes().toString(Charset.defaultCharset())))
            cursor = doc.indexOf("<a ", cursor, ignoreCase = true)
        }
        return pages
    }
}