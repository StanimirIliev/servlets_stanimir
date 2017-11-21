package com.clouway.showpages

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HandlerServlet : HttpServlet() {
    private var doc = HandlerServlet::class.java.getResourceAsStream("pageName.html").readBytes()
            .toString(Charset.defaultCharset())

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val out = resp.writer
        if (req.getParameter("pageName") !== null) {
            out.print(doc.replace(
                    "<h2 id=\"pageName\"></h2>",
                    "<h2 id=\"pageName\">${req.getParameter("pageName")} " +
                            "has been redirected to me</h2>"
            ))
        } else {
            out.print(doc.replace(
                    "<h2 id=\"pageName\"></h2>",
                    "<h2 id=\"pageName\">No page has been redirected to me</h2>"
            ))
        }
    }
}