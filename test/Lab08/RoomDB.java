
/**
 * Halli Meth, Mark Lerner, Michael Gage
 * CPE 365
 * Spring 2011
 * Lab 07
 */
import java.sql.*;
import java.util.*;
import java.io.*;

public class RoomDB{

    /**
     * Checks to see if the Rooms table exists
     *
     * @param   conn    Connection to database
     * @return      positive if it exists, negative if it doesn't exist
     */
    public static int exist(Connection conn){
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT COUNT(*) FROM Rooms");
            boolean f = result.next();
            int count = result.getInt("COUNT(*)");
            result.close();
	    s.close();
            return count;
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
            return -1;
        }
    }

    /**
     * Creates the table, with the order of attributes being : RoomID, RoomName, Beds, BedType, MaxOccupancy, BasePrice, Decor.
     *
     * @param   conn    Connection to database
     */
    public static void create(Connection conn){
	Statement s = null;
        try{
            s = conn.createStatement();
            String test = "CREATE TABLE Rooms (";
            test = test + "RoomID CHAR(3) PRIMARY KEY, roomName VARCHAR2(30) UNIQUE,";
            test = test + "beds INT, bedType VARCHAR(10), ";
            test = test + "maxOccupancy INT, basePrice INT, decor VARCHAR2(15))";
            s.executeUpdate(test);
	    s.close();
	}
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
        }
    }

    /**
     * Delete every tuple from the Rooms table
     *
     * @param   conn    Connection to database
     */
    public static void clear(Connection conn){
	Statement s = null;
        try{
            s = conn.createStatement();
            s.executeUpdate("DELETE FROM Rooms");
	    s.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
        }
    }

    /**
     * Completely drops the Rooms table
     *
     * @param   conn    Connection to database
     */
    public static void drop(Connection conn){
	Statement s = null;
        try{
            s = conn.createStatement();
            s.executeUpdate("DROP TABLE Rooms");
	    s.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
        }
    }

    public static Vector<Vector> display(ArrayList<Room> rooms){
        Vector<Vector> ret = new Vector<Vector>();
        if(rooms != null){
            for(int i = 0; i < rooms.size(); i++){
                Vector<Object> temp = new Vector<Object>();
                temp.add(rooms.get(i).roomID);
                temp.add(rooms.get(i).roomName);
                temp.add(rooms.get(i).beds);
                temp.add(rooms.get(i).bedType);
                temp.add(rooms.get(i).maxOccupancy);
                temp.add(rooms.get(i).basePrice);
                temp.add(rooms.get(i).decor);
                ret.add(temp);
            }
        }
        return ret;
    }

    public static Vector<Vector> getRooms(Connection conn){
        Vector<Vector> rooms = null;
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT roomName FROM Rooms ORDER BY roomName");
            rooms = new Vector<Vector>();
            boolean f = result.next();
            while(f){
                Vector<String> temp = new Vector<String>();
                temp.add(result.getString("roomName"));
                rooms.add(temp);
                f = result.next();
            }
	    s.close();
	    result.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return rooms;
    }

    /**
     * Gets all the info from the Rooms table
     *
     * @param   conn    Connection to database
     * @return      ArrayList of Rooms filled with all of the data for that Room
     */
    public static ArrayList<Room> getAll(Connection conn){
        ArrayList<Room> rooms = null;
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Rooms ORDER BY roomName");
            rooms = new ArrayList<Room> ();
            boolean f = result.next();
            while(f){
                rooms.add(buildRoom(result));
                f = result.next();
            }
	    result.close();
	    s.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return rooms;
    }

    public static Room getDet(Connection conn, String roomName)
    {
	Statement s = null;
	ResultSet result = null;
        Room ret = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Rooms WHERE roomName = '" + roomName + "'");
            boolean f = result.next();
            ret = buildRoom(result);
	    s.close();
	    result.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return ret;
    }
    
    /**
     * Gets all the detail on a specific room, by RoomID
     *
     * @param   conn    Connection to database
     * @param   RoomID  Unique RoomID for that room
     * @return        Returns the room information in a Rooom object
     */
    public static Room getDetail(Connection conn, String roomID)
    {
	Statement s = null;
	ResultSet result = null;
        Room ret = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Rooms WHERE RoomID = '" + roomID + "'");
            boolean f = result.next();
            ret = buildRoom(result);
	    s.close();
	    result.close();
        }
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return ret;
    }

    /**
     * Builds a room based on a ResultSet, and returns that Room object filled in.
     *
     * @param   result    ResultSet from a SQL query on the Rooms table
     * @return        Returns a Room object filled in
     */
    public static Room buildRoom(ResultSet result) throws SQLException{
        Room temp = new Room();

        temp = new Room();
        temp.roomID = result.getString("RoomID");
        temp.roomName = result.getString("roomName");
        temp.beds = result.getInt("beds");
        temp.bedType = result.getString("bedType");
        temp.maxOccupancy = result.getInt("maxOccupancy");
        temp.basePrice = result.getInt("basePrice");
        temp.decor = result.getString("decor");
        return temp;
    }

    public static Vector<Vector> getResRate(Connection conn, String start, String end){
        Vector<Vector> rates = new Vector<Vector>();
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT roomName, roomID FROM Rooms WHERE roomID IN ((SELECT roomID FROM Rooms) MINUS (SELECT DISTINCT RV.Room FROM Reservations RV WHERE (RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckIn < TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (RV.CheckOut > TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (RV.CheckIn < TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckOut > TO_DATE('" + end + "', 'YYYY-MM-DD'))))");
            boolean f = result.next();
            java.sql.Date st = java.sql.Date.valueOf(start);
            java.sql.Date en = java.sql.Date.valueOf(end);
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(result.getString("roomName"));
                String roomID = result.getString("roomID");
                temp.add(ResDB.avgRate(ResDB.getRates(conn, st, en, roomID)));
                rates.add(temp);
                f = result.next();
            }
	    s.close();
	    result.close();
	}
        catch(SQLException ex){
	    try{ s.close();}
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return rates;
    }
}
