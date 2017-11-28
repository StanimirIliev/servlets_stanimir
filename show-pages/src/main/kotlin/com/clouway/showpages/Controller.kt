package com.clouway.showpages

import java.nio.charset.Charset
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Controller : HttpServlet() {

    private var doc = Controller::class.java.getResourceAsStream("index.html").readBytes()
            .toString(Charset.defaultCharset())

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html; charset=utf-8"
        val out = resp.writer
        when {
            req.getParameter("pageName") !== null -> {
                out.print(Controller::class.java.getResourceAsStream("${req.getParameter("pageName")}.html")
                        .readBytes().toString(Charset.defaultCharset()))
            }
            req.getParameter("redirect") !== null -> {
                resp.sendRedirect("/View?pageName=${req.getParameter("redirect")}")
            }
            else -> out.print(doc)
        }
    }
}