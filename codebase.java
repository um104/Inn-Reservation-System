import javax.swing.*;

public class codebase{

    public static JComboBox monthDropdown(){
        JComboBox month = new JComboBox();
        month.addItem("Select Month:");
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

    public static  String monthConvert(String monthAbbr){
        if(monthAbbr.equals("JAN"))
            return "01";
        if(monthAbbr.equals("FEB"))
            return "02";
        if(monthAbbr.equals("MAR"))
            return "03";
        if(monthAbbr.equals("APR"))
            return "04";
        if(monthAbbr.equals("MAY"))
            return "05";
        if(monthAbbr.equals("JUN"))
            return "06";
        if(monthAbbr.equals("JUL"))
            return "07";
        if(monthAbbr.equals("AUG"))
            return "08";
        if(monthAbbr.equals("SEP"))
            return "09";
        if(monthAbbr.equals("OCT"))
            return "10";
        if(monthAbbr.equals("NOV"))
            return "11";
        else
            return "12";
    }

    public static JComboBox dayDropdown(){
        JComboBox day = new JComboBox();
        day.addItem("Select Day:");
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
}
