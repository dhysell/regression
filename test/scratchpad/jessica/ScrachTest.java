package scratchpad.jessica;

import java.text.SimpleDateFormat;
import java.util.Calendar;
public class ScrachTest {

public static void main(String[] args) {
	int i=0;
    try {  
        
             if(i==0){
                System.out.println("test here");
                throw new Exception("it is an exception");
             }            
        
    } catch (Exception e) {
           System.err.println("An exception was thrown");
      }
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	// create a calendar
    Calendar cal = Calendar.getInstance();		  
    
    System.out.println("The current date is : " + sdf.format(cal.getTime()));
    cal.add(Calendar.DAY_OF_YEAR, 6);
    String t1 = sdf.format(cal.getTime());	     
    System.out.println("new time is : " + t1);	    
    
    System.out.println("\"new time is : \"");	 
    

}
}
