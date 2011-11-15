/**
 * Halli Meth, Mark Lerner, Michael Gage
 * CPE 365
 * Spring 2011
 * Lab 07
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class InnReservations{

    private static Connection conn;

    public static void main(String [] args){
        conn = connect();
        if(conn != null){
            InnReservations.createDB(conn);
            JFrame frame = new gui(conn);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
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
        if(RoomDB.exist(conn) == -1)
            RoomDB.create(conn);
        if(ResDB.exist(conn) == -1)
            ResDB.create(conn);
    }

    public static void dropDB(Connection conn){
        if(ResDB.exist(conn) != -1)
            ResDB.drop(conn);
        if(RoomDB.exist(conn) != -1)
            RoomDB.drop(conn);
    }

    public static void tableClear(Connection conn){
        if(ResDB.exist(conn) != -1)
            ResDB.clear(conn);
        if(RoomDB.exist(conn) != -1)
            RoomDB.clear(conn);
    }

    public static void tablesFill(Connection conn){
        FileReader reader = null;
        Scanner scanner = null;
        Statement s = null;
        try{
            reader = new FileReader("INN-insert.txt");
            scanner = new Scanner(reader);
            s = conn.createStatement();
            while(scanner.hasNextLine()){
                try{
                    String temp = scanner.nextLine();
                    if(temp.charAt(0) == 'I')
                        s.executeUpdate(temp);
                }
                catch(SQLException ex){
                }
            }
            s.close();
            reader.close();
            scanner.close();
        }
        catch(SQLException ex){
            try{
                s.close();
            }
            catch(Exception e){
            }
        }
        catch(Exception ex1){
            ex1.printStackTrace();
            System.exit(-1);
        }
    }

    public static int loadCheck(Connection conn){
        if(RoomDB.exist(conn) == -1 || ResDB.exist(conn) == -1){
            createDB(conn);
            tablesFill(conn);
            return -1;
        }
        else if(RoomDB.exist(conn) == 0 || ResDB.exist(conn) == 0){
            tablesFill(conn);
            return 0;
        }
        else{
            return 1;
        }
    }
}


