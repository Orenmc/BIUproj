function run() {
  var elem = document.getElementById("prgbar_offlineMode");
  var width = 1;
  var id = setInterval(frame, 10);
  function frame() {
    if (width >= 100) {
      clearInterval(id);
    } else {
      width++;
      elem.style.width = width + '%';
      //elem.innerHTML = width  + '%';
    }
  }
}
