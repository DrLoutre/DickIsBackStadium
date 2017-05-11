CREATE TABLE Refreshment(
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    Attendance FLOAT NOT NULL,
    Localisation VARCHAR(100) NOT NULL
);

CREATE TABLE Athletic(
    NFC VARCHAR(50) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    Age INTEGER NOT NULL,
    Sex VARCHAR(10) NOT NULL,
    Password VARCHAR(500) NOT NULL,
    Salt VARCHAR(30) NOT NULL,
    PRIMARY KEY(NFC)
);

CREATE TABLE Race(
    Id_Score INTEGER PRIMARY KEY AUTO_INCREMENT,
    NFC VARCHAR(50) NOT NULL,
    FOREIGN KEY (NFC) REFERENCES Athletic(NFC)
);

CREATE TABLE Lap(
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    Temp DATETIME NOT NULL,
    Temp_Ms INTEGER NOT NULL,
    Id_Score INTEGER NOT NULL,
    IsBeginning BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (Id_Score) REFERENCES Race(Id_Score)
);

CREATE TABLE Team(
    Id_Team INTEGER PRIMARY KEY AUTO_INCREMENT,
    Team_Name VARCHAR(50) NOT NULL
);

CREATE TABLE Plays_In(
    NFC VARCHAR(50) NOT NULL,
    Id_Team INTEGER NOT NULL,
    PRIMARY KEY (NFC,Id_Team),
    FOREIGN KEY (NFC) REFERENCES Athletic(NFC),
    FOREIGN KEY (Id_Team) REFERENCES Team(Id_Team)
);

CREATE TABLE Matchs(
    Id_Match INTEGER PRIMARY KEY AUTO_INCREMENT,
    Id_Team_1 INTEGER NOT NULL,
    Id_Team_2 INTEGER NOT NULL,
    Goal_1 INTEGER NOT NULL,
    Goal_2 INTEGER NOT NULL,
    MatchDate DATETIME NOT NULL,
    Ended BOOLEAN NOT NULL,
    FOREIGN KEY (Id_Team_1) REFERENCES Team(Id_Team),
    FOREIGN KEY (Id_Team_2) REFERENCES Team(Id_Team)
);

CREATE TABLE Tribune(
    NFC INTEGER NOT NULL,
    Places INTEGER NOT NULL,
    Localisation VARCHAR(20) NOT NULL,
    Texte_Explanation VARCHAR(100) NOT NULL,
    PRIMARY KEY(NFC)
);

CREATE TABLE Seat(
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    TribuneNFC INTEGER NOT NULL,
    Occupied BOOLEAN NOT NULL,
    FOREIGN KEY (TribuneNFC) REFERENCES Tribune(NFC)
);

CREATE TABLE Spectator(
    Id_Spec INTEGER PRIMARY KEY AUTO_INCREMENT,
    LastName VARCHAR(30) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    TribuneNFC INTEGER NOT NULL,
    Id_Match INTEGER NOT NULL,
    FOREIGN KEY (TribuneNFC) REFERENCES Tribune(NFC),
    FOREIGN KEY (Id_Match) REFERENCES Matchs(Id_Match)
);

CREATE TABLE Tokens(
    NFC VARCHAR(50) NOT NULL,
    Token VARCHAR(50) NOT NULL,
    MatchDate DATETIME NOT NULL,
    PRIMARY KEY (NFC,Token),
    FOREIGN KEY (NFC) REFERENCES Athletic(NFC)
);

INSERT INTO `Refreshment` (`ID`, `Attendance`, `Localisation`) VALUES (1, 0.35, 'Buvette est');
INSERT INTO `Refreshment` (`ID`, `Attendance`, `Localisation`) VALUES (2, 0.75, 'Buvette ouest');
INSERT INTO `Athletic` (`NFC`,`LastName`,`FirstName`,`Age`,`Sex`,`Password`,`Salt`) VALUES ('5c005c9229', 'Dupont', 'Jean', 42, 'M', 'd58a13ba3e97a6e04c423be26c9a2408ecb87af8cd6c2651f046a7ceec3659cd7ff2318f5b364cbfc9981aea7156d76e3465c5c2ff2f683f83285b2fe236180e', 'DbuLwcUwtsFAdB2ShHK4d02B1CQj5M');
INSERT INTO `Tribune` (`NFC`,`Places`,`Localisation`,`Texte_Explanation`) VALUES (1, 4, 'Tribune Sud', 'Tribune sud');
INSERT INTO `Tribune` (`NFC`,`Places`,`Localisation`,`Texte_Explanation`) VALUES (2, 4, 'Tribune Nord', 'Tribune nord');
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (1,1,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (2,1,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (3,1,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (4,1,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (5,2,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (6,2,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (7,2,0);
INSERT INTO `Seat` (`ID`,`TribuneNFC`,`Occupied`) VALUES (8,2,0);
INSERT INTO `Athletic` (`NFC`, `LastName`, `FirstName`, `Age`, `Sex`, `Password`, `Salt`) VALUES ('1f00d061c8', 'Stadium', 'Dick', 30, 'H', 'd58a13ba3e97a6e04c423be26c9a2408ecb87af8cd6c2651f046a7ceec3659cd7ff2318f5b364cbfc9981aea7156d76e3465c5c2ff2f683f83285b2fe236180e', 'DbuLwcUwtsFAdB2ShHK4d02B1CQj5M');
INSERT INTO `Athletic` (`NFC`, `LastName`, `FirstName`, `Age`, `Sex`, `Password`, `Salt`) VALUES ('5c005c8aeb', 'Dupont', 'Jean', 45, 'H', '3f1e299c07e692700f851a3d61aec0605912a1891216814a730dbd87eb42a66e8fcbb83a796cbc57f2a73ed0f9d7f29962484a6c396f0c9ac2abc1f8b6cbefdb', 'I8hLOt92C1bsGjhCnIM3LCQBwp2YZ8');