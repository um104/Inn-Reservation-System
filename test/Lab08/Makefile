#Halli Meth
#Mark Lerner
#Michael Gage
#CPE 365-01
#Spring 2011

JAVAC = javac

JAVAFLAGS = -Xlint

all: InnReservations

test: all run clean

clean:
	rm -f *.class

run: 
	java -cp ojdbc14.jar:. InnReservations

InnReservations: InnReservations.class gui.class ResDB.class RoomDB.class Reservation.class Room.class

InnReservations.class: InnReservations.java
	$(JAVAC) InnReservations.java

gui.class: gui.java 
	$(JAVAC) gui.java

ResDB.class: ResDB.java
	$(JAVAC) ResDB.java

RoomDB.class: RoomDB.java
	$(JAVAC) RoomDB.java

Reservation.class: Reservation.java
	$(JAVAC) Reservation.java

Room.class: Room.java
	$(JAVAC) Room.java
