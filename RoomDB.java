import java.sql.*;
import java.util.*;
import java.io.*;
import java.lang.*;

/**
 * Class to organize Room Information for hotel reservation database system.
 * 
 * @author Halli Meth
 * @version 1
 */
public class RoomDB
{
    // instance variables
    public static final int RoomParams = 8;

    /**
     * Checks to see if the Rooms table exists
     * 
     * @param   conn    Connection to database
     * @return      positive if it exists, negative if it doesn't exist
     */
    public static int exist(Connection conn){
        try{
            Statement s = conn.createStatement();
            ResultSet result = s.executeQuery("SELECT COUNT(*) FROM Rooms");
            boolean f = result.next();
            return result.getInt(1);
        }
        catch(SQLException ex){
            return -1;
        }
    }

    /**
     * deletes every tuple from the Rooms table
     * 
     * @param   conn    Connection to DB
     */
    public static void clear(Connection conn){
        try{
            Statement s = conn.createStatement();
            s.executeUpdate("DELETE FROM Rooms");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }

    /**
     * Completely drops the Rooms table
     * 
     * @param   conn    Connection to DB
     */
    public static void drop(Connection conn){
        try{
            Statement s = conn.createStatement();
            s.executeUpdate("DROP TABLE Rooms");
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }

    /**
     * Creates the table, with the order of attributes being: RoomID, RoomName, Beds, BedType, MaxOccupancy, BasePrice, Decor.
     * 
     * @param   conn    Connection to database
     */
    public static void create(Connection conn){
        try{
            Statement s = conn.createStatement();
            String test = "CREATE TABLE Rooms ( RoomID CHAR(3) PRIMARY KEY, ";
            test = test + "RoomName VARCHAR(30) UNIQUE, beds INT, ";
            test = test + "bedType VARCHAR(10), maxOccupancy INT, ";
            test = test + "basePrice INT, decor VARCHAR(15))";
            s.executeUpdate(test);
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
    }

    /**
     * Gets all the info from all the rooms
     * 
     * @param   conn    Connection to DB
     * @return      ArrayList of Rooms filled with all of the data for that Room
     */
    public static ArrayList<Room> getAll(Connection conn){
        ArrayList<Room> rooms = new ArrayList<Room>();
        try{
            Statement s = conn.createStatement();
            ResultSet result = s.executeQuery("SELECT * FROM Rooms");
            boolean f = result.next();
            while(f){
                rooms.add(buildRoom(result));
                f = result.next();
            }
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        return rooms;
    }

    private static Room buildRoom(ResultSet result) throws SQLException
    {
	Room temp = new Room();
        temp.roomID = result.getString("RoomID");
        temp.roomName = result.getString("roomName");
        temp.beds = result.getInt("beds");
        temp.bedType = result.getString("bedType");
        temp.maxOccupancy = result.getInt("maxOccupancy");
        temp.basePrice = result.getInt("basePrice");
        temp.decor = result.getString("decor");
        return temp;
    }

    /**
     * comment
     * 
     * @return     the list of roomnames 
     */
    public static ArrayList<String> getList(Connection conn)
    {
        ArrayList<String> Rooms = new ArrayList<String>();
        try{
           Statement s = conn.createStatement();
           ResultSet result = s.executeQuery("SELECT roomName FROM Rooms");
           boolean f = result.next();
           while(f)
           {
              String str = result.getString("roomName");
              Rooms.add(str);
              f = result.next();
           }
        }
        catch(SQLException E) {System.out.println(E);}
        return Rooms;
    }
    
    /**
     * COMMENT
     * 
     * @return the list of all information for each room
     */
    public static Room getDetail(Connection conn, String roomID)
    {
        Room room = new Room();
        try{
           Statement s = conn.createStatement();
           ResultSet result = s.executeQuery("SELECT * FROM Rooms WHERE RoomID = '" + roomID + "'");
           boolean f = result.next();
           room = buildRoom(result);
        }
        catch(SQLException E) {System.out.println(E);}
        return room;
    }    
}
