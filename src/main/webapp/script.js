$(document).ready(function() {
    // Poll for new messages every 2 seconds
    setInterval(fetchMessages, 2000);
    
    // Send message when button is clicked
    $('#sendButton').click(sendMessage);
    
    // Also send when Enter is pressed
    $('#messageInput').keypress(function(e) {
        if (e.which == 13) {
            sendMessage();
        }
    });
});

function fetchMessages() {
    $.ajax({
        url: 'ChatServlet',
        type: 'GET',
        data: { action: 'getMessages' },
        success: function(data) {
            $('#chatBox').empty();
            data.forEach(function(message) {
                const msgHtml = `
                    <div class="message">
                        <span class="sender">${message.sender}:</span>
                        <span>${message.content}</span>
                        <span class="time">${message.timestamp}</span>
                    </div>
                `;
                $('#chatBox').append(msgHtml);
            });
            // Scroll to bottom
            $('#chatBox').scrollTop($('#chatBox')[0].scrollHeight);
        },
        dataType: 'json'
    });
}

function sendMessage() {
    const username = $('#username').val().trim();
    const message = $('#messageInput').val().trim();
    
    if (username && message) {
        $.ajax({
            url: 'ChatServlet',
            type: 'POST',
            data: { 
                action: 'sendMessage',
                sender: username,
                content: message
            },
            success: function() {
                $('#messageInput').val('');
                fetchMessages(); // Refresh messages
            }
        });
    }
}