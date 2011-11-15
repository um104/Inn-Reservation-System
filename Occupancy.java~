import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class Occupancy implements ActionListener
{
   JTextArea content; 
   JScrollPane contentScrollable;
   Connection C;

   JComboBox startMonth = codebase.monthDropdown();
   JComboBox startDay = codebase.dayDropdown();
   JComboBox endMonth = codebase.monthDropdown();
   JComboBox endDay = codebase.dayDropdown();
   JFrame f = new JFrame();
   JFrame f2 = new JFrame();
   public Occupancy(JTextArea content, JScrollPane contentScrollable, Connection C){
      this.content = content;
      this.contentScrollable = contentScrollable;
      this.C = C;

      JButton submit = new JButton("Submit");
      submit.addActionListener(this);
      
      JPanel p = new JPanel();
      p.add(startMonth);
      p.add(startDay);
      p.add(endMonth);
      p.add(endDay);
      p.add(submit);
      

      f.add(p);
      f.setSize(760,55);
      f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        content.setText("Results of query...");
        String M1 = (String)startMonth.getSelectedItem();
        String D1 = (String)startDay.getSelectedItem();
        String M2 = (String)endMonth.getSelectedItem();
        String D2 = (String)endDay.getSelectedItem();
        if(!(M1.equals("Select Month:")) && !(D1.equals("Select Day:"))){
           if(M2.equals("Select Month:") && D2.equals("Select Day:")){
              ArrayList<String> rooms = ResDB.dateRes(C, ("2010-"+codebase.monthConvert(M1)+"-"+D1));
              JRadioButton[] jrb = new JRadioButton[rooms.size()];
              for(int i = 0; i < rooms.size(); i++){
                 jrb[i] = new JRadioButton(rooms.get(i));
              }

              ButtonGroup bgroup = new ButtonGroup();
              JPanel roomPanel = new JPanel();
              roomPanel.setLayout(new GridLayout(10, 1));
              for(int i = 0; i < rooms.size(); i++){
                 bgroup.add(jrb[i]);
                 roomPanel.add(jrb[i]);
              }

              f2.add(roomPanel);
              f2.setSize(760,630);
              f2.setVisible(true);
              f.setVisible(false);
           }
           else if(!(M2.equals("Select Month:")) && !(D2.equals("Select Day:"))){

              f.setVisible(false);
           }
        }
        else{
           ;// Do nothing (invalid input)
        }
    }

}

