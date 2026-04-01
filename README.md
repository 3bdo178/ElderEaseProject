Create database ElderEasedb
Use ElderEasedb
go
-- Senior Table
CREATE TABLE Senior (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Email  NVARCHAR(100) Unique,
    Password NVARCHAR(255),
    Phone NVARCHAR(50),
    Location NVARCHAR(255),
    EmergencyContactName nvarchar(100),
    EmergencyContactPhone nvarchar(50),
    DOB Date
);

-- Caregiver Table
CREATE TABLE Caregiver (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Email NVARCHAR(100) Unique,
    Password NVARCHAR(255),
    Phone NVARCHAR(20)
);

-- Relation between Senior & Caregiver
CREATE TABLE SeniorCaregiver (
    SeniorId INT,
    CaregiverId INT,
    Relation nvarchar(50),
    PRIMARY KEY (SeniorId, CaregiverId),
    FOREIGN KEY (SeniorId) REFERENCES Senior(Id),
    FOREIGN KEY (CaregiverId) REFERENCES Caregiver(Id)
);
--Status type table (Lockup table)
Create table StatusType(
id int primary key identity(1,1),
Name nvarchar(50),
);
-- Repeat Type (Lockup Table)
CREATE TABLE RepeatType (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(50)
);
-- Reminder Table
CREATE TABLE Reminder (
    Id INT PRIMARY KEY IDENTITY(1,1),
    SeniorId INT,
    RepeatTypeId INT,
    Title NVARCHAR(100),
    Description NVARCHAR(255) Null,
    StatusTypeId int,
    Date Date,
    RemindAt Time,
    Foreign key (StatusTypeId) references StatusType(Id),
    FOREIGN KEY (SeniorId) REFERENCES Senior(Id),
    FOREIGN KEY (RepeatTypeId) REFERENCES RepeatType(Id)
);
-- Detail Type
CREATE TABLE DetailType (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(50)
);
-- Health Entry
CREATE TABLE HealthEntry (
    Id INT PRIMARY KEY IDENTITY(1,1),
    SeniorId INT,
    Title NVARCHAR(100),
    Notes NVARCHAR(255) Null,
    Time TIME,
    Date DATE,
    FOREIGN KEY (SeniorId) REFERENCES Senior(Id)
);
-- Details
CREATE TABLE Details (
    Id INT PRIMARY KEY IDENTITY(1,1),
    DetailTypeId INT,
    Value NVARCHAR(50),
    HealthEntryId int,
    Foreign Key (HealthEntryId) references HealthEntry(Id),
    FOREIGN KEY (DetailTypeId) REFERENCES DetailType(Id)
);
-- Exercises
CREATE TABLE Exercises (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100),
    Description NVARCHAR(255),
    Duration INT,
    Category NVARCHAR(50),
    DifficultyLevel NVARCHAR(50)
);
-- Daily Routine
CREATE TABLE DailyRoutine (
    Id INT PRIMARY KEY IDENTITY(1,1),
    SeniorId INT,
    Title NVARCHAR(100),
    Type NVARCHAR(50),
    Date DATE,
    IsDone Bit,
    FOREIGN KEY (SeniorId) REFERENCES Senior(Id)
);
Insert into StatusType(Name)Values ('Done'),('InProgress'),('Missed')
Insert into RepeatType(Name)Values('Daily'),('Weekly'),('Monthly'),('Yearly')
Insert into DetailType(Name)Values('Sugar Level'),('Blood Pressure Systolic'),('Blood Pressure Diastolic'),('Heart Rate'),('Body Temperature'),('Weight'),('Oxygen Saturation'),('Cholesterol')
