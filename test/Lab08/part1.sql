/*
 Halli Meth
 Mark Lerner
 Michael Gage
 CPE 365-01
 Spring 2011
 Lab 08
 */

declare

begin
    INNOperations.Find_Cheapest('10-FEB-2010', '15-FEB-2010', 1);
    INNOperations.Find_Cheapest('01-MAY-2010', '04-MAY-2010', 3);
    INNOperations.Find_Cheapest('01-JUL-2010', '05-JUL-2010', 4);
    INNOperations.Find_Cheapest('06-SEP-2010', '10-SEP-2010', 2);
    INNOperations.Find_Cheapest('01-JAN-2010', '08-JAN-2010', 2);
    INNOperations.Find_Cheapest('01-JAN-2010', '02-JAN-2010', 5);--too many occupants
    INNOperations.Find_Cheapest('01-JAN-2010', '31-DEC-2010', 3);--no available rooms
    INNOperations.Find_Cheapest('13-JAN-2010', '22-JAN-2010', 2);--two minimum rooms

end;
/