let currentUser = null;
let selectedFriend = null;
let stompClient = null;

// 1. Загружаем текущего пользователя и подключаем WebSocket
document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/current-user')
        .then(response => response.json())
        .then(user => {
            currentUser = user;
            connectWebSocket();
            loadFriends();
        })
        .catch(error => {
            console.error('Ошибка при получении текущего пользователя:', error);
        });
});

// 2. Установка WebSocket-соединения
function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log('🟢 WebSocket подключен');

        // 3. Подписка на сообщения
        stompClient.subscribe(`/user/${currentUser.id}/queue/messages`, function (message) {
            const msg = JSON.parse(message.body);
            const isMe = msg.senderId === currentUser.id;

            // 4. Показываем сообщение, если оно от выбранного друга
            if (
                selectedFriend &&
                (msg.senderId === selectedFriend.id || msg.receiverId === selectedFriend.id)
            ) {
                displayMessage(msg, isMe);
            }
        });
    });
}

// 5. Выбор друга
function selectFriend(event, friendId, friendName) {
    selectedFriend = { id: friendId, username: friendName };

    document.querySelectorAll('.friend-item').forEach(item => item.classList.remove('active'));
    event.currentTarget.classList.add('active');

    document.getElementById('messageInput').disabled = false;
    document.getElementById('sendButton').disabled = false;

    loadMessages(friendId);
}

// 6. Загрузка сообщений
function loadMessages(friendId) {
    const container = document.getElementById('messages');
    container.innerHTML = 'Загрузка...';

    fetch(`/api/messages/${friendId}`)
        .then(r => r.json())
        .then(messages => {
            container.innerHTML = '';
            if (messages.length === 0) {
                container.innerHTML = '<div class="text-muted">Нет сообщений</div>';
                return;
            }

            messages.forEach(msg => {
                displayMessage(msg, msg.senderId === currentUser.id);
            });
        });
}

// 7. Отправка сообщения
function sendMessage() {
    const input = document.getElementById('messageInput');
    const content = input.value.trim();
    if (content === '') return;

    const message = {
        senderId: currentUser.id,
        receiverId: selectedFriend.id,
        content: content,
        timestamp: new Date()
    };

    stompClient.send('/app/chat', {}, JSON.stringify(message));
    displayMessage(message, true);
    input.value = '';
}

// 8. Загрузка списка друзей
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
        .catch(error => {
            console.error('Ошибка при загрузке списка друзей:', error);
            friendsContainer.innerHTML = '<div class="alert alert-danger">Ошибка загрузки друзей</div>';
        });
}

// 9. Отображение сообщений
function displayMessage(message, isMe) {
    const container = document.getElementById('messages');

    const div = document.createElement('div');
    div.className = `mb-2 ${isMe ? 'text-end' : 'text-start'}`;
    div.innerHTML = `
        <div class="d-inline-block p-2 rounded ${isMe ? 'bg-primary text-white' : 'bg-light'}">
            ${message.content}
        </div>
        <div class="small text-muted">${new Date(message.timestamp).toLocaleString()}</div>
    `;
    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
}
