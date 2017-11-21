package com.clouway.showpages

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.nio.charset.Charset
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HandlerServlet(port: Int): HttpServlet() {

    private val server: Server = Server(port)
    private val style = HandlerServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/" +
            "css/style.css").readBytes()
            .toString(Charset.defaultCharset())
    private val script = HandlerServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/" +
            "js/functionality.js").readBytes()
            .toString(Charset.defaultCharset())
    private var doc = HandlerServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/" +
            "pageName.html").readBytes()
            .toString(Charset.defaultCharset()).replace(
            "<link rel=\"stylesheet\" href=\"css/style.css\" />",
            "<style>$style</style>").replace(
            "<script src=\"js/functionality.js\"></script>",
            "<script>$script</script>")

    fun start() {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        context.addEventListener(object : ServletContextListener {

            override fun contextInitialized(servletContextEvent: ServletContextEvent) {
                val servletContext = servletContextEvent.servletContext

                servletContext.addServlet("test", object : HttpServlet() {

                    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
                        resp.contentType = "text/html; charset=utf-8"
                        val out = resp.writer
                        if(req.getParameter("pageName") !== null) {
                            out.print(doc.replace(
                                    "<h2 id=\"pageName\"></h2>",
                                    "<h2 id=\"pageName\">${req.getParameter("pageName")} " +
                                            "has been redirected to me</h2>"
                            ))
                        }
                        else {
                            out.print(doc.replace(
                                    "<h2 id=\"pageName\"></h2>",
                                    "<h2 id=\"pageName\">No page has been redirected to me</h2>"
                            ))
                        }
                    }
                }).addMapping("/HandlerServlet")
            }

            override fun contextDestroyed(servletContextEvent: ServletContextEvent) {

            }
        })

        server.handler = context
        try {
            server.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}