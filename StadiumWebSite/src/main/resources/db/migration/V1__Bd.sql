CREATE TABLE Refreshment(
    ID INTEGER NOT NULL,
    Attendance FLOAT NOT NULL,
    Localisation VARCHAR(100) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE Athletic(
    NFC VARCHAR(50) NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    Age INTEGER NOT NULL,
    Sex VARCHAR(10) NOT NULL,
    Password VARCHAR(50) NOT NULL,
    PRIMARY KEY(NFC)
);

CREATE TABLE Race(
    Id_Score INTEGER NOT NULL,
    NFC VARCHAR(50) NOT NULL,
    PRIMARY KEY (Id_Score),
    FOREIGN KEY (NFC) REFERENCES Athletic(NFC)
);

CREATE TABLE Lap(
    ID INTEGER NOT NULL,
    Temp TIME NOT NULL,
    Temp_Ms INTEGER NOT NULL,
    Id_Score INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (Id_Score) REFERENCES Race(Id_Score)
);

CREATE TABLE Team(
    Id_Team INTEGER NOT NULL,
    Team_Name VARCHAR(50) NOT NULL,
    PRIMARY KEY (Id_Team)
);

CREATE TABLE Plays_In(
    NFC VARCHAR(50) NOT NULL,
    Id_Team INTEGER NOT NULL,
    PRIMARY KEY (NFC,Id_Team),
    FOREIGN KEY (NFC) REFERENCES Athletic(NFC),
    FOREIGN KEY (Id_Team) REFERENCES Team(Id_Team)
);

CREATE TABLE Matchs(
    Id_Match INTEGER NOT NULL,
    Id_Team_1 INTEGER NOT NULL,
    Id_Team_2 INTEGER NOT NULL,
    Goal_1 INTEGER NOT NULL,
    Goal_2 INTEGER NOT NULL,
    MatchDate DATETIME NOT NULL,
    Ended BOOLEAN NOT NULL,
    PRIMARY KEY (Id_Match),
    FOREIGN KEY (Id_Team_1) REFERENCES Team(Id_Team),
    FOREIGN KEY (Id_Team_2) REFERENCES Team(Id_Team)
);

CREATE TABLE Tribune(
    NFC VARCHAR(50) NOT NULL,
    Places INTEGER NOT NULL,
    Localisation VARCHAR(20) NOT NULL,
    Texte_Explanation VARCHAR(100) NOT NULL,
    PRIMARY KEY(NFC)
);

CREATE TABLE Seat(
    ID INTEGER NOT NULL,
    TribuneNFC VARCHAR(50) NOT NULL,
    Occupied BOOLEAN NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (TribuneNFC) REFERENCES Tribune(NFC)
);

CREATE TABLE Spectator(
    Id_Spec INTEGER NOT NULL,
    LastName VARCHAR(30) NOT NULL,
    FirstName VARCHAR(30) NOT NULL,
    TribuneNFC VARCHAR(50) NOT NULL,
    Id_Match INTEGER NOT NULL,
    PRIMARY KEY (Id_Spec),
    FOREIGN KEY (TribuneNFC) REFERENCES Tribune(NFC),
    FOREIGN KEY (Id_Match) REFERENCES Matchs(Id_Match)
);

INSERT INTO `Refreshment` (`ID`, `Attendance`, `Localisation`) VALUES (1, 0.35, 'C''est pas ici');
INSERT INTO `Refreshment` (`ID`, `Attendance`, `Localisation`) VALUES (2, 0.75, 'C''est pas ici non plus');