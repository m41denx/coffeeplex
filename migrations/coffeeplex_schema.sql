
CREATE TABLE IF NOT EXISTS `users`
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    username     TEXT    NOT NULL,
    passwordHash TEXT    NOT NULL,
    gender_i     INTEGER NOT NULL,
    age          INTEGER NOT NULL,
    zodiacSign_i INTEGER NOT NULL,
    bio          TEXT    NOT NULL
);

CREATE TABLE IF NOT EXISTS `relationships`
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    requestId INTEGER NOT NULL,
    targetId  INTEGER NOT NULL,
    relType   TINYINT NOT NULL,
    FOREIGN KEY (requestId) REFERENCES users (id),
    FOREIGN KEY (targetId) REFERENCES users (id),
    CHECK (requestId <> targetId)
    );

CREATE TABLE IF NOT EXISTS `photos`
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    ownerId   INTEGER NOT NULL,
    caption   TEXT    NOT NULL,
    image     LONGBLOB    NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ownerId) REFERENCES users (id)
    );


CREATE TABLE IF NOT EXISTS `likes`
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    userId   INTEGER NOT NULL,
    photoId  INTEGER NOT NULL,
    likeType TINYINT NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS `comments`
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    userId  INTEGER NOT NULL,
    photoId INTEGER NOT NULL,
    comment TEXT    NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (id),
    FOREIGN KEY (photoId) REFERENCES photos (id)
    );