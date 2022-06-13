document.addEventListener('DOMContentLoaded', function() {
    var checkPageButton = document.getElementById('Summarize');
    checkPageButton.addEventListener('click', function() {
        chrome.tabs.getSelected(null, function(tab) {
            d = document;
            var f = d.createElement('form');
            f.action = 'http://127.0.0.1:5000/transcript';
            f.method = 'get';
            var i = d.createElement('input');
            i.type = 'hidden';
            i.name = 'transcript';
            f.appendChild(i);
            d.body.appendChild(f);
            f.submit();
        });
    }, false);
}, false);