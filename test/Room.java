
/**
 * Halli Meth, Mark Lerner, Michael Gage
 * CPE 365
 * Spring 2011
 * Lab 07
 */
import java.sql.*;
import java.util.*;

public class Room{

    String roomID;
    String roomName;
    int beds;
    String bedType;
    int maxOccupancy;
    int basePrice;
    String decor;

    public Room(){
        roomID = roomName = bedType = decor = null;
    }

    public String toString(){
        return (roomID + ", " + roomName + ", " + beds + ", " + bedType + ", " + maxOccupancy + ", " + basePrice + ", " + decor);
    }
}
