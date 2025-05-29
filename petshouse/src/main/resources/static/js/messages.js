document.addEventListener('DOMContentLoaded', () => {
    const currentUserId = parseInt(document.body.getAttribute('data-owner-id'));
    console.log('Current User ID:', currentUserId);
    if (!currentUserId) {
        console.warn('User is not logged in');
        return;
    }

    const isUserLoggedIn = document.body.getAttribute('data-logged-in');

        // AUTH UI
    document.querySelectorAll('.auth-buttons a').forEach(link => {
        link.style.display = isUserLoggedIn ? 'none' : 'inline-block';
    });
    const logoutBtn = document.getElementById('logoutButton');
    if (logoutBtn) logoutBtn.style.display = isUserLoggedIn ? 'inline-block' : 'none';

    logoutBtn?.addEventListener('click', () => {
        fetch('/api/auth/logout', { method: 'POST', credentials: 'include' })
        .then(res => {
            if (res.ok) window.location.href = '/main';
            else showErrorMessage('Ошибка при выходе из системы');
        })
        .catch(() => showErrorMessage('Ошибка при выходе из системы'));
    }); 

    const mainContent = document.querySelector('.main-content');
    mainContent.innerHTML = `
        <div class="dialogs-list"></div>
        <div class="chat-window" style="display:none;">
        <h3 class="chat-with"></h3>
        <div class="messages-list"></div>
        <form id="messageForm">
            <input type="text" id="messageInput" placeholder="Write a message..." autocomplete="off" required />
            <button type="submit">Send</button>
        </form>
        </div>
    `;

    const dialogsList = mainContent.querySelector('.dialogs-list');
    const chatWindow = mainContent.querySelector('.chat-window');
    const chatWith = chatWindow.querySelector('.chat-with');
    const messagesList = chatWindow.querySelector('.messages-list');
    const messageForm = chatWindow.querySelector('#messageForm');
    const messageInput = chatWindow.querySelector('#messageInput');

  // Загрузить список диалогов
    async function loadDialogs() {
        try {
        const res = await fetch('/api/messages/dialogs');
        if (!res.ok) throw new Error(`Failed to fetch dialogs: ${res.status}`);
        const dialogs = await res.json();

        dialogsList.innerHTML = '';
        if (dialogs.length === 0) {
            dialogsList.innerHTML = '<p>No dialogs found.</p>';
            chatWindow.style.display = 'none';
            return;
        }

            dialogsList.innerHTML = '';

            dialogs.forEach(dialog => {
                const existing = dialogsList.querySelector(`.dialog-item[data-user-id="${dialog.otherUserId}"]`);
                const displayText = `${dialog.otherUserLogin}`;

                if (existing) {
                    existing.textContent = displayText;
                    existing.style.textAlign = 'center';
                } else {
                    const div = document.createElement('div');
                    div.className = 'dialog-item';
                    div.dataset.userId = dialog.otherUserId;
                    div.style.textAlign = 'center';
                    div.textContent = displayText;
                    div.addEventListener('click', () => {
                    dialogsList.querySelectorAll('.dialog-item').forEach(el => el.classList.remove('active'));
                    div.classList.add('active');
                    openDialog(dialog.otherUserId, dialog.otherUserLogin);
                    });
                    dialogsList.appendChild(div);
                }
            });
        } catch (err) {
        console.error(err);
        dialogsList.innerHTML = '<p>Error loading dialogs.</p>';
        }
    }

    let currentDialogUserId = null;
    let currentDialogUserLogin = null;

    async function openDialog(otherUserId, otherUserLogin) {
        currentDialogUserId = otherUserId;
        currentDialogUserLogin = otherUserLogin;
        chatWith.textContent = `Chat with ${otherUserLogin}`;
        chatWindow.style.display = 'block';
        await loadConversation(currentUserId, currentDialogUserId);
    }

    async function loadConversation(userId1, userId2) {
        try {
            const res = await fetch('/api/messages/conversation', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId1, userId2 })
            });

            if (!res.ok) throw new Error(`Failed to fetch conversation: ${res.status}`);
            const messages = await res.json();

            messagesList.innerHTML = '';

            messages.forEach(msg => {
                const senderId = msg.senderId;
                const senderLogin = (senderId === currentUserId) ? 'You' : currentDialogUserLogin;
                const div = document.createElement('div');
                div.className = 'message ' + (senderId === currentUserId ? 'sent' : 'received');
                div.innerHTML = `
                    <span class="sender">${senderLogin}</span>
                    <span class="text">${escapeHtml(msg.messageText)}</span>
                    <span class="timestamp">${formatDate(msg.messageTimeStamp)}</span>
                `;
                messagesList.appendChild(div);
            });
            messagesList.scrollTop = messagesList.scrollHeight;
        } catch (err) {
            console.error(err);
            messagesList.innerHTML = '<p>Error loading conversation.</p>';
        }
    }

  // Отправка сообщения
    messageForm.addEventListener('submit', async e => {
        e.preventDefault();
        if (!currentDialogUserId) return;

        const text = messageInput.value.trim();
        if (!text) return;

        const requestBody = {
        senderId: currentUserId,
        receiverId: currentDialogUserId,
        petId: 1, // FIXME: подставить реальный petId, возможно с UI или контекста
        messageText: text
        };

        try {
        const res = await fetch('/api/messages', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        if (!res.ok) throw new Error(`Failed to send message: ${res.status}`);
        messageInput.value = '';
        await loadConversation(currentUserId, currentDialogUserId);
        await loadDialogs();
        } catch (err) {
        alert(`Error sending message: ${err.message}`);
        }
    });

    // Форматируем дату в читаемый вид
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }

    // Безопасное экранирование текста (XSS)
    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    // Запускаем загрузку диалогов при старте
    loadDialogs();
});