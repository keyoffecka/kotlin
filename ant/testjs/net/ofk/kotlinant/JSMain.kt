package net.ofk.kotlinant

import kotlin.browser.window

fun main() {
  window.addEventListener("load", {
    console.log("Hello world");
  })
}