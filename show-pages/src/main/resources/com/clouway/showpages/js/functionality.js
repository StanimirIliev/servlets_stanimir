function redirect(nameOfPage) {
  document.location = "http://127.0.0.1:8081/HandlerServlet?pageName=" + nameOfPage
}
function back() {
  document.location = "http://127.0.0.1:8080/ShowPages"
}
