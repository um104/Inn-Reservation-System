/**
 * Halli Meth, Mark Lerner, Michael Gage
 * CPE 365
 * Spring 2011
 * Lab 07
 */
import java.sql.*;
import java.util.*;
import java.io.*;

public class ResDB{

    public static final java.sql.Date SATURDAY = java.sql.Date.valueOf("2010-01-02");
    public static final java.sql.Date SUNDAY = java.sql.Date.valueOf("2010-01-03");
    public static final long Weekend1 = 1262433600000L;
    public static final long Weekend2 = 1262520000000L;
    public static final long Week = 604800000L;
    public static final long DAY = (24 * 60 * 60 * 1000);

    /**
     * Checks to see if the Reservations table exists
     * 
     * @param   conn    Connection to database
     * @return      positive if it exists, negative if it doesn't exist
     */
    public static int exist(Connection conn){
        Statement s = null;
        ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT COUNT(*) FROM Reservations");
            boolean f = result.next();
            int count = result.getInt("COUNT(*)");
            s.close();
            result.close();
            return count;
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
            try{
                result.close();
            }
            catch(Exception e){
            }
            return -1;
        }
    }

    /**
     * Creates the table, with the order of attributes being: Code, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids.
     * 
     * @param   conn    Connection to database
     */
    public static void create(Connection conn){
        Statement s = null;
        try{
            s = conn.createStatement();
            String test = "CREATE TABLE Reservations ( Code INT PRIMARY KEY, ";
            test = test + "Room CHAR(3) REFERENCES Rooms, CheckIn DATE, ";
            test = test + "CheckOut DATE, Rate FLOAT, LastName VARCHAR2(15), ";
            test = test + "Firstname VARCHAR2(15), Adults INT, Kids INT, ";
            test = test + "UNIQUE (Room, CheckIn))";
            s.executeUpdate(test);
            s.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
        }
    }

    /**
     * Deletes every tuple from the Reservations table
     * 
     * @param   conn    Connection to database
     */
    public static void clear(Connection conn){
        Statement s = null;
        try{
            s = conn.createStatement();
            s.executeUpdate("DELETE FROM Reservations");
            s.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
        }
    }

    
    /**
     * Completely drops the Reservations table
     * 
     * @param   conn    Connection to database
     */
    public static void drop(Connection conn){
        Statement s = null;
        try{
            s = conn.createStatement();
            s.executeUpdate("DROP TABLE Reservations");
            s.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
        }
    }

    public static Vector<Vector> display(ArrayList<Reservation> reserve){
        Vector<Vector> ret = new Vector<Vector>();
        if(reserve != null){
            for(int i = 0; i < reserve.size(); i++){
                Vector<Object> temp = new Vector<Object>();
                temp.add(reserve.get(i).code);
                temp.add(reserve.get(i).room);
                temp.add(reserve.get(i).checkIn);
                temp.add(reserve.get(i).checkOut);
                temp.add(reserve.get(i).rate);
                temp.add(reserve.get(i).lastName);
                temp.add(reserve.get(i).firstName);
                temp.add(reserve.get(i).adults);
                temp.add(reserve.get(i).kids);
                ret.add(temp);
            }
	}
        return ret;
    }

    /**
     * Gets all the info from the Reservations table
     * 
     * @param   conn    Connection to database
     * @return      ArrayList of Reservations filled with all of the data for that Reservation
     */
    public static ArrayList<Reservation> getAll(Connection conn){
        ArrayList<Reservation> rooms = null;
        Statement s = null;
        ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Reservations ORDER BY CheckIn, CheckOut");
            rooms = new ArrayList<Reservation> ();
            boolean f = result.next();
            while(f){
                rooms.add(buildReservation(result));
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
            try{
                result.close();
            }
            catch(Exception e){
            }
        }
        return rooms;
    }

    public static Reservation getDet(Connection conn, String room, java.sql.Date date){
        Reservation detail = null;
        Statement s = null;
        ResultSet result = null;
        try{
	    s = conn.createStatement();
	    result = s.executeQuery("SELECT * FROM Reservations RV WHERE RV.Room = '" + room + "' AND RV.CheckIn <= TO_DATE('" + date + "', 'YYYY-MM-DD') AND RV.CheckOut > TO_DATE('" + date + "', 'YYYY-MM-DD')");
            result.next();
            detail = buildReservation(result);
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
            try{
                result.close();
            }
            catch(Exception e){
            }
        }
        return detail;
    }

    /**
     * Gets all the detail on a specific reservation, by code
     * 
     * @param   conn    Connection to DB
     * @param   code    Unique code for that reservation
     * @return      Returns the reservation information in a Reservation object
     */
    public static Reservation getDetail(Connection conn, int code){
        Reservation detail = null;
        Statement s = null;
        ResultSet result = null;
        try{
	    s = conn.createStatement();
	    result = s.executeQuery("SELECT * FROM Reservations RV WHERE RV.Code = " + code);
            boolean f = result.next();
            detail = buildReservation(result);
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
            try{
                result.close();
            }
            catch(Exception e){
            }
        }
        return detail;
    }

    public static Vector<Vector> getRev(Connection conn){
        Vector<Vector> result = new Vector<Vector>();
        Statement s1, s2, s3, s4;
        s1 = s2 = s3 = s4 = null;
        ResultSet total, roomTotals, monthTotals, grid;
        total = roomTotals = monthTotals = grid = null;
        try{
            s1 = conn.createStatement();
            s2 = conn.createStatement();
            s3 = conn.createStatement();
            s4 = conn.createStatement();
            total = s1.executeQuery("SELECT SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV");
            total.next();
            roomTotals = s2.executeQuery("SELECT RV.Room, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY RV.Room ORDER BY RV.Room");
            roomTotals.next();
            monthTotals = s3.executeQuery("SELECT TO_CHAR(RV.CheckIn, 'MM') AS Month, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY TO_CHAR(RV.CheckIn, 'MM') ORDER BY Month");
            monthTotals.next();
            grid = s4.executeQuery("SELECT RV.Room, TO_CHAR(RV.CheckIn, 'MM') AS Month, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY RV.Room, TO_CHAR(RV.CheckIN, 'MM') ORDER BY RV.Room, Month");
            grid.next();
            for(int i = 0; i < 10; i++){
                Vector<Object> temp = new Vector<Object>();
                temp.add(roomTotals.getString("Room"));
                for(int j = 0; j < 12; j++){
                    temp.add(grid.getInt("Revenue"));
                    grid.next();
                }
                temp.add(roomTotals.getInt("Revenue"));
                roomTotals.next();
                result.add(temp);
            }
            Vector<Object> temp = new Vector<Object>();
            temp.add("TOT");
            for(int i = 0; i < 12; i++){
                temp.add(monthTotals.getInt("Revenue"));
                monthTotals.next();
            }
            temp.add(total.getInt("Revenue"));
            result.add(temp);
            s1.close();
            s2.close();
            s3.close();
            s4.close();
            total.close();
            roomTotals.close();
            monthTotals.close();
            grid.close();
        }
        catch(Exception ex){
            try{
                s1.close();
            }
            catch(Exception e){
            }
            try{
                s2.close();
            }
            catch(Exception e){
            }
            try{
                s3.close();
            }
            catch(Exception e){
            }
            try{
                s4.close();
            }
            catch(Exception e){
            }
            try{
                total.close();
            }
            catch(Exception e){
            }
            try{
                roomTotals.close();
            }
            catch(Exception e){
            }
            try{
                monthTotals.close();
            }
            catch(Exception e){
            }
            try{
                grid.close();
            }
            catch(Exception e){
            }
        }
        return result;
    }

    public static Vector<Vector> getRes(Connection conn){
        Vector<Vector> result = new Vector<Vector>();
        Statement s1, s2, s3, s4;
        s1 = s2 = s3 = s4 = null;
        ResultSet total, roomTotals, monthTotals, grid;
        total = roomTotals = monthTotals = grid = null;
        try{
            s1 = conn.createStatement();
            s2 = conn.createStatement();
            s3 = conn.createStatement();
            s4 = conn.createStatement();
            total = s1.executeQuery("SELECT COUNT(*) AS Res FROM Reservations RV");
            total.next();
            roomTotals = s2.executeQuery("SELECT RV.Room, COUNT(*) AS Res FROM Reservations RV GROUP BY RV.Room ORDER BY RV.Room");
            roomTotals.next();
            monthTotals = s3.executeQuery("SELECT TO_CHAR(RV.CheckIn, 'MM') AS Month, COUNT(*) AS Res FROM Reservations RV GROUP BY TO_CHAR(RV.CheckIn, 'MM') ORDER BY Month");
            monthTotals.next();
            grid = s4.executeQuery("SELECT RV.Room, TO_CHAR(RV.CheckIn, 'MM') AS Month, COUNT(*) AS Res FROM Reservations RV GROUP BY RV.Room, TO_CHAR(RV.CheckIN, 'MM') ORDER BY RV.Room, Month");
            grid.next();
            for(int i = 0; i < 10; i++){
                Vector<Object> temp = new Vector<Object>();
                temp.add(roomTotals.getString("Room"));
                for(int j = 0; j < 12; j++){
                    temp.add(grid.getInt("Res"));
                    grid.next();
                }
                temp.add(roomTotals.getInt("Res"));
                roomTotals.next();
                result.add(temp);
            }
            Vector<Object> temp = new Vector<Object>();
            temp.add("TOT");
            for(int i = 0; i < 12; i++){
                temp.add(monthTotals.getInt("Res"));
                monthTotals.next();
            }
            temp.add(total.getInt("Res"));
            result.add(temp);
            s1.close();
            s2.close();
            s3.close();
            s4.close();
            total.close();
            roomTotals.close();
            monthTotals.close();
            grid.close();
        }
        catch(Exception ex){
            try{
                s1.close();
            }
            catch(Exception e){
            }
            try{
                s2.close();
            }
            catch(Exception e){
            }
            try{
                s3.close();
            }
            catch(Exception e){
            }
            try{
                s4.close();
            }
            catch(Exception e){
            }
            try{
                total.close();
            }
            catch(Exception e){
            }
            try{
                roomTotals.close();
            }
            catch(Exception e){
            }
            try{
                monthTotals.close();
            }
            catch(Exception e){
            }
            try{
                grid.close();
            }
            catch(Exception e){
            }
        }
        return result;
    }

    /**
     * Gets the total revenue of the Inn, split by month and by room
     * 
     * @param   conn    Connection to DB
     * @return      11x13 array of doubles, containing the revenues for specific month/rom combos, as well as the totals.
     */
    public static double [][] getRevenue(Connection conn){
        double [][] result = new double[11][13];
	Statement s = null;
	ResultSet total, roomTotals, monthTotals, grid;
        total = roomTotals = monthTotals = grid = null;
        try{
            s = conn.createStatement();
            total = s.executeQuery("SELECT SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV");
            total.next();
            result[10][12] = total.getInt("Revenue");
            roomTotals = s.executeQuery("SELECT RV.Room, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY RV.Room ORDER BY RV.Room");
            int row = 0;
            int col = 12;
            boolean f = roomTotals.next();
            while(f){
                result[row][col] = roomTotals.getInt("Revenue");
                row++;
                f = roomTotals.next();
            }
            monthTotals = s.executeQuery("SELECT TO_CHAR(RV.CheckIn, 'MM') AS Month, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY TO_CHAR(RV.CheckIn, 'MM') ORDER BY Month");
            col = 0;
            f = monthTotals.next();
            while(f){
                result[row][col] = monthTotals.getInt("Revenue");
                col++;
                f = monthTotals.next();
            }
            grid = s.executeQuery("SELECT RV.Room, TO_CHAR(RV.CheckIn, 'MM') AS Month, SUM(RV.Rate * (RV.CheckOut - RV.CheckIn)) AS Revenue FROM Reservations RV GROUP BY RV.Room, TO_CHAR(RV.CheckIN, 'MM') ORDER BY RV.Room, Month");
            row = 0;
            col = 0;
            f = grid.next();
            while(f){
                while(f && col < 12){
                    result[row][col] = grid.getInt("Revenue");
                    col++;
                    f = grid.next();
                }
                col = 0;
                row++;
            }
            total.close();
            roomTotals.close();
            monthTotals.close();
            grid.close();
            s.close();
        }
        catch(Exception ex){
	    try{ total.close(); }
	    catch(Exception e){}
	    try{ roomTotals.close(); }
	    catch(Exception e){}
	    try{ monthTotals.close(); }
	    catch(Exception e){}
	    try{ grid.close(); }
	    catch(Exception e){}
	    try{ s.close(); }
	    catch(Exception e){}
        }
        return result;
    }

    /**
     * Similar to getRevenue, except it returns the number of Reservations by room, month.
     */
    public static double [][] getNumRes(Connection conn){
	Statement s = null;
	ResultSet total, roomTotals, monthTotals, grid;
        total = roomTotals = monthTotals = grid = null;
        double [][] result = null;
        try{
            result = new double[11][13];
            s = conn.createStatement();
            total = s.executeQuery("SELECT COUNT(*) AS NumRes FROM Reservations RV");
            total.next();
            result[10][12] = total.getInt("NumRes");
            roomTotals = s.executeQuery("SELECT RV.Room, COUNT(*) AS NumRes FROM Reservations RV GROUP BY RV.Room ORDER BY RV.Room");
            int row = 0;
            int col = 12;
            boolean f = roomTotals.next();
            while(f){
                result[row][col] = roomTotals.getInt("NumRes");
                row++;
                f = roomTotals.next();
            }
           monthTotals = s.executeQuery("SELECT TO_CHAR(RV.CheckIn, 'MM') AS Month, COUNT(*) AS NumRes FROM Reservations RV GROUP BY TO_CHAR(RV.CheckIn, 'MM') ORDER BY Month");
            col = 0;
            f = monthTotals.next();
            while(f){
                result[row][col] = monthTotals.getInt("NumRes");
                col++;
                f = monthTotals.next();
            }
            grid = s.executeQuery("SELECT RV.Room, TO_CHAR(RV.CheckIn, 'MM') AS Month, COUNT(*) AS NumRes FROM Reservations RV GROUP BY RV.Room, TO_CHAR(RV.CheckIN, 'MM') ORDER BY RV.Room, Month");
            row = 0;
            col = 0;
            f = grid.next();
            while(f){
                while(f && col < 12){
                    result[row][col] = grid.getInt("NumRes");
                    col++;
                    f = grid.next();
                }
                col = 0;
                row++;
            }
            total.close();
            roomTotals.close();
            monthTotals.close();
            grid.close();
            s.close();
        }
        catch(Exception ex){
	    try{ total.close(); }
	    catch(Exception e){}
	    try{ roomTotals.close(); }
	    catch(Exception e){}
	    try{ monthTotals.close(); }
	    catch(Exception e){}
	    try{ grid.close(); }
	    catch(Exception e){}
	    try{ s.close(); }
	    catch(Exception e){}
        }
        return result;
    }

    /**
     * Checks which rooms are available on a given date.
     * 
     * @param   conn    Connection to DB
     * @param   date    The specific date that we're checking for
     * @return      Returns an int[] array of 10 elements, with the ith element referring to the ith room (ordered alpha).
     *              If array[i] == 0, it's free. If array[i] == 1, it's occupied.
     */
    public static Vector<Vector> dateRes(Connection conn, java.sql.Date date)
    {
        Vector<Vector> ret = new Vector<Vector>();
	Statement s = null;
	ResultSet result = null;
        try {
            s = conn.createStatement();
            result = s.executeQuery("SELECT RoomID FROM Rooms ORDER BY RoomID");
            boolean f = result.next();
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(result.getString("RoomID"));
                temp.add("Empty");
                ret.add(temp);
                f = result.next();
            }
            result.close();
            String query = "SELECT RV.Room, COUNT(*) AS Res FROM Reservations RV WHERE CheckIn <= TO_DATE('" + date + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + date + "', 'YYYY-MM-DD')  GROUP BY RV.Room ORDER BY RV.Room";
            result = s.executeQuery(query);
            f = result.next();
            while(f)
            {
                for(int i = 0; i < ret.size(); i++){
                    if(ret.get(i).get(0).equals(result.getString("Room"))){
                        if(result.getInt("Res") == 1){
                            ret.get(i).set(1, "Occupied");
                            break;
                        }
                    }
                }
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex)
        {
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return ret;
    }

    public static Vector<Vector> dateResRoom(Connection conn, String start ,String  end, String roomID)
    {
        Vector<Vector> ret = null;
	Statement s = null;
	ResultSet result = null;
        try{
	    s = conn.createStatement();
	    result = s.executeQuery("SELECT * FROM Reservations WHERE room = '" + roomID + "' AND ((CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckIn <= TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (CheckOut > TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (CheckIn <= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut >= TO_DATE('" + end + "', 'YYYY-MM-DD')))");
            ret = new Vector<Vector>();
            boolean f = result.next();
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(result.getInt("Code"));
                temp.add(result.getString("Room"));
                temp.add(result.getDate("CheckIn"));
                temp.add(result.getDate("CheckOut"));
                ret.add(temp);
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return ret;
    }

    /**
     * Returns a list of reservations for a specific room that occupy any night within the given date ranges
     * 
     * @param   conn    Connection to DB
     * @param   Start   start date of range
     * @param   End     end date of range
     * @param   roomID  specific room we're checking
     * @return      ArrayList of reservations
     */
    public static ArrayList<Reservation> dateRoomRes(Connection conn, java.sql.Date start, java.sql.Date end, String roomID)
    {
	Statement s = null;
	ResultSet result = null;
        ArrayList<Reservation> retArray = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Reservations WHERE room = '" + roomID + "' AND ((CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckIn <= TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (CheckOut > TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD')) OR (CheckIn <= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut >= TO_DATE('" + end + "', 'YYYY-MM-DD')))");
            retArray = new ArrayList<Reservation> ();
            boolean f = result.next();
            while(f)
            {
                retArray.add(buildReservation(result));
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return retArray;
    }
    
    /**
     * Builds a reservation based on a ResultSet, and returns that Reservation object filled in.
     * @param   result    ResultSet from a SQL query on the Reservations table
     * @return        Returns a Reservation object filled in
     */
    public static Reservation buildReservation(ResultSet result) throws SQLException
    {
	Reservation temp = new Reservation();

	temp = new Reservation();
	temp.code = result.getInt("code");
	temp.room = result.getString("room");
	temp.checkIn = result.getDate("checkIn");
	temp.checkOut = result.getDate("checkOut");
	temp.rate = result.getFloat("rate");
	temp.lastName = result.getString("lastName");
	temp.firstName = result.getString("firstName");
	temp.adults = result.getInt("adults");
	temp.kids = result.getInt("kids");
	return temp;
    }

    public static Vector<Vector> resInterval(Connection conn, String start, String end){
        Vector<Vector> interval = null;
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Reservations RV WHERE RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckIN <= TO_DATE('" + end + "', 'YYYY-MM-DD') ORDER BY RV.CheckIn, RV.CheckOut");
            interval = new Vector<Vector>();
            boolean f = result.next();
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(result.getInt("Code"));
                temp.add(result.getString("Room"));
                temp.add(result.getDate("CheckIn"));
                temp.add(result.getDate("CheckOut"));
                interval.add(temp);
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return interval;
    }

    /**
     * Returns an ArrayList of reservations that COMMENCE within the range. SPECIFICALLY COMMENCE.
     */
    public static ArrayList<Reservation> intervalRes(Connection conn, java.sql.Date start, java.sql.Date end){
        ArrayList<Reservation> interval = null;
	Statement s = null;
	ResultSet result = null;
        try{
	    s = conn.createStatement();
	    result = s.executeQuery("SELECT * FROM Reservations RV WHERE RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckIN <= TO_DATE('" + end + "', 'YYYY-MM-DD') ORDER BY RV.CheckIn, RV.CheckOut");
            interval = new ArrayList<Reservation> ();
            boolean f = result.next();
            while(f){
                interval.add(buildReservation(result));
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return interval;
    }

    public static Vector<Vector> availCheck(Connection conn, java.sql.Date start, java.sql.Date end){
        Vector<Vector> avail = null;
	Statement r, s;
        r = s = null;
	ResultSet r1, result;
        r1 = result = null;
        try{
            r = conn.createStatement();
            r1 = r.executeQuery("SELECT RoomID FROM Rooms");
            boolean f = r1.next();
            avail = new Vector<Vector>();
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(r1.getString("RoomID"));
                temp.add("Empty");
                avail.add(temp);
                f = r1.next();
            }
            r.close();
            r1.close();
            s = conn.createStatement();
            // Union between table of reservations that start between Start and End, and...
            String query = "(SELECT RV.Code, (RV.CheckOut - RV.CheckIN) AS Nights FROM Reservations RV WHERE RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the startdate is between Start and End, and the end date is after End, and...
            query = query + " UNION (SELECT RV.Code, (TO_DATE('" + end + "', 'YYYY-MM-DD') - RV.CheckIN) AS Nights FROM Reservations RV WHERE RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckIN < TO_DATE('" + end + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the enddate is between Start and End, and the start date is before Start, and...
            query = query + " UNION (SELECT RV.Code, (RV.CheckOut - TO_DATE('" + start + "', 'YYYY-MM-DD')) AS Nights FROM Reservations RV WHERE RV.CheckIN < TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + start + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the startdate and enddate that are both on the outer ranges of Start and End.
            query = query + " UNION (SELECT RV.Code, (TO_DATE('" + end + "', 'YYYY-MM-DD') - TO_DATE('" + start + "', 'YYYY-MM-DD')) AS Nights FROM Reservations RV WHERE RV.CheckIn < TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // Finally, get the sum of all those days
            query = "SELECT RV.Room, SUM(R.Nights) AS Avail FROM Reservations RV, (" + query + ") R WHERE RV.Code = R.Code GROUP BY RV.Room ORDER BY RV.Room";
            //span is the number of days 
            long span = (end.getTime() - start.getTime()) / DAY;
            result = s.executeQuery(query);
            f = result.next();
            while(f)
            {
                for(int i = 0; i < avail.size(); i++){
                    if(avail.get(i).get(0).equals(result.getString("Room"))){
                        if(result.getInt("Avail") == span){
                            avail.get(i).set(1, "Fully Occupied");
                            break;
                        }
                        if(result.getInt("Avail") > 0){
                            avail.get(i).set(1, "Partially Occupied");
                            break;
                        }
                    }
                }
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
	    try{r.close();}
	    catch(Exception e){}
	    try{r1.close();}
	    catch(Exception e){}
	    try{s.close();}
	    catch(Exception e){}
	    try{result.close();}
	    catch(Exception e){}
        }
        return avail;
    }

/**
 * Checks the availability of a specific room between two dates, Start and End
 * 
 * @param   conn    Connection
 * @param   start   Start date
 * @param   end     End date
 * @param   roomID  specific room we're checking the availability for
 * @return      avail - -1 is occupied, 0 is partially occupied, 1 is free
 */
    public static int checkAvail(Connection conn, java.sql.Date start, java.sql.Date end, String roomID){
	Statement s = null;
	ResultSet result = null;
        int avail = 0;
        try{
            s = conn.createStatement();
            // Union between table of reservations that start between Start and End, and...
            String query = "(SELECT RV.Code, (RV.CheckOut - RV.CheckIN) AS Nights FROM Reservations RV WHERE RV.Room = '" + roomID + "' AND RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the startdate is between Start and End, and the end date is after End, and...
            query = query + " UNION (SELECT RV.Code, (TO_DATE('" + end + "', 'YYYY-MM-DD') - RV.CheckIN) AS Nights FROM Reservations RV WHERE RV.Room = '" + roomID + "' AND RV.CheckIn >= TO_DATE('" + start + "', 'YYYY-MM-DD') AND RV.CheckIN < TO_DATE('" + end + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the enddate is between Start and End, and the start date is before Start, and...
            query = query + " UNION (SELECT RV.Code, (RV.CheckOut - TO_DATE('" + start + "', 'YYYY-MM-DD')) AS Nights FROM Reservations RV WHERE RV.Room = '" + roomID + "' AND RV.CheckIN < TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut <= TO_DATE('" + end + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + start + "', 'YYYY-MM-DD'))";
            // ...table of reservations where the startdate and enddate that are both on the outer ranges of Start and End.
            query = query + " UNION (SELECT RV.Code, (TO_DATE('" + end + "', 'YYYY-MM-DD') - TO_DATE('" + start + "', 'YYYY-MM-DD')) AS Nights FROM Reservations RV WHERE RV.Room = '" + roomID + "' AND RV.CheckIn < TO_DATE('" + start + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + end + "', 'YYYY-MM-DD'))";
            // Finally, get the sum of all those days
            query = "SELECT RV.Room, SUM(R.Nights) AS Avail FROM Reservations RV, (" + query + ") R WHERE RV.Code = R.Code GROUP BY RV.Room ORDER BY RV.Room";
            //span is the number of days 
            long span = (end.getTime() - start.getTime()) / DAY;
            result = s.executeQuery(query);
            boolean f = result.next();
            if(result.getInt("Avail") == span){
                avail = -1;
            }
            else if(result.getInt("Avail") == 0){
                avail = 1;
            }
            else{
                avail = 0;
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{s.close();}
	    catch(Exception e){}
            try{result.close();}
	    catch(Exception e){}
        }
        return avail;
    }

    public static Vector<Vector> resRoom(Connection conn, String roomName){
        Vector<Vector> interval = null;
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Reservations RV, Rooms R WHERE R.RoomID = RV.Room AND R.roomName = '" + roomName + "' ORDER BY RV.CheckIn, RV.CheckOut");
            interval = new Vector<Vector>();
            boolean f = result.next();
            while(f){
                Vector<Object> temp = new Vector<Object>();
                temp.add(result.getInt("Code"));
                temp.add(result.getString("Room"));
                temp.add(result.getDate("CheckIn"));
                temp.add(result.getDate("CheckOut"));
                interval.add(temp);
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{s.close();}
	    catch(Exception e){}
            try{result.close();}
	    catch(Exception e){}
        }
        return interval;
    }

    /**
     * Returns an ArrayList of Reservations for the specified room.
     */
    public static ArrayList<Reservation> roomRes(Connection conn, String roomID){
        ArrayList<Reservation> interval = null;
	Statement s = null;
	ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Reservations RV WHERE RV.Room = '" + roomID + "' ORDER BY RV.CheckIn, RV.CheckOut");
            interval = new ArrayList<Reservation> ();
            boolean f = result.next();
            while(f){
                interval.add(buildReservation(result));
                f = result.next();
            }
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{s.close();}
	    catch(Exception e){}
            try{result.close();}
	    catch(Exception e){}
        }
        return interval;
    }
/**
 * Private method for use in getRates(). Only useful for getting a single day's rate.
 *
 * @param   conn    Connection
 * @param   date    The date that we're checking
 * @param   roomID  The roomID of the room we're checking
 * @return     The rate, modified for the specific date, of this room
 */
    public static double getRate(Connection conn, java.sql.Date date, String roomID)
    {
        double retVal = 0;
        ResultSet result = null;
        Statement s = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT COUNT(*) FROM Reservations WHERE CheckIn <= TO_DATE('" + date.toString() + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + date + "', 'YYYY-MM-DD') AND Room = '" + roomID + "'");
            boolean f = result.next();
            int count = result.getInt("COUNT(*)");
            result.close();
            if(count == 1)
            {
                s.close();
                return 0;
            }
            else //if(result.getInt("COUNT(*)") == 0)
            {
                result = s.executeQuery("SELECT Baseprice FROM Rooms WHERE roomID = '" + roomID + "'");
                f = result.next();
                retVal = result.getInt("BASEPRICE");
                s.close();
                result.close();
                //Check specific 125% dates
                if((date.equals(java.sql.Date.valueOf("2010-01-01"))) || (date.equals(java.sql.Date.valueOf("2010-07-4")))
                || (date.equals(java.sql.Date.valueOf("2010-09-06"))) || (date.equals(java.sql.Date.valueOf("2010-10-30"))))
                {
                    retVal = retVal * 1.25;
                    return retVal;
                }
                
                //Check weekends
                for(int i = 0; i < 52; i++)
                {
                    if(date.equals(new java.sql.Date(SATURDAY.getTime() + (i*7*DAY))) || date.equals(new java.sql.Date(SUNDAY.getTime() + (i*7*DAY))))
                    {
                        retVal = retVal * 1.1;
                        return retVal;
                    }
                }
                return retVal;
            }
        }
        catch(SQLException ex)
        {
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close();}
	    catch(Exception e){}
        }
        return retVal;
    }
    
    /**
     * Pulic method for getting the rates of the desired room from the Start date to the End date.
     * Ordered by date. If an item in the array is 0, it means the room is occupied that day.
     * It's the responsibility of the caller to know what days match up with what elements.
     * 
     * @param   conn    The connection between thsi program and the DB
     * @param   start   The first date in the range.
     * @param   end     The last date in the range.
     * @param   roomID  The primary key of the room the user is looking at.
     * @return      An integer array, with the ith element corresponding to the ith day in the range.
     *              If array[i] = 0, the room is occupied on that day. Otherwise, array[i] will represent
     *              the rate for the room on that date. Weekends and holidays are already accounted for.
     */
    public static double[] getRates(Connection conn, java.sql.Date start, java.sql.Date end, String roomID)
    {
        double[] retArray;
        int days = (int)((end.getTime() - start.getTime()) / DAY);
        retArray = new double[days];
        for(int i = 0; i < days; i++)
        {
            retArray[i] = getRate(conn, new java.sql.Date(start.getTime() + (i*(DAY))), roomID);
        }
        double max = retArray[0];
        for(int i = 1; i < days; i++){
            if(retArray[i] > max)
                max = retArray[i];
        }
        for(int i = 0; i < days; i++)
            retArray[i] = max;
        return retArray;
    }

    public static double avgRate(double [] rates){
        double avg = 0;
        for(int i = 0; i < rates.length; i++)
            avg += rates[i];
        avg = avg / rates.length;
        return avg;
    }

    /**
     * Method to replace ratesGet and rateGet using a PL/SQL function
     */

    public static Vector<Vector> plSQLratesGet(Connection conn, String start, String end, String roomName)
    {
        Vector<Vector> retArray = new Vector<Vector>();
        java.sql.Date st = java.sql.Date.valueOf(start);
        java.sql.Date en = java.sql.Date.valueOf(end);
        int days = (int)((en.getTime() - st.getTime()) / DAY);
        Statement s = null;
        ResultSet result = null;
        String roomID = "";
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT RoomID FROM Rooms WHERE roomName = '" + roomName + "'");
            result.next();
            roomID = result.getString("RoomID");
            s.close();
            result.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
            try{
                result.close();
            }
            catch(Exception e){
            }
        }
        for(int i = 0; i < days; i++){
            java.sql.Date day = new java.sql.Date(st.getTime() + (i * (DAY)));
            Vector<Object> temp = new Vector();
            temp.add(day.toString());
            CallableStatement cstmt = null;
            try{
                cstmt = conn.prepareCall("{? = call INNOperations.fin_Occupancy(?, ?)}");
                String dest = null;
                cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                cstmt.setString(2, roomID);
                cstmt.setDate(3, day);
                cstmt.execute();
                dest = cstmt.getString(1);
                cstmt.close();
                temp.add(dest);
            }
            catch(SQLException ex){
                System.out.println(ex);
                try{
                    cstmt.close();
                }
                catch(Exception e){
                }
            }
            retArray.add(temp);
        }
        double max = 0;
        for(int i = 0; i < days; i++){
            if(!retArray.get(i).get(1).toString().equals("Occupied")){
                double check = Double.parseDouble(retArray.get(i).get(1).toString());
                if(check > max)
                    max = check;
            }
        }
        for(int i = 0; i < days; i++){
            if(!retArray.get(i).get(1).toString().equals("Occupied"))
                retArray.get(i).set(1, max);
        }
        return retArray;
    }

    public static Vector<Vector> ratesGet(Connection conn, String start, String end, String roomName)
    {
        Vector<Vector> retArray = new Vector<Vector>();
        java.sql.Date st = java.sql.Date.valueOf(start);
        java.sql.Date en = java.sql.Date.valueOf(end);
        int days = (int)((en.getTime() - st.getTime()) / DAY);
        for(int i = 0; i < days; i++)
        {
            retArray.add(rateGet(conn, new java.sql.Date(st.getTime() + (i*(DAY))), roomName));
        }
        double max = Double.parseDouble((String)retArray.get(0).get(1));
        for(int i = 1; i < days; i++){
            if(Double.parseDouble((String)retArray.get(i).get(1)) > max)
                max = Double.parseDouble((String)retArray.get(i).get(1));
        }
        for(int i = 0; i < days; i++){
            if(Double.parseDouble((String)retArray.get(i).get(1)) == 0)
                retArray.get(i).set(1, "Occupied");
            else
                retArray.get(i).set(1, max);
        }
        return retArray;
    }

    public static Vector<Object> rateGet(Connection conn, java.sql.Date date, String roomName)
    {
        Vector<Object> retVal = new Vector<Object>();
        ResultSet result = null;
        Statement s = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT COUNT(*) FROM Reservations RV, Rooms R WHERE RV.Room = R.RoomID AND CheckIn <= TO_DATE('" + date.toString() + "', 'YYYY-MM-DD') AND CheckOut > TO_DATE('" + date + "', 'YYYY-MM-DD') AND R.roomName = '" + roomName + "'");
            boolean f = result.next();
            int count = result.getInt("COUNT(*)");
            result.close();
            if(count == 1)
            {
                s.close();
                retVal.add("" + date);
                retVal.add("0");
                return retVal;
            }
            else //if(result.getInt("COUNT(*)") == 0)
            {
                result = s.executeQuery("SELECT Baseprice FROM Rooms WHERE roomName = '" + roomName + "'");
                f = result.next();
                double price = result.getDouble("BASEPRICE");
                s.close();
                result.close();
                //Check specific 125% dates
                if((date.equals(java.sql.Date.valueOf("2010-01-01"))) || (date.equals(java.sql.Date.valueOf("2010-07-4")))
                || (date.equals(java.sql.Date.valueOf("2010-09-06"))) || (date.equals(java.sql.Date.valueOf("2010-10-30"))))
                {
                    price = price * 1.25;
                    retVal.add("" + date);
                    retVal.add("" + price);
                    return retVal;
                }
                
                //Check weekends
                for(int i = 0; i < 52; i++)
                {
                    if(date.equals(new java.sql.Date(SATURDAY.getTime() + (i*7*DAY))) || date.equals(new java.sql.Date(SUNDAY.getTime() + (i*7*DAY))))
                    {
                        price = price * 1.1;
                        retVal.add("" + date);
                        retVal.add("" + price);
                        return retVal;
                    }
                }
                retVal.add("" + date);
                retVal.add("" + price);
                return retVal;
            }
            
        }
        catch(SQLException ex)
        {
	    try{ s.close(); }
	    catch(Exception e){}
	    try{ result.close(); }
	    catch(Exception e){}
        }
        return retVal;
    }

    public static int generateCode(Connection conn){
        Random rn = new Random();
        int code = 0;
        int count = 1;
	Statement s = null;
	ResultSet result = null;
        while(count != 0){
            code = 999999 - (Math.abs(rn.nextInt() % 900000));
            try{
                s = conn.createStatement();
                result = s.executeQuery("SELECT COUNT(*) FROM Reservations WHERE Code = " + code);
                boolean f = result.next();
                count = result.getInt("COUNT(*)");
                s.close();
                result.close();
            }
            catch(SQLException ex){
		try{ s.close(); }
		catch(Exception e){}
		try{ result.close(); }
		catch(Exception e){}
            }
        }
        return code;
    }
}
