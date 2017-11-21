package com.clouway.clickcounter

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ResourcesServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {

        val uri = req.requestURI

        if (!uri.contains("/")) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
            return
        }

        val fileName = uri.substring(uri.lastIndexOf("/") + 1, uri.length)


        val input = ResourcesServlet::class.java.getResourceAsStream(fileName)
        if (input == null) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
            return
        }

        if (uri.endsWith(".css")) {
            resp.contentType = "text/css"
        }

        if (uri.endsWith(".js")) {
            resp.contentType = "application/javascript"
        }


        input.copyTo(resp.outputStream)
    }
}