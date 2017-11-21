package com.clouway.showpages

import com.clouway.radiobuttons.RadioButtonsServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.nio.charset.Charset
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ShowPagesServlet(port: Int) : HttpServlet() {

    private val server: Server = Server(port)
    private val style = RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/css/style.css").readBytes()
            .toString(Charset.defaultCharset())
    private val script = RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/js/functionality.js").readBytes()
            .toString(Charset.defaultCharset())
    private var doc = RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/index.html").readBytes()
            .toString(Charset.defaultCharset()).replace(
            "<link rel=\"stylesheet\" href=\"css/style.css\">",
            "<style>$style</style>").replace(
            "<script src=\"js/functionality.js\"></script>",
            "<script>$script</script>")
    private var page1 = Page("Page1",
            RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/page1.html").readBytes()
                    .toString(Charset.defaultCharset()).replace(
                    "<link rel=\"stylesheet\" href=\"css/style.css\" />",
                    "<style>$style</style>").replace(
                    "<script src=\"js/functionality.js\"></script>",
                    "<script>$script</script>"))
    private var page2 = Page("Page2",
            RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/page2.html").readBytes()
                    .toString(Charset.defaultCharset()).replace(
                    "<link rel=\"stylesheet\" href=\"css/style.css\" />",
                    "<style>$style</style>").replace(
                    "<script src=\"js/functionality.js\"></script>",
                    "<script>$script</script>"))
    private var page3 = Page("Page3",
            RadioButtonsServlet::class.java.getResourceAsStream("../../../com/clouway/showpages/page3.html").readBytes()
                    .toString(Charset.defaultCharset()).replace(
                    "<link rel=\"stylesheet\" href=\"css/style.css\" />",
                    "<style>$style</style>").replace(
                    "<script src=\"js/functionality.js\"></script>",
                    "<script>$script</script>"))

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
                            when {
                                req.getParameter("pageName") == page1.name -> {
                                    out.print(page1.doc)
                                }
                                req.getParameter("pageName") == page2.name -> {
                                    out.print(page2.doc)
                                }
                                req.getParameter("pageName") == page3.name -> {
                                    out.print(page3.doc)
                                }
                            }
                        }
                        else {
                            out.print(doc)
                        }
                    }
                }).addMapping("/ShowPages")
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