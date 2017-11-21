package com.clouway.clickcounter

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.handler.ContextHandler



class Server (val portNumber: Int) {
    private val server: Server = Server(portNumber)

    fun start() {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS)
        context.contextPath = "/"
        context.addEventListener(object : ServletContextListener {

            override fun contextInitialized(servletContextEvent: ServletContextEvent) {
                val servletContext = servletContextEvent.servletContext

                servletContext.addServlet("clickCounter", ClickCounterServlet()).addMapping("/ClickCounter")
                servletContext.addServlet("resources", ResourcesServlet()).addMapping("/assets/*")

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