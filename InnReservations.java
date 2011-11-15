import java.sql.*;
import java.util.*;
import java.io.*;

public class InnReservations{

    private static Connection conn;

    public static void main(String [] args){
        conn = connect();
        if(conn != null){
            setup(conn);
            try{
                ImageOnBackground.runGUI(conn);
            }
            catch(IOException e){
                System.out.println(e);
            }/*
            System.out.println("Dropping tables");
            dropDB(conn);
            System.out.println("\nCreating tables");
            createDB(conn);
            System.out.println("\nFilling tables");
            tablesFill(conn);
            System.out.println("\nRoomDB.getAll");
            ArrayList<Room> rooms = RoomDB.getAll(conn);
            for(int i = 0; i < rooms.size(); i++)
                System.out.println(rooms.get(i));
            System.out.println("\nRoomDB.getDetail");
            Room room = RoomDB.getDetail(conn, "RND");
            System.out.println(room);
            System.out.println("\nResDB.getAll");
            ArrayList<Reservation> reservations = ResDB.getAll(conn);
            for(int i = 0; i < reservations.size(); i++)
                System.out.println(reservations.get(i));
            System.out.println("\nResDB.getDetail");
            Reservation reserve = ResDB.getDetail(conn, 47496);
            System.out.println(reserve);
            System.out.println("\nResDB.getRevenue");
            Object [][] res = ResDB.getRevenue(conn);
            for(int i = 0; i < 11; i++){
                for(int j = 0; j < 13; j++)
                    System.out.print(res[i][j] + " ");
                System.out.println();
            }
            System.out.println("\nResDB.getNumRes");
            res = ResDB.getNumRes(conn);
            for(int i = 0; i < 11; i++){
                for(int j = 0; j < 13; j++)
                    System.out.print(res[i][j] + " ");
                System.out.println();
            }
            System.out.println("\nResDB.dateRes");
            java.sql.Date start = java.sql.Date.valueOf("2010-10-31");
            ArrayList<String> avail = ResDB.dateRes(conn, start.toString());
            for(int i = 0; i < avail.size(); i++){
                System.out.println(avail.get(i));
            }
            System.out.println("\nResDB.dateRoomRes");
            java.sql.Date end = java.sql.Date.valueOf("2011-01-01");
            reservations = ResDB.dateRoomRes(conn, start, end, "RND");
            for(int i = 0; i < reservations.size(); i++)
                System.out.println(reservations.get(i));
            System.out.println("\nResDB.intervalRes");
            reservations = ResDB.intervalRes(conn, start, end);
            for(int i = 0; i < reservations.size(); i++)
                System.out.println(reservations.get(i));
            System.out.println("\nResDB.checkAvail");
            ArrayList<String> check = ResDB.checkAvail(conn, start.toString(), end.toString());
            for(int i = 0; i < check.size(); i++)
                System.out.println(check.get(i));
            System.out.println("\nResDB.roomRes");
            reservations = ResDB.roomRes(conn, "RND");
            for(int i = 0; i < reservations.size(); i++)
                System.out.println(reservations.get(i));
            System.out.println("\nResDB.getRate");
            System.out.println(ResDB.getRate(conn, start, "RND"));
            System.out.println("\nResDB.getRates");
            double [] rates = ResDB.getRates(conn, start, end, "RND");
            for(int i = 0; i < rates.length; i++)
                System.out.println(rates[i]);
            System.out.println("\nClearing Tables");
            tableClear(conn);
            System.out.println("\nDropping Tables");
            dropDB(conn);*/
        }
    }
    
    public static Connection connect(){
        Connection temp = null;
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            String url, user, pw;
            try{
                FileReader reader = new FileReader("ServerSettings.txt");
                Scanner scanner = new Scanner(reader);
                url = scanner.nextLine();
                user = scanner.nextLine();
                pw = scanner.nextLine();
                scanner.close();
                reader.close();
                try{
                    temp = DriverManager.getConnection(url, user, pw);
                }
                catch(Exception ex){
                    System.out.println("Could not open connection");
                }
            }
            catch(Exception ex){
                System.out.println("Unable to find connection information");
            }
        }
        catch(Exception ex){
            System.out.println("Driver not found");
        }
        return temp;
    }

    public static void createDB(Connection conn){
        RoomDB.create(conn);
        ResDB.create(conn);
    }

    public static void dropDB(Connection conn){
        ResDB.drop(conn);
        RoomDB.drop(conn);
    }

    public static void tableClear(Connection conn){
        ResDB.clear(conn);
        RoomDB.clear(conn);
    }

    public static void tablesFill(Connection conn){
        FileReader reader = null;
        Scanner scanner = null;
        try{
            reader = new FileReader("INN-insert.txt");
            scanner = new Scanner(reader);
            Statement s = conn.createStatement();
            while(scanner.hasNextLine()){
                try{
                    s.executeUpdate(scanner.nextLine());
                }
                catch(SQLException e){
                }
            }
            reader.close();
            scanner.close();
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        catch(Exception ex1){
            ex1.printStackTrace();
        }
    }

    public static void setup(Connection conn){
        if(RoomDB.exist(conn) < 0)
            RoomDB.create(conn);
        if(ResDB.exist(conn) < 0)
            ResDB.create(conn);
    }

    public static void loadCheck(Connection conn){
        if(RoomDB.exist(conn) < 0 && ResDB.exist(conn) < 0)
            createDB(conn);
        tablesFill(conn);
    }

    //Takes care of AR-1
    public static String getStatus(Connection conn)
    {
        String status = "Current Status\n---------------\nRooms: ";
        if(RoomDB.exist(conn) == -1)
        {
            status += "No Database\n";
        }
        else if(RoomDB.exist(conn) == 0)
        {
            status += "Empty\n";
        }
        else
        {
            status += "Full\n";
        }
        status += "Reservations: ";
        if(ResDB.exist(conn) == -1)
        {
            status += "No Database\n";
        }
        else if(ResDB.exist(conn) == 0)
        {
            status += "Empty\n";
        }
        else
        {
            status += "Full\n";
        }
        
        return status;
    }
}
