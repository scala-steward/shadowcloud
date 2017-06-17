CREATE TABLE DATABASECHANGELOG
(
  ID            VARCHAR(255) NOT NULL,
  AUTHOR        VARCHAR(255) NOT NULL,
  FILENAME      VARCHAR(255) NOT NULL,
  DATEEXECUTED  TIMESTAMP    NOT NULL,
  ORDEREXECUTED INTEGER      NOT NULL,
  EXECTYPE      VARCHAR(10)  NOT NULL,
  MD5SUM        VARCHAR(35),
  DESCRIPTION   VARCHAR(255),
  COMMENTS      VARCHAR(255),
  TAG           VARCHAR(255),
  LIQUIBASE     VARCHAR(20),
  CONTEXTS      VARCHAR(255),
  LABELS        VARCHAR(255),
  DEPLOYMENT_ID VARCHAR(10)
);

CREATE TABLE DATABASECHANGELOGLOCK
(
  ID          INTEGER PRIMARY KEY NOT NULL,
  LOCKED      BOOLEAN             NOT NULL,
  LOCKGRANTED TIMESTAMP,
  LOCKEDBY    VARCHAR(255)
);

CREATE TABLE SC_AKKA_JOURNAL
(
  PERSISTENCE_ID VARCHAR(255)          NOT NULL,
  SEQUENCE_NR    BIGINT                NOT NULL,
  ORDERING       BIGINT AUTO_INCREMENT NOT NULL,
  TAGS           ARRAY                 NOT NULL,
  MESSAGE        VARBINARY             NOT NULL,
  PRIMARY KEY (PERSISTENCE_ID, SEQUENCE_NR)
);

CREATE TABLE SC_AKKA_SNAPSHOTS
(
  PERSISTENCE_ID VARCHAR   NOT NULL,
  SEQUENCE_NR    BIGINT    NOT NULL,
  TIMESTAMP      BIGINT    NOT NULL,
  SNAPSHOT       VARBINARY NOT NULL,
  PRIMARY KEY (PERSISTENCE_ID, SEQUENCE_NR)
);
CREATE INDEX SNAPSHOT_INDEX
  ON SC_AKKA_SNAPSHOTS (PERSISTENCE_ID, SEQUENCE_NR DESC, TIMESTAMP DESC);

CREATE TABLE SC_KEYS
(
  KEY_ID         VARCHAR(36) PRIMARY KEY NOT NULL,
  FOR_ENCRYPTION BOOLEAN                 NOT NULL,
  FOR_DECRYPTION BOOLEAN                 NOT NULL,
  SERIALIZED_KEY VARBINARY               NOT NULL
);

INSERT INTO PUBLIC.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, EXECTYPE, MD5SUM, DESCRIPTION, COMMENTS, TAG, LIQUIBASE, CONTEXTS, LABELS, DEPLOYMENT_ID)
VALUES ('1', 'shadowcloud', 'B:/Data/IdeaProjects/shadowcloud/src/main/migrations/changelog.sql',
             '2017-06-18 00:24:35.851000000', 1, 'EXECUTED', '7:0bdaad0c90372c1238f0e873dc0f4309', 'sql', '', NULL,
             '3.5.3', NULL, NULL, '7734675318');
INSERT INTO PUBLIC.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, EXECTYPE, MD5SUM, DESCRIPTION, COMMENTS, TAG, LIQUIBASE, CONTEXTS, LABELS, DEPLOYMENT_ID)
VALUES ('2', 'shadowcloud', 'B:/Data/IdeaProjects/shadowcloud/src/main/migrations/changelog.sql',
             '2017-06-18 00:24:35.856000000', 2, 'EXECUTED', '7:5936bbdaa5d8803bee9adb9316b29d8c', 'sql', '', NULL,
             '3.5.3', NULL, NULL, '7734675318');
INSERT INTO PUBLIC.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, EXECTYPE, MD5SUM, DESCRIPTION, COMMENTS, TAG, LIQUIBASE, CONTEXTS, LABELS, DEPLOYMENT_ID)
VALUES ('3', 'shadowcloud', 'B:/Data/IdeaProjects/shadowcloud/src/main/migrations/changelog.sql',
             '2017-06-18 00:24:35.861000000', 3, 'EXECUTED', '7:7ded3f63857d4e43d7cc06c0d166dae2', 'sql', '', NULL,
             '3.5.3', NULL, NULL, '7734675318');