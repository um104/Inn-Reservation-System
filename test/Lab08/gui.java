/**
 * Halli Meth, Mark Lerner, Michael Gage
 * CPE 365
 * Spring 2011
 * Lab 07
 */
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.sql.*;

public class gui extends JFrame{

    private JTabbedPane tabbedPane;
    private JPanel admin;
    private JPanel owner;
    private JPanel guest;
    private JPanel exit;
    private static Connection conn;

    public gui(Connection conn){
        super("Hello and welcome to the HMM Inn, your number one choice for hotel management.");
        this.conn = conn;
        getContentPane().removeAll();
        tabbedPane = new JTabbedPane();
        admin = addAdminPanel();
        owner = addOwnerPanel();
        guest = addGuestPanel();
        exit = addExitPanel();
        tabbedPane.addTab("Admin", admin);
        tabbedPane.addTab("Owner", owner);
        tabbedPane.addTab("Guest", guest);
        tabbedPane.addTab("Leave", exit);
	tabbedPane.setEnabledAt(1, false);
	tabbedPane.setEnabledAt(2, false);
        getContentPane().add(tabbedPane);
        getContentPane().validate();
        setSize(800, 650);
    }

    public void rebuild(){
        getContentPane().removeAll();
        tabbedPane = new JTabbedPane();
        admin = addAdminPanel();
        owner = addOwnerPanel();
        guest = addGuestPanel();
        exit = addExitPanel();
        tabbedPane.addTab("Admin", admin);
        tabbedPane.addTab("Owner", owner);
        tabbedPane.addTab("Guest", guest);
        tabbedPane.addTab("Leave", exit);
	tabbedPane.setEnabledAt(0, false);
	tabbedPane.setEnabledAt(1, false);
        getContentPane().add(tabbedPane);
        getContentPane().validate();
    }

    class NoEditTable extends JTable{
        public NoEditTable(Vector rowData, Vector columnNames){
            super(rowData, columnNames);
        }
        public boolean isCellEditable(int rowIndex, int vColIndex){
            return false;
        }
    }

    public JPanel addGuestPanel(){
        JPanel guest = new JPanel();
        guest.setLayout(new BorderLayout());
        JTabbedPane options = new JTabbedPane();
        options.addTab("Rooms and Rates", addRoomRates());
        options.addTab("Reservations", addReservePanel());
        guest.add(options, BorderLayout.CENTER);
        return guest;
    }

    class ResetListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            rebuild();
            tabbedPane.setSelectedIndex(2);
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            options.setSelectedIndex(1);
        }
    }

    public JPanel confirmation1(int code, String roomName, String checkIn, String checkOut, double rate, String lastName, String firstName, int adults, int kids){
        JPanel overall = new JPanel();
        overall.setLayout(new GridLayout(10,1));
        JPanel panel = new JPanel();
        overall.add(panel);
        panel = new JPanel();
        JLabel message = new JLabel("Your reservation is complete.");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Reservation Information:");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel(firstName + " " + lastName + " is staying with " + adults + " adult(s) and " + kids + " kid(s)");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Checking in on " + checkIn + " and checking out on " + checkOut);
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Staying in " + roomName + " at a nightly rate of $" + rate);
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Your reservation code is " + code + ".");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Please keep this for your records");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        JButton confirm = new JButton("Continue");
        confirm.addActionListener(new ResetListener1());
        panel.add(confirm);
        overall.add(panel);
        return overall;
    }

    class SubmitListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel make = (JPanel)options.getComponent(1);
            JPanel panel = (JPanel)make.getComponent(0);
            JPanel text = (JPanel)panel.getComponent(0);
            JTextField lblTxt = (JTextField)text.getComponent(1);
            String lastName = lblTxt.getText();
            text = (JPanel)panel.getComponent(1);
            lblTxt = (JTextField)text.getComponent(1);
            String firstName = lblTxt.getText();
            text = (JPanel)panel.getComponent(2);
            JComboBox cmb = (JComboBox)text.getComponent(1);
            Integer adults = Integer.parseInt((String)cmb.getSelectedItem());
            text = (JPanel)panel.getComponent(3);
            cmb = (JComboBox)text.getComponent(1);
            Integer kids = Integer.parseInt((String)cmb.getSelectedItem());
            panel = (JPanel)make.getComponent(2);
            text = (JPanel)panel.getComponent(0);
            lblTxt = (JTextField)text.getComponent(1);
            String roomName = lblTxt.getText();
            text = (JPanel)panel.getComponent(1);
            lblTxt = (JTextField)text.getComponent(1);
            Double rate = Double.parseDouble(lblTxt.getText());
            text = (JPanel)panel.getComponent(2);
            lblTxt = (JTextField)text.getComponent(1);
            String start = lblTxt.getText();
            text = (JPanel)panel.getComponent(3);
            lblTxt = (JTextField)text.getComponent(1);
            String end = lblTxt.getText();
            text = (JPanel)make.getComponent(1);
            Double discount = 1.0;
            JRadioButton selected = (JRadioButton)text.getComponent(0);
            if(selected.isSelected())
                discount = 1.0;
            else{
                selected = (JRadioButton)text.getComponent(1);
                if(selected.isSelected())
                    discount = 0.9;
                else{
                    selected = (JRadioButton)text.getComponent(2);
                    if(selected.isSelected())
                        discount = 0.85;
                }
            }
            String roomID = null;
            Integer maxOccupancy = null;
            Statement s = null;
            ResultSet result = null;
            try{
                s = conn.createStatement();
                result = s.executeQuery("SELECT RoomID, maxOccupancy FROM Rooms WHERE roomName = '" + roomName + "'");
                result.next();
                roomID = result.getString("RoomID");
                maxOccupancy = result.getInt("maxOccupancy");
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
            if(adults + kids > maxOccupancy){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*Room capactiy exceeded, please modify your reservation or select a different room");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else if(adults + kids == 0){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*At least 1 adult or 1 kid must stay in the room, please modify your reservation        ");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else if(lastName.equals("") || firstName.equals("")){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*Please enter both a first and last name, please modify your reservation                   ");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else{
                Integer code = ResDB.generateCode(conn);
                Statement s1 = null;
                try{
                    s1 = conn.createStatement();
                    s1.executeUpdate("INSERT INTO Reservations VALUES (" + code + ", '" + roomID + "', TO_DATE('" + start + "', 'YYYY-MM-DD'), TO_DATE('" + end + "', 'YYYY-MM-DD'), " + (rate * discount) + ", '" + lastName + "', '" + firstName + "', " + adults + ", " + kids + ")");
                    conn.commit();
                    s1.close();
                }
                catch(SQLException ex){
                    try{
                        s1.close();
                    }
                    catch(Exception e){
                    }
                }
                getContentPane().removeAll();
                getContentPane().add(confirmation1(code, roomName, start, end, (rate * discount), lastName, firstName, adults, kids));
                getContentPane().validate();
            }
        }
    }

    public JPanel makeReservation1(String roomName, String start, String end, Double rate){
        JPanel make = new JPanel();
        JPanel text = new JPanel();
        text.setLayout(new GridLayout(3,2));
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Lastname: ");
        JTextField lblTxt = new JTextField(15);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Firstname: ");
        lblTxt = new JTextField(15);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Number of Adults: ");
        JComboBox cmb = new JComboBox();
        cmb.addItem("0");
        cmb.addItem("1");
        cmb.addItem("2");
        cmb.addItem("3");
        cmb.addItem("4");
        panel.add(lbl);
        panel.add(cmb);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Number of Kids: ");
        cmb = new JComboBox();
        cmb.addItem("0");
        cmb.addItem("1");
        cmb.addItem("2");
        cmb.addItem("3");
        cmb.addItem("4");
        panel.add(lbl);
        panel.add(cmb);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Max Occupants: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        Statement s = null;
        ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT maxOccupancy FROM Rooms WHERE roomName = '" + roomName + "'");
            result.next();
            lblTxt.setText("" + result.getInt("maxOccupancy"));
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
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        make.add(text);
        JPanel buttonPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton button = new JRadioButton("None");
        group.add(button);
        buttonPanel.add(button);
        button = new JRadioButton("AAA Discount");
        group.add(button);
        buttonPanel.add(button);
        button = new JRadioButton("AARP Discount");
        group.add(button);
        buttonPanel.add(button);
        buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Discounts"));
        make.add(buttonPanel);
        text = new JPanel();
        text.setLayout(new GridLayout(2,2));
        panel = new JPanel();
        lbl = new JLabel("Room Name: ");
        lblTxt = new JTextField(15);
        lblTxt.setEditable(false);
        lblTxt.setText(roomName);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Rate: ");
        lblTxt = new JTextField(15);
        lblTxt.setEditable(false);
        lblTxt.setText("" + rate);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Starting: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(start);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Ending: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(end);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        make.add(text);
        JPanel submit = new JPanel();
        JButton confirm = new JButton("Confirm Reservation");
        confirm.addActionListener(new SubmitListener1());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomRateListener1());
        submit.add(back);
        submit.add(confirm);
        make.add(submit);
        return make;
    }

    class MakeResListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(1);
            JPanel avail = (JPanel)overall.getComponent(0);
            JTextField st = (JTextField)avail.getComponent(1);
            JTextField en = (JTextField)avail.getComponent(3);
            avail = (JPanel)overall.getComponent(1);
            JScrollPane scroll = (JScrollPane)avail.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                String roomName = (String)table.getValueAt(table.getSelectedRow(), 0);
                Double rate = (Double)table.getValueAt(table.getSelectedRow(), 1);
                String start = st.getText();
                String end = en.getText();
                options.removeAll();
                options.addTab("Rooms and Rates", addRoomRates());
                options.addTab("Reservations", makeReservation1(roomName, start, end, rate));
                options.validate();
                options.setSelectedIndex(1);
            }
        }
    }

    public JPanel addAvailRooms(String start, String end){
        JPanel avail = new JPanel();
        JPanel dates = new JPanel();
        JLabel lbl = new JLabel("Starting: ");
        JTextField lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(start);
        dates.add(lbl);
        dates.add(lblTxt);
        lbl = new JLabel("Ending: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(end);
        dates.add(lbl);
        dates.add(lblTxt);
        JPanel data = new JPanel();
        Vector<Vector> rowData = RoomDB.getResRate(conn, start, end);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Room Name");
        columnNames.add("Nightly Rate");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        data.add(scroll);
        JPanel buttonPanel = new JPanel();
        JButton make = new JButton("Place a Reservation");
        if(table.getRowCount() == 0)
            make.setEnabled(false);
        make.addActionListener(new MakeResListener1());
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadRoomRateListener1());
        buttonPanel.add(back);
        buttonPanel.add(make);
        avail.add(dates);
        avail.add(data);
        avail.add(buttonPanel);
        return avail;
    }

    class GuestResListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel datePair = (JPanel)options.getComponent(1);
            JComboBox month = (JComboBox)datePair.getComponent(2);
            JComboBox day = (JComboBox)datePair.getComponent(4);
            String m1 = (String)month.getSelectedItem();
            String d1 = (String)day.getSelectedItem();
            Integer mi1 = Integer.parseInt(month(m1));
            Integer di1 = Integer.parseInt(d1);
            month = (JComboBox)datePair.getComponent(7);
            day = (JComboBox)datePair.getComponent(9);
            String m2 = (String)month.getSelectedItem();
            String d2 = (String)day.getSelectedItem();
            Integer mi2 = Integer.parseInt(month(m2));
            Integer di2 = Integer.parseInt(d2);
            if((m1.equals("FEB") && di1 > 28) || (m2.equals("FEB") && di2 > 28))
                ;//do nothing
            else if(((m1.equals("APR") || m1.equals("JUN") || 
                      m1.equals("SEP") || m1.equals("NOV")) && di1 > 30) || 
		    ((m2.equals("APR") || m2.equals("JUN") ||
                      m2.equals("SEP") || m2.equals("NOV")) && di2 > 30))
                ;//do nothing
            else if((mi1 == mi2 && di1 == di2) || 
                    (mi1 == mi2 && di1 > di2) ||
                    (mi1 > mi2))
                ;//do nothing
            else{
                String date1 = ("2010-" + month(m1) + "-" + d1);
                String date2 = ("2010-" + month(m2) + "-" + d2);
                options.removeAll();
                options.addTab("Rooms and Rates", addRoomRates());
                options.addTab("Reservations", addAvailRooms(date1, date2));
                options.validate();
                options.setSelectedIndex(1);
            }
        }
    }

    public JPanel addReservePanel(){
        JPanel datePair = new JPanel();
        JLabel name = new JLabel("Starting");
        JLabel monthLbl = new JLabel("Month: ");
        JComboBox month = monthCombo();
        JLabel dayLbl = new JLabel("Day: ");
        JComboBox day = dayCombo();
        JButton submit = new JButton("Availability");
        submit.addActionListener(new GuestResListener());
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        name = new JLabel("Ending");
        monthLbl = new JLabel("Month: ");
        month = monthCombo();
        dayLbl = new JLabel("Day: ");
        day = dayCombo();
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        datePair.add(submit);
        return datePair;
    }

    class ResetListener0 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            rebuild();
            tabbedPane.setSelectedIndex(2);
        }
    }

    public JPanel confirmation0(int code, String roomName, String checkIn, String checkOut, double rate, String lastName, String firstName, int adults, int kids){
        JPanel overall = new JPanel();
        overall.setLayout(new GridLayout(10,1));
        JPanel panel = new JPanel();
        overall.add(panel);
        panel = new JPanel();
        JLabel message = new JLabel("Your reservation is complete.");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Reservation Information:");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel(firstName + " " + lastName + " is staying with " + adults + " adult(s) and " + kids + " kid(s)");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Checking in on " + checkIn + " and checking out on " + checkOut);
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Staying in " + roomName + " at a nightly rate of $" + rate);
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Your reservation code is " + code + ".");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        message = new JLabel("Please keep this for your records");
        panel.add(message);
        overall.add(panel);
        panel = new JPanel();
        JButton confirm = new JButton("Continue");
        confirm.addActionListener(new ResetListener0());
        panel.add(confirm);
        overall.add(panel);
        return overall;
    }

    class SubmitListener0 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel make = (JPanel)options.getComponent(0);
            JPanel panel = (JPanel)make.getComponent(0);
            JPanel text = (JPanel)panel.getComponent(0);
            JTextField lblTxt = (JTextField)text.getComponent(1);
            String lastName = lblTxt.getText();
            text = (JPanel)panel.getComponent(1);
            lblTxt = (JTextField)text.getComponent(1);
            String firstName = lblTxt.getText();
            text = (JPanel)panel.getComponent(2);
            JComboBox cmb = (JComboBox)text.getComponent(1);
            Integer adults = Integer.parseInt((String)cmb.getSelectedItem());
            text = (JPanel)panel.getComponent(3);
            cmb = (JComboBox)text.getComponent(1);
            Integer kids = Integer.parseInt((String)cmb.getSelectedItem());
            panel = (JPanel)make.getComponent(2);
            text = (JPanel)panel.getComponent(0);
            lblTxt = (JTextField)text.getComponent(1);
            String roomName = lblTxt.getText();
            text = (JPanel)panel.getComponent(1);
            lblTxt = (JTextField)text.getComponent(1);
            Double rate = Double.parseDouble(lblTxt.getText());
            text = (JPanel)panel.getComponent(2);
            lblTxt = (JTextField)text.getComponent(1);
            String start = lblTxt.getText();
            text = (JPanel)panel.getComponent(3);
            lblTxt = (JTextField)text.getComponent(1);
            String end = lblTxt.getText();
            text = (JPanel)make.getComponent(1);
            Double discount = 1.0;
            JRadioButton selected = (JRadioButton)text.getComponent(0);
            if(selected.isSelected())
                discount = 1.0;
            else{
                selected = (JRadioButton)text.getComponent(1);
                if(selected.isSelected())
                    discount = 0.9;
                else{
                    selected = (JRadioButton)text.getComponent(2);
                    if(selected.isSelected())
                        discount = 0.85;
                }
            }
            String roomID = null;
            Integer maxOccupancy = null;
            Statement s = null;
            ResultSet result = null;
            try{
                s = conn.createStatement();
                result = s.executeQuery("SELECT RoomID, maxOccupancy FROM Rooms WHERE roomName = '" + roomName + "'");
                result.next();
                roomID = result.getString("RoomID");
                maxOccupancy = result.getInt("maxOccupancy");
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
            if(adults + kids > maxOccupancy){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*Room capactiy exceeded, please modify your reservation or select a different room");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else if(adults + kids == 0){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*At least 1 adult or 1 kid must stay in the room, please modify your reservation        ");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else if(lastName.equals("") || firstName.equals("")){
                int count = java.lang.reflect.Array.getLength(make.getComponents());
                if(count > 4)
                    make.remove(4);
                JPanel note = new JPanel();
                JLabel message = new JLabel("*Please enter both a first and last name, please modify your reservation                   ");
                note.add(message);
                make.add(note);
                make.validate();
            }
            else{
                Integer code = ResDB.generateCode(conn);
                Statement s1 = null;
                try{
                    s1 = conn.createStatement();
                    s1.executeUpdate("INSERT INTO Reservations VALUES (" + code + ", '" + roomID + "', TO_DATE('" + start + "', 'YYYY-MM-DD'), TO_DATE('" + end + "', 'YYYY-MM-DD'), " + (rate * discount) + ", '" + lastName + "', '" + firstName + "', " + adults + ", " + kids + ")");
                    conn.commit();
                    s1.close();
                }
                catch(SQLException ex){
                    try{
                        s1.close();
                    }
                    catch(Exception e){
                    }
                }
                getContentPane().removeAll();
                getContentPane().add(confirmation0(code, roomName, start, end, (rate * discount), lastName, firstName, adults, kids));
                getContentPane().validate();
            }
        }
    }

    public JPanel makeReservation0(String roomName, String start, String end, Double rate){
        JPanel make = new JPanel();
        JPanel text = new JPanel();
        text.setLayout(new GridLayout(3,2));
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Lastname: ");
        JTextField lblTxt = new JTextField(15);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Firstname: ");
        lblTxt = new JTextField(15);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Number of Adults: ");
        JComboBox cmb = new JComboBox();
        cmb.addItem("0");
        cmb.addItem("1");
        cmb.addItem("2");
        cmb.addItem("3");
        cmb.addItem("4");
        panel.add(lbl);
        panel.add(cmb);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Number of Kids: ");
        cmb = new JComboBox();
        cmb.addItem("0");
        cmb.addItem("1");
        cmb.addItem("2");
        cmb.addItem("3");
        cmb.addItem("4");
        panel.add(lbl);
        panel.add(cmb);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Max Occupants: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        Statement s = null;
        ResultSet result = null;
        try{
            s = conn.createStatement();
            result = s.executeQuery("SELECT maxOccupancy FROM Rooms WHERE roomName = '" + roomName + "'");
            result.next();
            lblTxt.setText("" + result.getInt("maxOccupancy"));
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
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        make.add(text);
        JPanel buttonPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton button = new JRadioButton("None");
        group.add(button);
        buttonPanel.add(button);
        button = new JRadioButton("AAA Discount");
        group.add(button);
        buttonPanel.add(button);
        button = new JRadioButton("AARP Discount");
        group.add(button);
        buttonPanel.add(button);
        buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Discounts"));
        make.add(buttonPanel);
        text = new JPanel();
        text.setLayout(new GridLayout(2,2));
        panel = new JPanel();
        lbl = new JLabel("Room Name: ");
        lblTxt = new JTextField(15);
        lblTxt.setEditable(false);
        lblTxt.setText(roomName);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Rate: ");
        lblTxt = new JTextField(15);
        lblTxt.setEditable(false);
        lblTxt.setText("" + rate);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Starting: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(start);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        panel = new JPanel();
        lbl = new JLabel("Ending: ");
        lblTxt = new JTextField(10);
        lblTxt.setEditable(false);
        lblTxt.setText(end);
        panel.add(lbl);
        panel.add(lblTxt);
        text.add(panel);
        make.add(text);
        JPanel submit = new JPanel();
        JButton confirm = new JButton("Confirm Reservation");
        confirm.addActionListener(new SubmitListener0());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomRateListener0());
        submit.add(back);
        submit.add(confirm);
        make.add(submit);
        return make;
    }

    class MakeResListener0 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(0);
            JPanel avail = (JPanel)overall.getComponent(0);
            JTextField ro = (JTextField)avail.getComponent(1);
            JTextField st = (JTextField)avail.getComponent(3);
            JTextField en = (JTextField)avail.getComponent(5);
            avail = (JPanel)overall.getComponent(1);
            JScrollPane scroll = (JScrollPane)avail.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            boolean check = false;
            for(int i = 0; i < table.getRowCount(); i++){
                if((table.getValueAt(i, 1)).toString().equals("Occupied")){
                    check = true;
                    break;
                }
            }
            if(check)
                ;//do nothing
            else{
                String roomName = ro.getText();
                String roomID = null;
                Statement s = null;
                ResultSet result = null;
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
                String start = st.getText();
                String end = en.getText();
                Double rate = ResDB.avgRate(ResDB.getRates(conn, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), roomID));
                options.removeAll();
                options.addTab("Rooms and Rates", makeReservation0(roomName, start, end, rate));
                options.addTab("Reservations", addReservePanel());
                options.validate();
            }
        }
    }

    public JPanel addRatesRoom(String roomName, String start, String end){
        JPanel overall = new JPanel();
        JPanel info = new JPanel();
        JLabel label = new JLabel("Room Name: ");
        JTextField name = new JTextField(15);
        name.setEditable(false);
        name.setText(roomName);
        info.add(label);
        info.add(name);
        label = new JLabel("Start Date: ");
        name = new JTextField(10);
        name.setEditable(false);
        name.setText(start);
        info.add(label);
        info.add(name);
        label = new JLabel("End Date: ");
        name = new JTextField(10);
        name.setEditable(false);
        name.setText(end);
        info.add(label);
        info.add(name);
        JPanel list = new JPanel();
        Vector<Vector> rowData = ResDB.plSQLratesGet(conn, start, end, roomName);//Modified to use the PL/SQL function
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Night");
        columnNames.add("Rate");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        list.add(scroll);
        JPanel buttonPanel = new JPanel();
        JButton make = new JButton("Place a Reservation");
	boolean check = false;
	for(int i = 0; i < table.getRowCount(); i++){
	    if((table.getValueAt(i, 1)).toString().equals("Occupied")){
		check = true;
		break;
	    }
	}
	if(check)
            make.setEnabled(false);
        make.addActionListener(new MakeResListener0());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomRateListener0());
        buttonPanel.add(back);
        buttonPanel.add(make);
        overall.add(info);
        overall.add(list);
        overall.add(buttonPanel);
        return overall;
    }

    class RoomResListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(0);
            JPanel name = (JPanel)overall.getComponent(0);
            JTextField room = (JTextField)name.getComponent(1);
            JPanel datePair = (JPanel)overall.getComponent(1);
            JComboBox month = (JComboBox)datePair.getComponent(2);
            JComboBox day = (JComboBox)datePair.getComponent(4);
            String m1 = (String)month.getSelectedItem();
            String d1 = (String)day.getSelectedItem();
            Integer mi1 = Integer.parseInt(month(m1));
            Integer di1 = Integer.parseInt(d1);
            month = (JComboBox)datePair.getComponent(7);
            day = (JComboBox)datePair.getComponent(9);
            String m2 = (String)month.getSelectedItem();
            String d2 = (String)day.getSelectedItem();
            Integer mi2 = Integer.parseInt(month(m2));
            Integer di2 = Integer.parseInt(d2);
            String roomName = room.getText();
            if((m1.equals("FEB") && di1 > 28) || (m2.equals("FEB") && di2 > 28))
                ;//do nothing
            else if(((m1.equals("APR") || m1.equals("JUN") || 
                      m1.equals("SEP") || m1.equals("NOV")) && di1 > 30) || 
		    ((m2.equals("APR") || m2.equals("JUN") ||
                      m2.equals("SEP") || m2.equals("NOV")) && di2 > 30))
                ;//do nothing
            else if((mi1 == mi2 && di1 == di2) || 
                    (mi1 == mi2 && di1 > di2) ||
                    (mi1 > mi2))
                ;//do nothing
            else{
                String date1 = ("2010-" + month(m1) + "-" + d1);
                String date2 = ("2010-" + month(m2) + "-" + d2);
                options.removeAll();
                options.addTab("Rooms and Rates", addRatesRoom(roomName, date1, date2));
                options.addTab("Reservations", addReservePanel());
                options.validate();
            }
        }
    }

    public JPanel addDateCheckPanel(String roomName){
        JPanel dates = new JPanel();
        JPanel room = new JPanel();
        JLabel name = new JLabel("Room Name: ");
        JTextField nameTxt = new JTextField(15);
        nameTxt.setEditable(false);
        nameTxt.setText(roomName);
        room.add(name);
        room.add(nameTxt);
        JPanel datePair = new JPanel();
        name = new JLabel("Starting");
        JLabel monthLbl = new JLabel("Month: ");
        JComboBox month = monthCombo();
        JLabel dayLbl = new JLabel("Day: ");
        JComboBox day = dayCombo();
        JPanel buttonPanel = new JPanel();
        JButton submit = new JButton("Check Rates and Availability");
        submit.addActionListener(new RoomResListener1());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomRateListener0());
        buttonPanel.add(back);
        buttonPanel.add(submit);
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        name = new JLabel("Ending");
        monthLbl = new JLabel("Month: ");
        month = monthCombo();
        dayLbl = new JLabel("Day: ");
        day = dayCombo();
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        dates.add(room);
        dates.add(datePair);
        dates.add(buttonPanel);
        return dates;
    }

    class DateCheckListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(0);
            JPanel panel = (JPanel)overall.getComponent(1);
            JTextField roomTxt = (JTextField)panel.getComponent(1);
            String roomName = roomTxt.getText();
            options.removeAll();
            options.addTab("Rooms and Rates", addDateCheckPanel(roomName));
            options.addTab("Reservations", addReservePanel());
            options.validate();
        }
    }

    public JPanel addRoomDetail1(String roomName){
        JPanel room = new JPanel();
        room.setLayout(new GridLayout(7, 1));
        Room temp = RoomDB.getDet(conn, roomName);
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Room ID: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText(temp.roomID);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        room.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Name: ");
        JTextField roomTxt = new JTextField(20);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.roomName);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        room.add(roomPanel);
        JPanel bedsPanel = new JPanel();
        JLabel bedsLbl = new JLabel("Beds: ");
        JTextField bedsTxt = new JTextField(10);
        bedsTxt.setEditable(false);
        bedsTxt.setText("" + temp.beds);
        bedsPanel.add(bedsLbl);
        bedsPanel.add(bedsTxt);
        room.add(bedsPanel);
        JPanel bedTypePanel = new JPanel();
        JLabel bedTypeLbl = new JLabel("Bed Type: ");
        JTextField bedTypeTxt = new JTextField(10);
        bedTypeTxt.setEditable(false);
        bedTypeTxt.setText("" + temp.bedType);
        bedTypePanel.add(bedTypeLbl);
        bedTypePanel.add(bedTypeTxt);
        room.add(bedTypePanel);
        JPanel maxPanel = new JPanel();
        JLabel maxLbl = new JLabel("Max Occupancy: ");
        JTextField maxTxt = new JTextField(10);
        maxTxt.setEditable(false);
        maxTxt.setText("" + temp.maxOccupancy);
        maxPanel.add(maxLbl);
        maxPanel.add(maxTxt);
        room.add(maxPanel);
        JPanel pricePanel = new JPanel();
        JLabel priceLbl = new JLabel("Base Price: ");
        JTextField priceTxt = new JTextField(10);
        priceTxt.setEditable(false);
        priceTxt.setText("" + temp.basePrice);
        pricePanel.add(priceLbl);
        pricePanel.add(priceTxt);
        room.add(pricePanel);
        JPanel decorPanel = new JPanel();
        JLabel decorLbl = new JLabel("Decor: ");
        JTextField decorTxt = new JTextField(15);
        decorTxt.setEditable(false);
        decorTxt.setText(temp.decor);
        decorPanel.add(decorLbl);
        decorPanel.add(decorTxt);
        room.add(decorPanel);
        JPanel buttonPanel = new JPanel();
        JButton check = new JButton("Check Availability");
        check.addActionListener(new DateCheckListener());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomRateListener0());
        buttonPanel.add(back);
        buttonPanel.add(check);
        room.add(buttonPanel);
        return room;
    }

    class RoomDetailListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(0);
            JPanel panel = (JPanel)overall.getComponent(0);
            JScrollPane scroll = (JScrollPane)panel.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                String roomName = (String)table.getValueAt(table.getSelectedRow(), 0);
                options.removeAll();
                options.addTab("Rooms and Rates", addRoomDetail1(roomName));
                options.addTab("Reservations", addReservePanel());
                options.validate();
            }
        }
    }

    public JPanel addRoomRates(){
        JPanel roomRates = new JPanel();
        JPanel table = new JPanel();
        Vector<Vector> rowData = RoomDB.getRooms(conn);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Room Name");
        JTable rooms = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(rooms);
        table.add(scroll);
        JPanel button = new JPanel();
        JButton check = new JButton("Room Information");
        check.addActionListener(new RoomDetailListener1());
        button.add(check);
        roomRates.add(table);
        roomRates.add(button);
        return roomRates;
    }

    class ReloadRoomRateListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            options.removeAll();
            options.addTab("Rooms and Rates", addRoomRates());
            options.addTab("Reservations", addReservePanel());
            options.validate();
            options.setSelectedIndex(1);
        }
    }

    class ReloadRoomRateListener0 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)guest.getComponent(0);
            options.removeAll();
            options.addTab("Rooms and Rates", addRoomRates());
            options.addTab("Reservations", addReservePanel());
            options.validate();
            options.setSelectedIndex(0);
        }
    }

    public JPanel addOwnerPanel(){
        JPanel owner = new JPanel();
        owner.setLayout(new BorderLayout());
        JTabbedPane options = new JTabbedPane();
        options.addTab("Occupancy", addOccupancy());
        options.addTab("Revenue", addRevenue());
        options.addTab("Reservations", addResPanel());
        options.addTab("Rooms", addRoomPanel());
        owner.add(options, BorderLayout.CENTER);
        return owner;
    }

    class DetailListener3 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(3);
            JPanel data = (JPanel)overall.getComponent(0);
            JScrollPane scroll = (JScrollPane)data.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                Integer code = (Integer)table.getValueAt(table.getSelectedRow(), 0);
                options.removeAll();
                options.addTab("Occupancy", addOccupancy());
                options.addTab("Revenue", addRevenue());
                options.addTab("Reservations", addResPanel());
                options.addTab("Rooms", resInfo3(code));
                options.validate();
                options.setSelectedIndex(3);
            }
        }
    }

    public JPanel resInfo3(int code){
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(9, 1));
        Reservation temp = ResDB.getDetail(conn, code);
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Code: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText("" + temp.code);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        res.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Room: ");
        JTextField roomTxt = new JTextField(10);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.room);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        res.add(roomPanel);
        JPanel checkInPanel = new JPanel();
        JLabel checkInLbl = new JLabel("Check In: ");
        JTextField checkInTxt = new JTextField(10);
        checkInTxt.setEditable(false);
        checkInTxt.setText("" + temp.checkIn);
        checkInPanel.add(checkInLbl);
        checkInPanel.add(checkInTxt);
        res.add(checkInPanel);
        JPanel checkOutPanel = new JPanel();
        JLabel checkOutLbl = new JLabel("Check Out: ");
        JTextField checkOutTxt = new JTextField(10);
        checkOutTxt.setEditable(false);
        checkOutTxt.setText("" + temp.checkOut);
        checkOutPanel.add(checkOutLbl);
        checkOutPanel.add(checkOutTxt);
        res.add(checkOutPanel);
        JPanel ratePanel = new JPanel();
        JLabel rateLbl = new JLabel("Rate: ");
        JTextField rateTxt = new JTextField(10);
        rateTxt.setEditable(false);
        rateTxt.setText("" + temp.rate);
        ratePanel.add(rateLbl);
        ratePanel.add(rateTxt);
        JPanel lastPanel = new JPanel();
        JLabel lastLbl = new JLabel("Lastname: ");
        JTextField lastTxt = new JTextField(15);
        lastTxt.setEditable(false);
        lastTxt.setText(temp.lastName);
        lastPanel.add(lastLbl);
        lastPanel.add(lastTxt);
        res.add(lastPanel);
        JPanel firstPanel = new JPanel();
        JLabel firstLbl = new JLabel("Firstname: ");
        JTextField firstTxt = new JTextField(15);
        firstTxt.setEditable(false);
        firstTxt.setText(temp.firstName);
        firstPanel.add(firstLbl);
        firstPanel.add(firstTxt);
        res.add(firstPanel);
        JPanel adultsPanel = new JPanel();
        JLabel adultsLbl = new JLabel("Adults: ");
        JTextField adultsTxt = new JTextField(10);
        adultsTxt.setEditable(false);
        adultsTxt.setText("" + temp.adults);
        adultsPanel.add(adultsLbl);
        adultsPanel.add(adultsTxt);
        res.add(adultsPanel);
        JPanel kidsPanel = new JPanel();
        JLabel kidsLbl = new JLabel("Kids: ");
        JTextField kidsTxt = new JTextField(10);
        kidsTxt.setEditable(false);
        kidsTxt.setText("" + temp.kids);
        kidsPanel.add(kidsLbl);
        kidsPanel.add(kidsTxt);
        res.add(kidsPanel);
        res.add(ratePanel);
        JPanel buttonPanel = new JPanel();
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomListener());
        buttonPanel.add(back);
        res.add(buttonPanel);
        return res;
    }

    class RoomResListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(3);
            JScrollPane scroll = (JScrollPane)overall.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                String roomName = (String)table.getValueAt(table.getSelectedRow(), 0);
                options.removeAll();
                options.addTab("Occupancy", addOccupancy());
                options.addTab("Revenue", addRevenue());
                options.addTab("Reservations", addResPanel());
                options.addTab("Rooms", addRoomRes(roomName));
                options.validate();
                options.setSelectedIndex(3);
            }
        }
    }

    public JPanel addRoomRes(String roomName){
        JPanel overall = new JPanel();
        JPanel data = new JPanel();
        Vector<Vector> rowData = ResDB.resRoom(conn, roomName);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Code");
        columnNames.add("Room");
        columnNames.add("Check In");
        columnNames.add("Check Out");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        data.add(scroll);
        JButton detail = new JButton("View Reservation Details");
        detail.addActionListener(new DetailListener3());
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(detail);
        overall.add(data);
        overall.add(buttonPanel);
        return overall;
    }

    public JPanel addRoomDetail(String roomName){
        JPanel room = new JPanel();
        room.setLayout(new GridLayout(7, 1));
        Room temp = RoomDB.getDet(conn, roomName);
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Room ID: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText(temp.roomID);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        room.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Name: ");
        JTextField roomTxt = new JTextField(20);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.roomName);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        room.add(roomPanel);
        JPanel bedsPanel = new JPanel();
        JLabel bedsLbl = new JLabel("Beds: ");
        JTextField bedsTxt = new JTextField(10);
        bedsTxt.setEditable(false);
        bedsTxt.setText("" + temp.beds);
        bedsPanel.add(bedsLbl);
        bedsPanel.add(bedsTxt);
        room.add(bedsPanel);
        JPanel bedTypePanel = new JPanel();
        JLabel bedTypeLbl = new JLabel("Bed Type: ");
        JTextField bedTypeTxt = new JTextField(10);
        bedTypeTxt.setEditable(false);
        bedTypeTxt.setText("" + temp.bedType);
        bedTypePanel.add(bedTypeLbl);
        bedTypePanel.add(bedTypeTxt);
        room.add(bedTypePanel);
        JPanel maxPanel = new JPanel();
        JLabel maxLbl = new JLabel("Max Occupancy: ");
        JTextField maxTxt = new JTextField(10);
        maxTxt.setEditable(false);
        maxTxt.setText("" + temp.maxOccupancy);
        maxPanel.add(maxLbl);
        maxPanel.add(maxTxt);
        room.add(maxPanel);
        JPanel pricePanel = new JPanel();
        JLabel priceLbl = new JLabel("Base Price: ");
        JTextField priceTxt = new JTextField(10);
        priceTxt.setEditable(false);
        priceTxt.setText("" + temp.basePrice);
        pricePanel.add(priceLbl);
        pricePanel.add(priceTxt);
        room.add(pricePanel);
        JPanel decorPanel = new JPanel();
        JLabel decorLbl = new JLabel("Decor: ");
        JTextField decorTxt = new JTextField(15);
        decorTxt.setEditable(false);
        decorTxt.setText(temp.decor);
        decorPanel.add(decorLbl);
        decorPanel.add(decorTxt);
        room.add(decorPanel);
        JPanel buttonPanel = new JPanel();
        JButton back = new JButton("Select a Different Room");
        back.addActionListener(new ReloadRoomListener());
        buttonPanel.add(back);
        room.add(buttonPanel);
        return room;
    }

    class RoomDetailListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(3);
            JScrollPane scroll = (JScrollPane)overall.getComponent(0);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                String roomName = (String)table.getValueAt(table.getSelectedRow(), 0);
                options.removeAll();
                options.addTab("Occupancy", addOccupancy());
                options.addTab("Revenue", addRevenue());
                options.addTab("Reservations", addResPanel());
                options.addTab("Rooms", addRoomDetail(roomName));
                options.validate();
                options.setSelectedIndex(3);
            }
        }
    }

    public JPanel addRoomPanel(){
        JPanel room = new JPanel();
        Vector<Vector> rowData = RoomDB.getRooms(conn);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Room Name");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        JButton roomInfo = new JButton("Room Information");
        roomInfo.addActionListener(new RoomDetailListener());
        JButton roomRes = new JButton("Room Reservations");
        roomRes.addActionListener(new RoomResListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(roomInfo);
        buttonPanel.add(roomRes);
        room.add(scroll);
        room.add(buttonPanel);
        return room;
    }

    class ReloadRoomListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            options.removeAll();
            options.addTab("Occupancy", addOccupancy());
            options.addTab("Revenue", addRevenue());
            options.addTab("Reservations", addResPanel());
            options.addTab("Rooms", addRoomPanel());
            options.validate();
            options.setSelectedIndex(3);
        }
    }

    public JPanel resInfo2(int code){
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(9, 1));
        Reservation temp = ResDB.getDetail(conn, code);
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Code: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText("" + temp.code);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        res.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Room: ");
        JTextField roomTxt = new JTextField(10);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.room);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        res.add(roomPanel);
        JPanel checkInPanel = new JPanel();
        JLabel checkInLbl = new JLabel("Check In: ");
        JTextField checkInTxt = new JTextField(10);
        checkInTxt.setEditable(false);
        checkInTxt.setText("" + temp.checkIn);
        checkInPanel.add(checkInLbl);
        checkInPanel.add(checkInTxt);
        res.add(checkInPanel);
        JPanel checkOutPanel = new JPanel();
        JLabel checkOutLbl = new JLabel("Check Out: ");
        JTextField checkOutTxt = new JTextField(10);
        checkOutTxt.setEditable(false);
        checkOutTxt.setText("" + temp.checkOut);
        checkOutPanel.add(checkOutLbl);
        checkOutPanel.add(checkOutTxt);
        res.add(checkOutPanel);
        JPanel ratePanel = new JPanel();
        JLabel rateLbl = new JLabel("Rate: ");
        JTextField rateTxt = new JTextField(10);
        rateTxt.setEditable(false);
        rateTxt.setText("" + temp.rate);
        ratePanel.add(rateLbl);
        ratePanel.add(rateTxt);
        JPanel lastPanel = new JPanel();
        JLabel lastLbl = new JLabel("Lastname: ");
        JTextField lastTxt = new JTextField(15);
        lastTxt.setEditable(false);
        lastTxt.setText(temp.lastName);
        lastPanel.add(lastLbl);
        lastPanel.add(lastTxt);
        res.add(lastPanel);
        JPanel firstPanel = new JPanel();
        JLabel firstLbl = new JLabel("Firstname: ");
        JTextField firstTxt = new JTextField(15);
        firstTxt.setEditable(false);
        firstTxt.setText(temp.firstName);
        firstPanel.add(firstLbl);
        firstPanel.add(firstTxt);
        res.add(firstPanel);
        JPanel adultsPanel = new JPanel();
        JLabel adultsLbl = new JLabel("Adults: ");
        JTextField adultsTxt = new JTextField(10);
        adultsTxt.setEditable(false);
        adultsTxt.setText("" + temp.adults);
        adultsPanel.add(adultsLbl);
        adultsPanel.add(adultsTxt);
        res.add(adultsPanel);
        JPanel kidsPanel = new JPanel();
        JLabel kidsLbl = new JLabel("Kids: ");
        JTextField kidsTxt = new JTextField(10);
        kidsTxt.setEditable(false);
        kidsTxt.setText("" + temp.kids);
        kidsPanel.add(kidsLbl);
        kidsPanel.add(kidsTxt);
        res.add(kidsPanel);
        res.add(ratePanel);
        JPanel buttonPanel = new JPanel();
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadResListener());
        buttonPanel.add(back);
        res.add(buttonPanel);
        return res;
    }

    class DetailListener2 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JPanel overall = (JPanel)options.getComponent(2);
            JScrollPane scroll = (JScrollPane)overall.getComponent(4);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                Integer code = (Integer)table.getValueAt(table.getSelectedRow(), 0);
                options.removeAll();
                options.addTab("Occupancy", addOccupancy());
                options.addTab("Revenue", addRevenue());
                options.addTab("Reservations", resInfo2(code));
                options.addTab("Rooms", addRoomPanel());
                options.validate();
                options.setSelectedIndex(2);
            }
        }
    }

    public JPanel addResDetail(String start, String end){
        JPanel overall = new JPanel();
        JLabel dateLbls = new JLabel("Starting Date: ");
        JTextField dateTxts = new JTextField(10);
        JLabel dateLble = new JLabel("Ending Date: ");
        JTextField dateTxte = new JTextField(10);
        dateTxts.setEditable(false);
        dateTxte.setEditable(false);
        dateTxts.setText(start);
        dateTxte.setText(end);
        Vector<Vector> rowData = ResDB.resInterval(conn, start, end);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Code");
        columnNames.add("Room");
        columnNames.add("Check In");
        columnNames.add("Check Out");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        JButton detail = new JButton("View Reservation Details");
        detail.addActionListener(new DetailListener2());
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadResListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(detail);
        overall.add(dateLbls);
        overall.add(dateTxts);
        overall.add(dateLble);
        overall.add(dateTxte);
        overall.add(scroll);
        overall.add(buttonPanel);
        return overall;
    }

    class OR3Listener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JPanel resPanel = (JPanel)options.getComponent(2);
            JComboBox month = (JComboBox)resPanel.getComponent(2);
            JComboBox day = (JComboBox)resPanel.getComponent(4);
            String m1 = (String)month.getSelectedItem();
            String d1 = (String)day.getSelectedItem();
            Integer mi1 = Integer.parseInt(month(m1));
            Integer di1 = Integer.parseInt(d1);
            month = (JComboBox)resPanel.getComponent(7);
            day = (JComboBox)resPanel.getComponent(9);
            String m2 = (String)month.getSelectedItem();
            String d2 = (String)day.getSelectedItem();
            Integer mi2 = Integer.parseInt(month(m2));
            Integer di2 = Integer.parseInt(d2);
            if((m1.equals("FEB") && di1 > 28) || (m2.equals("FEB") && di2 > 28))
                ;//do nothing
            else if(((m1.equals("APR") || m1.equals("JUN") || 
                      m1.equals("SEP") || m1.equals("NOV")) && di1 > 30) || 
		    ((m2.equals("APR") || m2.equals("JUN") ||
                      m2.equals("SEP") || m2.equals("NOV")) && di2 > 30))
                ;//do nothing
            else if((mi1 == mi2 && di1 > di2) ||
                    (mi1 > mi2))
                ;//do nothing
            else{
                String date1 = ("2010-" + month(m1) + "-" + d1);
                String date2 = ("2010-" + month(m2) + "-" + d2);
                options.removeAll();
                options.addTab("Occupancy", addOccupancy());
                options.addTab("Revenue", addRevenue());
                options.addTab("Reservations", addResDetail(date1, date2));
                options.addTab("Rooms", addRoomPanel());
                options.validate();
                options.setSelectedIndex(2);
            }
        }
    }

    public JPanel addResPanel(){
        JPanel datePair = new JPanel();
        JLabel name = new JLabel("Starting");
        JLabel monthLbl = new JLabel("Month: ");
        JComboBox month = monthCombo();
        JLabel dayLbl = new JLabel("Day: ");
        JComboBox day = dayCombo();
        JButton submit = new JButton("Reservations");
        submit.addActionListener(new OR3Listener());
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        name = new JLabel("Ending");
        monthLbl = new JLabel("Month: ");
        month = monthCombo();
        dayLbl = new JLabel("Day: ");
        day = dayCombo();
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        datePair.add(submit);
        return datePair;
    }

    class ReloadResListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            options.removeAll();
            options.addTab("Occupancy", addOccupancy());
            options.addTab("Revenue", addRevenue());
            options.addTab("Reservations", addResPanel());
            options.addTab("Rooms", addRoomPanel());
            options.validate();
            options.setSelectedIndex(2);
        }
    }

    public JTabbedPane addOccupancy(){
        JTabbedPane occupancy = new JTabbedPane();
        occupancy.addTab("Specific Date", addSingleDate());
        occupancy.addTab("Date Range", addDatePair());
        return occupancy;
    }

    public JPanel resInfo1(int code){
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(9, 1));
        Reservation temp = ResDB.getDetail(conn, code);
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Code: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText("" + temp.code);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        res.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Room: ");
        JTextField roomTxt = new JTextField(10);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.room);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        res.add(roomPanel);
        JPanel checkInPanel = new JPanel();
        JLabel checkInLbl = new JLabel("Check In: ");
        JTextField checkInTxt = new JTextField(10);
        checkInTxt.setEditable(false);
        checkInTxt.setText("" + temp.checkIn);
        checkInPanel.add(checkInLbl);
        checkInPanel.add(checkInTxt);
        res.add(checkInPanel);
        JPanel checkOutPanel = new JPanel();
        JLabel checkOutLbl = new JLabel("Check Out: ");
        JTextField checkOutTxt = new JTextField(10);
        checkOutTxt.setEditable(false);
        checkOutTxt.setText("" + temp.checkOut);
        checkOutPanel.add(checkOutLbl);
        checkOutPanel.add(checkOutTxt);
        res.add(checkOutPanel);
        JPanel ratePanel = new JPanel();
        JLabel rateLbl = new JLabel("Rate: ");
        JTextField rateTxt = new JTextField(10);
        rateTxt.setEditable(false);
        rateTxt.setText("" + temp.rate);
        ratePanel.add(rateLbl);
        ratePanel.add(rateTxt);
        JPanel lastPanel = new JPanel();
        JLabel lastLbl = new JLabel("Lastname: ");
        JTextField lastTxt = new JTextField(15);
        lastTxt.setEditable(false);
        lastTxt.setText(temp.lastName);
        lastPanel.add(lastLbl);
        lastPanel.add(lastTxt);
        res.add(lastPanel);
        JPanel firstPanel = new JPanel();
        JLabel firstLbl = new JLabel("Firstname: ");
        JTextField firstTxt = new JTextField(15);
        firstTxt.setEditable(false);
        firstTxt.setText(temp.firstName);
        firstPanel.add(firstLbl);
        firstPanel.add(firstTxt);
        res.add(firstPanel);
        JPanel adultsPanel = new JPanel();
        JLabel adultsLbl = new JLabel("Adults: ");
        JTextField adultsTxt = new JTextField(10);
        adultsTxt.setEditable(false);
        adultsTxt.setText("" + temp.adults);
        adultsPanel.add(adultsLbl);
        adultsPanel.add(adultsTxt);
        res.add(adultsPanel);
        JPanel kidsPanel = new JPanel();
        JLabel kidsLbl = new JLabel("Kids: ");
        JTextField kidsTxt = new JTextField(10);
        kidsTxt.setEditable(false);
        kidsTxt.setText("" + temp.kids);
        kidsPanel.add(kidsLbl);
        kidsPanel.add(kidsTxt);
        res.add(kidsPanel);
        res.add(ratePanel);
        JPanel buttonPanel = new JPanel();
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadOccListener1());
        buttonPanel.add(back);
        res.add(buttonPanel);
        return res;
    }

    public JPanel resInfo(String roomID, String date){
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(9, 1));
        Reservation temp = ResDB.getDet(conn, roomID, java.sql.Date.valueOf(date));
        JPanel codePanel = new JPanel();
        JLabel codeLbl = new JLabel("Code: ");
        JTextField codeTxt = new JTextField(10);
        codeTxt.setEditable(false);
        codeTxt.setText("" + temp.code);
        codePanel.add(codeLbl);
        codePanel.add(codeTxt);
        res.add(codePanel);
        JPanel roomPanel = new JPanel();
        JLabel roomLbl = new JLabel("Room: ");
        JTextField roomTxt = new JTextField(10);
        roomTxt.setEditable(false);
        roomTxt.setText(temp.room);
        roomPanel.add(roomLbl);
        roomPanel.add(roomTxt);
        res.add(roomPanel);
        JPanel checkInPanel = new JPanel();
        JLabel checkInLbl = new JLabel("Check In: ");
        JTextField checkInTxt = new JTextField(10);
        checkInTxt.setEditable(false);
        checkInTxt.setText("" + temp.checkIn);
        checkInPanel.add(checkInLbl);
        checkInPanel.add(checkInTxt);
        res.add(checkInPanel);
        JPanel checkOutPanel = new JPanel();
        JLabel checkOutLbl = new JLabel("Check Out: ");
        JTextField checkOutTxt = new JTextField(10);
        checkOutTxt.setEditable(false);
        checkOutTxt.setText("" + temp.checkOut);
        checkOutPanel.add(checkOutLbl);
        checkOutPanel.add(checkOutTxt);
        res.add(checkOutPanel);
        JPanel ratePanel = new JPanel();
        JLabel rateLbl = new JLabel("Rate: ");
        JTextField rateTxt = new JTextField(10);
        rateTxt.setEditable(false);
        rateTxt.setText("" + temp.rate);
        ratePanel.add(rateLbl);
        ratePanel.add(rateTxt);
        JPanel lastPanel = new JPanel();
        JLabel lastLbl = new JLabel("Lastname: ");
        JTextField lastTxt = new JTextField(15);
        lastTxt.setEditable(false);
        lastTxt.setText(temp.lastName);
        lastPanel.add(lastLbl);
        lastPanel.add(lastTxt);
        res.add(lastPanel);
        JPanel firstPanel = new JPanel();
        JLabel firstLbl = new JLabel("Firstname: ");
        JTextField firstTxt = new JTextField(15);
        firstTxt.setEditable(false);
        firstTxt.setText(temp.firstName);
        firstPanel.add(firstLbl);
        firstPanel.add(firstTxt);
        res.add(firstPanel);
        JPanel adultsPanel = new JPanel();
        JLabel adultsLbl = new JLabel("Adults: ");
        JTextField adultsTxt = new JTextField(10);
        adultsTxt.setEditable(false);
        adultsTxt.setText("" + temp.adults);
        adultsPanel.add(adultsLbl);
        adultsPanel.add(adultsTxt);
        res.add(adultsPanel);
        JPanel kidsPanel = new JPanel();
        JLabel kidsLbl = new JLabel("Kids: ");
        JTextField kidsTxt = new JTextField(10);
        kidsTxt.setEditable(false);
        kidsTxt.setText("" + temp.kids);
        kidsPanel.add(kidsLbl);
        kidsPanel.add(kidsTxt);
        res.add(kidsPanel);
        res.add(ratePanel);
        JPanel buttonPanel = new JPanel();
        JButton back = new JButton("Enter A New Date");
        back.addActionListener(new ReloadOccListener0());
        buttonPanel.add(back);
        res.add(buttonPanel);
        return res;
    }

    class DetailListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            JPanel overall = (JPanel)occupancy.getComponent(1);
            JScrollPane scroll = (JScrollPane)overall.getComponent(4);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                Integer code = (Integer)table.getValueAt(table.getSelectedRow(), 0);
                occupancy.removeAll();
                occupancy.addTab("Specific Date", resInfo1(code));
                occupancy.addTab("Date Range", addDatePair());
                occupancy.validate();
            }
        }
    }

    class DetailListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            JPanel overall = (JPanel)occupancy.getComponent(0);
            JTextField dateTxt = (JTextField)overall.getComponent(1);
            JScrollPane scroll = (JScrollPane)overall.getComponent(2);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                if(((String) table.getValueAt(table.getSelectedRow(), 1)).equals("Empty"))
                    ;//do nothing
                else{
                    String roomID = (String)table.getValueAt(table.getSelectedRow(), 0);
                    String night = dateTxt.getText();
                    occupancy.removeAll();
                    occupancy.addTab("Specific Date", resInfo(roomID, night));
                    occupancy.addTab("Date Range", addDatePair());
                    occupancy.validate();
                }
            }
        }
    }

    public JPanel addPairResDetail(String roomID, String start, String end){
        JPanel overall = new JPanel();
        JLabel dateLbls = new JLabel("Starting Date: ");
        JTextField dateTxts = new JTextField(10);
        JLabel dateLble = new JLabel("Ending Date: ");
        JTextField dateTxte = new JTextField(10);
        dateTxts.setEditable(false);
        dateTxte.setEditable(false);
        dateTxts.setText(start);
        dateTxte.setText(end);
        Vector<Vector> rowData = ResDB.dateResRoom(conn, start, end, roomID);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Code");
        columnNames.add("Room");
        columnNames.add("Check In");
        columnNames.add("Check Out");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        JButton detail = new JButton("View Reservation Details");
        detail.addActionListener(new DetailListener1());
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadOccListener1());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(detail);
        overall.add(dateLbls);
        overall.add(dateTxts);
        overall.add(dateLble);
        overall.add(dateTxte);
        overall.add(scroll);
        overall.add(buttonPanel);
        return overall;
    }

    class OR1Listener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            JPanel overall = (JPanel)occupancy.getComponent(1);
            JTextField dateTxts = (JTextField)overall.getComponent(1);
            JTextField dateTxte = (JTextField)overall.getComponent(3);
            JScrollPane scroll = (JScrollPane)overall.getComponent(4);
            JViewport view = (JViewport)scroll.getComponent(0);
            JTable table = (JTable)view.getComponent(0);
            if(table.getSelectedRow() == -1)
                ;//do nothing
            else{
                if(((String) table.getValueAt(table.getSelectedRow(), 1)).equals("Empty"))
                    ;//do nothing
                else{
                    String roomID = (String)table.getValueAt(table.getSelectedRow(), 0);
                    String start = dateTxts.getText();
                    String end = dateTxte.getText();
                    occupancy.removeAll();
                    occupancy.addTab("Specific Date", addSingleDate());
                    occupancy.addTab("Date Range", addPairResDetail(roomID, start, end));
                    occupancy.validate();
                    occupancy.setSelectedIndex(1);
                }
            }
        }
    }

    public JPanel addPairDetail(String start, String end){
        JPanel overall = new JPanel();
        JLabel dateLbls = new JLabel("Starting Date: ");
        JTextField dateTxts = new JTextField(10);
        JLabel dateLble = new JLabel("Ending Date: ");
        JTextField dateTxte = new JTextField(10);
        dateTxts.setEditable(false);
        dateTxte.setEditable(false);
        dateTxts.setText(start);
        dateTxte.setText(end);
        Vector<Vector> rowData = ResDB.availCheck(conn, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end));
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Room");
        columnNames.add("Status");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        JButton detail = new JButton("View Reservation Details");
        detail.addActionListener(new OR1Listener1());
        JButton back = new JButton("Enter New Dates");
        back.addActionListener(new ReloadOccListener1());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(detail);
        overall.add(dateLbls);
        overall.add(dateTxts);
        overall.add(dateLble);
        overall.add(dateTxte);
        overall.add(scroll);
        overall.add(buttonPanel);
        return overall;
    }

    public JPanel addSingleDetail(String date){
        JPanel overall = new JPanel();
        JLabel dateLbl = new JLabel("Date: ");
        JTextField dateTxt = new JTextField(10);
        dateTxt.setEditable(false);
        dateTxt.setText(date);
        Vector<Vector> rowData = ResDB.dateRes(conn, java.sql.Date.valueOf(date));
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Room");
        columnNames.add("Status");
        JTable table = new NoEditTable(rowData, columnNames);
        JScrollPane scroll = new JScrollPane(table);
        JButton detail = new JButton("View Room Reservation Detail");
        detail.addActionListener(new DetailListener());
        JButton back = new JButton("Enter A New Date");
        back.addActionListener(new ReloadOccListener0());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(back);
        buttonPanel.add(detail);
        overall.add(dateLbl);
        overall.add(dateTxt);
        overall.add(scroll);
        overall.add(buttonPanel);
        return overall;
    }

    class SingleListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            JPanel singleDate = (JPanel)occupancy.getComponent(0);
            JComboBox month = (JComboBox)singleDate.getComponent(1);
            JComboBox day = (JComboBox)singleDate.getComponent(3);
            String m = (String)month.getSelectedItem();
            String d = (String)day.getSelectedItem();
            Integer mi = Integer.parseInt(month(m));
            Integer di = Integer.parseInt(d);
            if(m.equals("FEB") && di > 28)
                ;//do nothing
            else if((m.equals("APR") || m.equals("JUN") || m.equals("SEP") ||
                     m.equals("NOV")) && di > 30)
                ;//do nothing
            else{
                String date = ("2010-" + month(m) + "-" + d);
                occupancy.removeAll();
                occupancy.addTab("Specific Date", addSingleDetail(date));
                occupancy.addTab("Date Range", addDatePair());
                occupancy.validate();
            }
        }
    }

    class PairListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            JPanel datePair = (JPanel)occupancy.getComponent(1);
            JComboBox month = (JComboBox)datePair.getComponent(2);
            JComboBox day = (JComboBox)datePair.getComponent(4);
            String m1 = (String)month.getSelectedItem();
            String d1 = (String)day.getSelectedItem();
            Integer mi1 = Integer.parseInt(month(m1));
            Integer di1 = Integer.parseInt(d1);
            month = (JComboBox)datePair.getComponent(7);
            day = (JComboBox)datePair.getComponent(9);
            String m2 = (String)month.getSelectedItem();
            String d2 = (String)day.getSelectedItem();
            Integer mi2 = Integer.parseInt(month(m2));
            Integer di2 = Integer.parseInt(d2);
            if((m1.equals("FEB") && di1 > 28) || (m2.equals("FEB") && di2 > 28))
                ;//do nothing
            else if(((m1.equals("APR") || m1.equals("JUN") || 
                      m1.equals("SEP") || m1.equals("NOV")) && di1 > 30) || 
		    ((m2.equals("APR") || m2.equals("JUN") ||
                      m2.equals("SEP") || m2.equals("NOV")) && di2 > 30))
                ;//do nothing
            else if((mi1 == mi2 && di1 == di2) || 
                    (mi1 == mi2 && di1 > di2) ||
                    (mi1 > mi2))
                ;//do nothing
            else{
                String date1 = ("2010-" + month(m1) + "-" + d1);
                String date2 = ("2010-" + month(m2) + "-" + d2);
                occupancy.removeAll();
                occupancy.addTab("Specific Date", addSingleDate());
                occupancy.addTab("Date Range", addPairDetail(date1, date2));
                occupancy.validate();
                occupancy.setSelectedIndex(1);
            }
        }
    }

    public String month(String mon){
        if(mon.equals("JAN"))
            return "01";
        if(mon.equals("FEB"))
            return "02";
        if(mon.equals("MAR"))
            return "03";
        if(mon.equals("APR"))
            return "04";
        if(mon.equals("MAY"))
            return "05";
        if(mon.equals("JUN"))
            return "06";
        if(mon.equals("JUL"))
            return "07";
        if(mon.equals("AUG"))
            return "08";
        if(mon.equals("SEP"))
            return "09";
        if(mon.equals("OCT"))
            return "10";
        if(mon.equals("NOV"))
            return "11";
        if(mon.equals("DEC"))
            return "12";
        return "00";
    }

    class ReloadOccListener1 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            occupancy.removeAll();
            occupancy.addTab("Specific Date", addSingleDate());
            occupancy.addTab("Date Range", addDatePair());
            occupancy.validate();
            occupancy.setSelectedIndex(1);
        }
    }

    class ReloadOccListener0 implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JTabbedPane options = (JTabbedPane)owner.getComponent(0);
            JTabbedPane occupancy = (JTabbedPane)options.getComponent(0);
            occupancy.removeAll();
            occupancy.addTab("Specific Date", addSingleDate());
            occupancy.addTab("Date Range", addDatePair());
            occupancy.validate();
        }
    }

    public JPanel addSingleDate(){
        JPanel singleDate = new JPanel();
        JLabel monthLbl = new JLabel("Month: ");
        JComboBox month = monthCombo();
        JLabel dayLbl = new JLabel("Day: ");
        JComboBox day = dayCombo();
        JButton submit = new JButton("Availability");
        submit.addActionListener(new SingleListener());
        singleDate.add(monthLbl);
        singleDate.add(month);
        singleDate.add(dayLbl);
        singleDate.add(day);
        singleDate.add(submit);
        return singleDate;
    }

    public JPanel addDatePair(){
        JPanel datePair = new JPanel();
        JLabel name = new JLabel("Starting");
        JLabel monthLbl = new JLabel("Month: ");
        JComboBox month = monthCombo();
        JLabel dayLbl = new JLabel("Day: ");
        JComboBox day = dayCombo();
        JButton submit = new JButton("Availability");
        submit.addActionListener(new PairListener());
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        name = new JLabel("Ending");
        monthLbl = new JLabel("Month: ");
        month = monthCombo();
        dayLbl = new JLabel("Day: ");
        day = dayCombo();
        datePair.add(name);
        datePair.add(monthLbl);
        datePair.add(month);
        datePair.add(dayLbl);
        datePair.add(day);
        datePair.add(submit);
        return datePair;
    }

    public JComboBox monthCombo(){
        JComboBox month = new JComboBox();
        month.addItem("JAN");
        month.addItem("FEB");
        month.addItem("MAR");
        month.addItem("APR");
        month.addItem("MAY");
        month.addItem("JUN");
        month.addItem("JUL");
        month.addItem("AUG");
        month.addItem("SEP");
        month.addItem("OCT");
        month.addItem("NOV");
        month.addItem("DEC");
        return month;
    }

    public JComboBox dayCombo(){
        JComboBox day = new JComboBox();
        day.addItem("01");
        day.addItem("02");
        day.addItem("03");
        day.addItem("04");
        day.addItem("05");
        day.addItem("06");
        day.addItem("07");
        day.addItem("08");
        day.addItem("09");
        day.addItem("10");
        day.addItem("11");
        day.addItem("12");
        day.addItem("13");
        day.addItem("14");
        day.addItem("15");
        day.addItem("16");
        day.addItem("17");
        day.addItem("18");
        day.addItem("19");
        day.addItem("20");
        day.addItem("21");
        day.addItem("22");
        day.addItem("23");
        day.addItem("24");
        day.addItem("25");
        day.addItem("26");
        day.addItem("27");
        day.addItem("28");
        day.addItem("29");
        day.addItem("30");
        day.addItem("31");
        return day;
    }

    public JTabbedPane addRevenue(){
        JTabbedPane revenue = new JTabbedPane();
        Vector<String> months = new Vector<String>();
        months.add("");
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");
        months.add("TOT");
        JTable info = new NoEditTable(ResDB.getRev(conn), months);
        JScrollPane scroll = new JScrollPane(info);
        revenue.addTab("Cash Flow", scroll);
        info = new NoEditTable(ResDB.getRes(conn), months);
        scroll = new JScrollPane(info);
        revenue.addTab("Reservations", scroll);
        return revenue;
    }

    public JPanel addAdminPanel(){
        JPanel admin = new JPanel();
        admin.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8,1));
        addButtons(buttonPanel);
        admin.add(buttonPanel, BorderLayout.WEST);
        JTabbedPane options = new JTabbedPane();
        addOptions(options);
        admin.add(options, BorderLayout.CENTER);
        return admin;
    }

    public void addButtons(JPanel buttonPanel){
        JPanel button = new JPanel();
        buttonPanel.add(button);
        button = new JPanel();
        buttonPanel.add(button);
        button = new JPanel();
        JButton temp = new JButton("Load Database");
        temp.addActionListener(new CreateDBListener());
        button.add(temp);
        buttonPanel.add(button);
        button = new JPanel();
        temp = new JButton("Clear Database");
        temp.addActionListener(new ClearDBListener());
        button.add(temp);
        buttonPanel.add(button);
        button = new JPanel();
        temp = new JButton("Delete Database");
        temp.addActionListener(new DeleteDBListener());
        button.add(temp);
        buttonPanel.add(button);
	temp = new JButton("Owner");
	button = new JPanel();
	temp.addActionListener(new OwnerListener());
	button.add(temp);
	buttonPanel.add(button);
	button = new JPanel();
	temp = new JButton("Guest");
	temp.addActionListener(new GuestListener());
	button.add(temp);
	buttonPanel.add(button);
    }

    public void addOptions(JTabbedPane options){
        addStatus(options);
        addReservations(options);
        addRooms(options);
    }

    public void addStatus(JTabbedPane options){
        JPanel statusDB = new JPanel();
        statusDB.setLayout(new GridLayout(7,1));
        JPanel overall = new JPanel();
        JLabel statusLbl = new JLabel("Current Status: ");
        JTextField status = new JTextField(10);
        status.setEditable(false);
        JPanel reservations = new JPanel();
        JLabel resCountLbl = new JLabel("Reservations: ");
        JTextField resCount = new JTextField(10);
        resCount.setEditable(false);
        JPanel rooms = new JPanel();
        JLabel roomCountLbl = new JLabel("Rooms: ");
        JTextField roomCount = new JTextField(10);
        roomCount.setEditable(false);
        Integer room = RoomDB.exist(conn);
        Integer res = ResDB.exist(conn);
        if(room == -1 && res == -1){
            status.setText("No database");
            resCount.setText("N/A");
            roomCount.setText("N/A");
        }
        else if(room == 0 && res == 0){
            status.setText("Empty");
            resCount.setText(res.toString());
            roomCount.setText(room.toString());
        }
        else{
            status.setText("Full");
            resCount.setText(res.toString());
            roomCount.setText(room.toString());
        }
        overall.add(statusLbl);
        overall.add(status);
        reservations.add(resCountLbl);
        reservations.add(resCount);
        rooms.add(roomCountLbl);
        rooms.add(roomCount);
        statusDB.add(new JPanel());
        statusDB.add(new JPanel());
        statusDB.add(overall);
        statusDB.add(reservations);
        statusDB.add(rooms);
        options.addTab("Current System Status", statusDB);
    }

    public void addReservations(JTabbedPane options){
        JPanel reserve = new JPanel();
        reserve.setLayout(new BorderLayout());
        Vector<String> columnNames = new Vector<String> ();
        columnNames.add("Code");
        columnNames.add("Room");
        columnNames.add("Check In");
        columnNames.add("Check Out");
        columnNames.add("Rate");
        columnNames.add("Last Name");
        columnNames.add("First Name");
        columnNames.add("Adults");
        columnNames.add("Kids");
        Vector<Vector> rowData = ResDB.display(ResDB.getAll(conn));
        JTable resTable = new NoEditTable(rowData, columnNames);
        JScrollPane resScroll = new JScrollPane(resTable);
        reserve.add(resScroll, BorderLayout.CENTER);
        options.addTab("View Reservations", reserve);
    }

    public void addRooms(JTabbedPane options){
        JPanel room = new JPanel();
        room.setLayout(new BorderLayout());
        Vector<String> columnNames = new Vector<String> ();
        columnNames.add("Room ID");
        columnNames.add("Room Name");
        columnNames.add("Beds");
        columnNames.add("Bed Type");
        columnNames.add("Max Occupancy");
        columnNames.add("Base Price");
        columnNames.add("Decor");
        Vector<Vector> rowData = RoomDB.display(RoomDB.getAll(conn));
        JTable roomTable = new NoEditTable(rowData, columnNames);
        JScrollPane roomScroll = new JScrollPane(roomTable);
        room.add(roomScroll, BorderLayout.CENTER);
        options.addTab("View Rooms", room);
    }

    public void checkDB(){
        admin.remove(1);
        JTabbedPane options = new JTabbedPane();
        addOptions(options);
        admin.add(options, BorderLayout.CENTER);
        admin.validate();
        tabbedPane.remove(owner);
        tabbedPane.remove(guest);
        tabbedPane.remove(exit);
        owner = addOwnerPanel();
        tabbedPane.addTab("Owner", owner);
        guest = addGuestPanel();
        tabbedPane.addTab("Guest", guest);
        tabbedPane.addTab("Leave", exit);
	tabbedPane.setEnabledAt(1, false);
	tabbedPane.setEnabledAt(2, false);
    }

    class CreateDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            int status = InnReservations.loadCheck(conn);
            if(status == -1 || status == 0)
                checkDB();
            else{
                JTabbedPane options = (JTabbedPane)admin.getComponent(1);
                JPanel view = (JPanel)options.getComponent(0);
                int count = java.lang.reflect.Array.getLength((Object)view.getComponents());
                if(count == 5){
                    JPanel note = new JPanel();
                    JLabel message = new JLabel("*The tables already exist and contain information in them");
                    note.add(message);
                    view.add(note);
                    view.validate();
                }
            }
                    
        }
    }

    class OwnerListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
	    tabbedPane.setEnabledAt(1, true);
	    tabbedPane.setSelectedIndex(1);
	    tabbedPane.setEnabledAt(0, false);
        }
    }

    class GuestListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
	    tabbedPane.setEnabledAt(2, true);
	    tabbedPane.setSelectedIndex(2);
	    tabbedPane.setEnabledAt(0, false);
        }
    }

    class ClearDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            InnReservations.tableClear(conn);
            checkDB();
        }
    }
    class DeleteDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            InnReservations.dropDB(conn);
            checkDB();
        }
    }

    class ExitListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            try{
                conn.commit();
                conn.close();
            }
            catch(SQLException ex){
            }
            System.exit(0);
        }
    }

    public JPanel addExitPanel(){
        JPanel exit = new JPanel();
        JPanel text = new JPanel();
        JLabel message = new JLabel("Thank you for using our reservation system!");
        text.add(message);
        JPanel buttonPanel = new JPanel();
        JButton leave = new JButton("Leave System");
        leave.addActionListener(new ExitListener());
        buttonPanel.add(leave);
        exit.add(text);
        exit.add(buttonPanel);
        return exit;
    }

}
