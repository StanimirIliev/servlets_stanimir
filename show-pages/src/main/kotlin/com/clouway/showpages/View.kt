package com.clouway.showpages

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class View : HttpServlet() {
    private var doc = View::class.java.getResourceAsStream("targetPageTemplate.html").readBytes()
            .toString(Charset.defaultCharset())

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val out = resp.writer
        if (req.getParameter("pageName") !== null) {
            out.print(doc.replace(
                    "<h2></h2>",
                    "<h2>${req.getParameter("pageName")} " +
                            "has been redirected to me</h2>"
            ))
        } else {
            out.print(doc.replace(
                    "<h2></h2>",
                    "<h2>No page has been redirected to me</h2>"
            ))
        }
    }
}