document.addEventListener('DOMContentLoaded', function() {
    var checkPageButton = document.getElementById('Summarize');
    var input = document.getElementById('input');
    var result = document.getElementById('result');
    result.innerHTML = input.innerHTML;
    const Http = new XMLHttpRequest();
    url = 'http://127.0.0.1:5000/transcript?youtube_url=';
    input.addEventListener('blur', function() {
        // get the input value for the video id
        url += input.value;
        console.log(url);
    });
    checkPageButton.addEventListener('click', function() {
        // get the input value for the video id
        Http.open("GET", url);
        Http.send();
    }, false);
    Http.onreadystatechange = function() {
        if (Http.readyState == 4 && Http.status == 200) {
            console.log(Http.responseText);
            result.innerHTML = Http.responseText;
        }
    }
}, false);