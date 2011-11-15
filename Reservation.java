import java.sql.*;
import java.util.*;

public class Reservation{

    int code;
    String room;
    java.sql.Date checkIn;
    java.sql.Date checkOut;
    float rate;
    String lastName;
    String firstName;
    int adults;
    int kids;

    public Reservation(){
        room = lastName = firstName = null;
        checkIn = checkOut = null;
    }

    public String toString(){
        return (code + ", " + room + ", " + checkIn.toString() + ", " + checkOut.toString() + ", " + rate + ", " + lastName + ", " + firstName + ", " + adults + ", " + kids);
    }
} 
