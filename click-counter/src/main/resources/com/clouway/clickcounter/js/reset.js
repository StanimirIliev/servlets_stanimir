function reset() {
  document.getElementById("link1").innerHTML = "Clicked: 0"
  document.getElementById("link2").innerHTML = "Clicked: 0"
  document.getElementById("link3").innerHTML = "Clicked: 0"
  document.location = "http://127.0.0.1:8080/ClickCounter?reset"
}
