package com.clouway.showpages

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class Server(val port: Int) {
    private val server = Server(port)

    fun start() {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        context.addEventListener(object : ServletContextListener {
            override fun contextInitialized(servletContextEvent: ServletContextEvent) {
                val servletContext = servletContextEvent.servletContext
                servletContext.addServlet("resources", ResourceServlet())
                        .addMapping("/assets/*")
                servletContext.addServlet("handlerServlet", HandlerServlet())
                        .addMapping("/HandlerServlet")
                servletContext.addServlet("showPages", ShowPagesServlet())
                        .addMapping("/ShowPages")
            }

            override fun contextDestroyed(sce: ServletContextEvent?) {

            }
        })
        server.handler = context
        try {
            server.start()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}