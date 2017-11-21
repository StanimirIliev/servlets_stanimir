package com.clouway.clickcounter

fun main(args: Array<String>) {
    val clickCounter = ClickCounterServlet(8080)
    clickCounter.start()
}