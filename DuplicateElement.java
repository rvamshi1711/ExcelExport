import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DuplicateElement {  
	public String numTo(int mn)
	{
		String month="";
		switch (mn) {
        case 1:
          //System.out.println("JAN");
          month = "JAN";
          break;
        case 2:
          //System.out.println("FEB");
          month = "FEB";
          break;
        case 3:
          //System.out.println("MAR");
          month = "MAR";
          break;
        case 4:
          //System.out.println("APR");
          month = "APR";
          break;
        case 5:
          //System.out.println("MAY");
          month = "MAY";
          break;
        case 6:
          //System.out.println("JUN");
          month = "JUN";
          break;
        case 7:
          //System.out.println("JUL");
          month = "JUL";
          break;
        case 8:
            //System.out.println("AUG");
            month = "AUG";
            break;
        case 9:
            //System.out.println("SEP");
            month = "SEP";
            break;
        case 10:
            //System.out.println("OCT");
            month = "OCT";
            break;
        case 11:
            //System.out.println("NOV");
            month = "NOV";
            break;
        case 12:
            //System.out.println("DEC");
            month = "DEC";
            break;
      }
		return month;
	}
	
	public String createStr(String f1)
	{
		//String f = "TO_ML_NEW_19_01_2022";
        String month ="";
        String total="";
        
        //String f1 = "19042022metlife";
        //System.out.println(f1.substring(8,15));
        
        if(f1.substring(0, 10).equals("TO_ML_NEW_"))
        {
        	
        	int mn = Integer.parseInt(f1.substring(13, 15)) ;
        	
        	month =  numTo(mn);
          
        	total = "PNB "+f1.substring(10, 12)+month+f1.substring(18, 20);
        }
        else if(f1.substring(8,15).equals("metlife"))
        {
        	int mn = Integer.parseInt(f1.substring(2, 4)) ;
        	
        	month =  numTo(mn);
          
        	total = "JKB "+f1.substring(0, 2)+month+f1.substring(6, 8);
        }
        
        
        //System.out.println(total);
		return total;
	}
	
    public static void main(String[] args) {      
        
    	String name= "19042022metlife";
    	String f = "TO_ML_NEW_19_01_2022";
        DuplicateElement e = new DuplicateElement();
        String t= e.createStr(name);
        System.out.println(t);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
        Date date = new Date();
        String da = formatter.format(date);
        System.out.println(da);
        int db = Integer.parseInt(da.substring(3, 5)) ;
        System.out.println(db);
        String mn  = e.numTo(db);
        System.out.println(mn);
        String att = da.substring(0, 2)+mn+da.substring(8,10);
        System.out.println(att);
        
        
        String fileDir = "C:\\Users\\Geetha Rapolu\\Desktop\\Creditor Case Status "+ att+".xlsx";
        System.out.println(fileDir);
        
        File file = new File(fileDir);
        System.out.println(file.exists());
    }  
}