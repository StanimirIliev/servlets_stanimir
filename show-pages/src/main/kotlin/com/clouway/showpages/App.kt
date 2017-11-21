package com.clouway.showpages

fun main(args: Array<String>) {
    val redirectServlet = ShowPagesServlet(8080)
    val handlerServlet = HandlerServlet(8081)
    handlerServlet.start()
    redirectServlet.start()
}