CREATE TABLE "password"
(
    ID INT NOT NULL PRIMARY KEY,
    USERID VARCHAR(255) NOT NULL,
    USERNAME VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    URL VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    DELETED BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (ID)
)
