package com.clouway.clickcounter

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.nio.charset.Charset
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ClickCounterServlet(port: Int) : HttpServlet() {

    private val server: Server = Server(port)
    private val link1 = Link("link1", 0)
    private val link2 = Link("link2", 0)
    private val link3 = Link("link3", 0)
    private val style = ClickCounterServlet::class.java.getResourceAsStream("../../../com/clouway/com.clouway." +
            "clickcounter/css/style.css").readBytes()
            .toString(Charset.defaultCharset())
    private val script = ClickCounterServlet::class.java.getResourceAsStream("../../../com/clouway/com.clouway." +
            "clickcounter/js/reset.js").readBytes()
            .toString(Charset.defaultCharset())
    private var doc = ClickCounterServlet::class.java.getResourceAsStream("../../../com/clouway/com.clouway." +
            "clickcounter/index.html").readBytes()
            .toString(Charset.defaultCharset()).replace(
            "<link rel=\"stylesheet\" href=\"css/style.css\">",
            "<style>$style</style>").replace(
            "<script src=\"js/reset.js\"></script>",
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
                        when {
                            req.getParameter("link") !== null -> when {
                                req.getParameter("link") == link1.value -> {
                                    doc = doc.replace(
                                            "<label id=\"link1\">Clicked: ${link1.visits}</label>",
                                            "<label id=\"link1\">Clicked: ${++link1.visits}</label>")
                                    out.print(doc)
                                }
                                req.getParameter("link") == link2.value -> {
                                    doc = doc.replace(
                                            "<label id=\"link2\">Clicked: ${link2.visits}</label>",
                                            "<label id=\"link2\">Clicked: ${++link2.visits}</label>")
                                    out.print(doc)
                                }
                                req.getParameter("link") == link3.value -> {
                                    doc = doc.replace(
                                            "<label id=\"link3\">Clicked: ${link3.visits}</label>",
                                            "<label id=\"link3\">Clicked: ${++link3.visits}</label>")
                                    out.print(doc)
                                }
                            }
                            req.getParameter("reset") !== null -> {
                                doc = doc.replace(
                                        "<label id=\"link1\">Clicked: ${link1.visits}</label>",
                                        "<label id=\"link1\">Clicked: 0</label>").replace(
                                        "<label id=\"link2\">Clicked: ${link2.visits}</label>",
                                        "<label id=\"link2\">Clicked: 0</label>").replace(
                                        "<label id=\"link3\">Clicked: ${link3.visits}</label>",
                                        "<label id=\"link3\">Clicked: 0</label>")
                                link1.visits = 0
                                link2.visits = 0
                                link3.visits = 0
                                out.print(doc)
                            }
                            else -> out.print(doc)
                        }
                    }
                }).addMapping("/ClickCounter")
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