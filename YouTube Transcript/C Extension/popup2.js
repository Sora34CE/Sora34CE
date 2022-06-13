// popup2.js
document.addEventListener('DOMContentLoaded', function() {
    var sendMessageButton = document.getElementById('Summarize')
    sendMessageButton.onclick = async function(e) {
        let queryOptions = { active: true, currentWindow: true };
        let tabs = await chrome.tabs.query(queryOptions);

        chrome.tabs.sendMessage(tabs[0].id, {color: "#00FF00"}, function(response) {
        });
    }
}, false);