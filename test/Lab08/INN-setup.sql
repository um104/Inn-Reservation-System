--Michael Gage
--mgage@calpoly.edu

CREATE TABLE Rooms(
   RoomID CHAR(3) PRIMARY KEY,
   roomName VARCHAR(30) UNIQUE,
   beds INT,
   bedType VARCHAR(10),
   maxOccupancy INT,
   basePrice INT,
   decor VARCHAR(15)
);

CREATE TABLE Reservations(
   Code INT PRIMARY KEY,
   Room CHAR(3) REFERENCES Rooms,
   CheckIn DATE,
   CheckOut DATE,
   Rate FLOAT,
   LastName VARCHAR(15),
   FirstName VARCHAR(15),
   Adults INT,
   Kids INT,
   UNIQUE(Room, CheckIn)
);
