-- Создание таблицы пользователей
CREATE TABLE usr
(
    id           SERIAL PRIMARY KEY,
    active       BOOLEAN             NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    username     VARCHAR(255) UNIQUE NOT NULL,
    friend_of_id BIGINT,
    CONSTRAINT fk_last_friend FOREIGN KEY (friend_of_id) REFERENCES usr (id)
);

-- Создание таблицы ролей пользователя (user_role)
CREATE TABLE user_role
(
    user_id BIGINT       NOT NULL,
    roles   VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE
);

-- Создание таблицы связи друзей (user_friends)
CREATE TABLE user_friends
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_user_friends_user FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_friends_friend FOREIGN KEY (friend_id) REFERENCES usr (id) ON DELETE CASCADE
);

-- Создание таблицы сообщений (chat_messages)
CREATE TABLE chat_messages
(
    id           SERIAL PRIMARY KEY,
    sender_id    BIGINT    NOT NULL,
    recipient_id BIGINT    NOT NULL,
    content      TEXT      NOT NULL,
    timestamp    TIMESTAMP NOT NULL,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES usr (id),
    CONSTRAINT fk_recipient FOREIGN KEY (recipient_id) REFERENCES usr (id)
);
