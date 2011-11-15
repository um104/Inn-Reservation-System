CC = javac

CFLAGS = -Xlint

all: InnReservations

clean:
	rm -f *.class

test: all
	java -cp ojdbc14.jar:. InnReservations

InnReservations: InnReservations.class ResDB.class RoomDB.class Reservation.class Room.class ImageOnBackground.class Occupancy.class

InnReservations.class: InnReservations.java
	$(CC) InnReservations.java

ResDB.class: ResDB.java
	$(CC) ResDB.java

RoomDB.class: RoomDB.java
	$(CC) RoomDB.java

Reservation.class: Reservation.java
	$(CC) Reservation.java

Room.class: Room.java
	$(CC) Room.java

ImageOnBackground.class: ImageOnBackground.java
	$(CC) ImageOnBackground.java

Occupancy.class: Occupancy.java
	$(CC) Occupancy.java
