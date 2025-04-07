let currentUser = null;
let selectedFriend = null;
let stompClient = null;

document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/current-user')
        .then(response => response.json())
        .then(user => {
            currentUser = user;
            loadFriends();
        })
        .catch(error => console.error('Ошибка при получении текущего пользователя:', error));
});

function loadFriends() {
    const friendsContainer = document.getElementById('friendsContainer');
    friendsContainer.innerHTML = '<div class="text-center mt-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Загрузка...</span></div></div>';

    fetch('/api/friends')
        .then(response => response.json())
        .then(friends => {
            if (friends.length === 0) {
                friendsContainer.innerHTML = '<div class="alert alert-info">У вас пока нет друзей</div>';
                return;
            }
            let html = '<ul class="list-group">';
            friends.forEach(friend => {
                html += `
                <li class="list-group-item friend-item" onclick="selectFriend(event, ${friend.id}, '${friend.username}')">
                    ${friend.username}
                </li>
                `;
            });
            html += '</ul>';
            friendsContainer.innerHTML = html;
        })
        .catch(error => console.error('Ошибка при загрузке списка друзей:', error));
}

function selectFriend(event, friendId, friendName) {
    selectedFriend = { id: friendId, username: friendName };
    document.querySelectorAll('.friend-item').forEach(item => item.classList.remove('active'));
    event.currentTarget.classList.add('active');
    document.getElementById('messageInput').disabled = false;
    document.getElementById('sendButton').disabled = false;
    loadMessages(friendId);
    connectWebSocket();
}

function loadMessages(friendId) {
    const messagesContainer = document.getElementById('messages');
    messagesContainer.innerHTML = '<div class="text-center mt-3"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Загрузка...</span></div></div>';
    fetch(`/api/messages/${friendId}`)
        .then(response => response.json())
        .then(messages => {
            if (messages.length === 0) {
                messagesContainer.innerHTML = '<div class="text-center mt-5 text-muted">Нет сообщений</div>';
                return;
            }
            let html = '';
            messages.forEach(msg => {
                const isMe = msg.sender.id === currentUser.id;
                html += `
                <div class="mb-3 ${isMe ? 'text-end' : 'text-start'}">
                    <div class="d-inline-block p-2 rounded ${isMe ? 'bg-primary text-white' : 'bg-light'}">
                        ${msg.content}
                    </div>
                    <div class="small text-muted">${new Date(msg.timestamp).toLocaleString()}</div>
                </div>
                `;
            });
            messagesContainer.innerHTML = html;
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        })
        .catch(error => console.error('Ошибка при загрузке сообщений:', error));
}

function connectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected to WebSocket');
        stompClient.subscribe(`/user/${currentUser.username}/queue/messages`, function(message) {
            const msg = JSON.parse(message.body);
            displayMessage(msg, false);
        });
    });
}

function sendMessage() {
    if (!stompClient || !stompClient.connected) {
        alert("WebSocket не подключен.");
        return;
    }
    const input = document.getElementById('messageInput');
    const content = input.value.trim();
    if (content === '' || !selectedFriend) return;
    const message = {
        recipientId: selectedFriend.id,
        content: content
    };
    stompClient.send("/app/chat", {}, JSON.stringify(message));
    displayMessage({
        sender: currentUser,
        content: content,
        timestamp: new Date()
    }, true);
    input.value = '';
}

function displayMessage(message, isMe) {
    const messagesContainer = document.getElementById('messages');
    if (messagesContainer.querySelector('.text-muted')) {
        messagesContainer.innerHTML = '';
    }
    const messageElement = document.createElement('div');
    messageElement.className = `mb-3 ${isMe ? 'text-end' : 'text-start'}`;
    messageElement.innerHTML = `
        <div class="d-inline-block p-2 rounded ${isMe ? 'bg-primary text-white' : 'bg-light'}">
            ${message.content}
        </div>
        <div class="small text-muted">${new Date(message.timestamp).toLocaleString()}</div>
    `;
    messagesContainer.appendChild(messageElement);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}
