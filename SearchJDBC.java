import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SearchJDBC {
	
	//public String createFile
	
	public String numToMonth(int mn)
	{
		String month="";
		switch (mn) {
        case 1:
          month = "JAN";
          break;
        case 2:
          month = "FEB";
          break;
        case 3:
          month = "MAR";
          break;
        case 4:
          month = "APR";
          break;
        case 5:
          month = "MAY";
          break;
        case 6:
          month = "JUN";
          break;
        case 7:
          month = "JUL";
          break;
        case 8:
            month = "AUG";
            break;
        case 9:
            month = "SEP";
            break;
        case 10:
            month = "OCT";
            break;
        case 11:
            month = "NOV";
            break;
        case 12:
            month = "DEC";
            break;
      }
		return month;
	}
	
	public String fileNameConverter(String fileName)
	{
		//String f = "TO_ML_NEW_19_01_2022";
        String month ="";
        String finalWorkSheetName="";
        
        if(fileName.substring(0, 10).equals("TO_ML_NEW_"))
        {
        	int mn = Integer.parseInt(fileName.substring(13, 15)) ;
        	month =  numToMonth(mn);
        	finalWorkSheetName = "PNB "+fileName.substring(10, 12)+month+fileName.substring(18, 20);
        }
        else if(fileName.substring(8,15).equals("metlife"))
        {
        	int mn = Integer.parseInt(fileName.substring(2, 4)) ;
        	month =  numToMonth(mn);
        	finalWorkSheetName = "JKB "+fileName.substring(0, 2)+month+fileName.substring(6, 8);
        }
		return finalWorkSheetName;
	}
	
	/**
	 * @param dbURL
	 * @param userID
	 * @param password
	 * @param fileDirectory
	 * @param finalFileName
	 * @param excelFile
	 * @param att
	 * @return
	 */
	public ArrayList<String> connectJDBC(String dbURL, String userID, String password, String[][] fileDirectory,String finalFileName, String excelFile,String att) {
		try
		{
			ArrayList<String> finalOutput  = new ArrayList<String>();
			Class.forName("com.mysql.jdbc.Driver");
		    Connection connection = DriverManager.getConnection(dbURL,userID,password);
		    if(connection!=null) {
		    	//System.out.println("Connection Established");
		    	finalOutput.add("Connection Established");
		    	Statement statement = connection.createStatement();
		    	
		    	int remainingAppls = 0;
		    	
		    	ArrayList<String> dup = new ArrayList<String>();
		    	ArrayList<Integer> al2 = new ArrayList<Integer>();
		    	ArrayList<String> al3 = new ArrayList<String>();
		    	ArrayList<String> al4 = new ArrayList<String>();
		    	ArrayList<String> al5 = new ArrayList<String>();
		    	
		    	
		    	HashMap<String,String> occValue=new HashMap<String,String>();
		    	HashMap<String,Integer> freq = new HashMap<String,Integer>();
		    	
		    	finalOutput.add("Total No.of Applications in Flat File: "+fileDirectory.length);
		    	
		    	
		    	StringBuilder sb = new StringBuilder();
		    	//String appNo="";
		    	for(int i=0;i<fileDirectory.length-1;i++)
		    	{
		    		sb.append("'"+fileDirectory[i][1]+"',");
		    		/*
		    		sb.append("'");
		    		appNo = fileDirectory[i][1]; 
		    		sb.append(appNo);
		    		sb.append("',");
		    		*/
		    	}
		    	sb.append("'"+fileDirectory[fileDirectory.length-1][1]+"'");
		    	/*
		    	sb.append("'");
		    	appNo = fileDirectory[fileDirectory.length-1][1];
		    	sb.append(appNo);
		    	sb.append("'");
		    	*/
		    	String finalone = sb.toString();
		    	
		    	String query = "select count(*) from applist where personID in ("+finalone+");";
		    	//System.out.println(query);
		    	ResultSet rs = statement.executeQuery(query);
		    	
		    	
		    	while(rs.next()) {
		    		//System.out.println("Applications Processed: "+rs.getInt(1));
		    		finalOutput.add("Applications Processed: "+rs.getInt(1));
		    		remainingAppls = fileDirectory.length - rs.getInt(1);
		    		
		    		if(rs.getInt(1)==fileDirectory.length)
			    	{
		    			finalOutput.add("All Applications are processsed");
			    		//System.out.println("All Applications are processsed");
			    	}
		    	}
		    	rs.close();
		    	
		    	
		    	//XSSFWorkbook workbook1;
				
		    	//System.out.println(remainingAppls);
		    	if(remainingAppls>0)
		    	{
		    		//System.out.println("Applications not Processed: "+remainingAppls);
		    		finalOutput.add("Applications not Processed: "+remainingAppls);
		    		String query2 = "select * from applist where personID in("+finalone+");";
		    		//System.out.println(query2);
	    			ResultSet rs2 = statement.executeQuery(query2);
	    			
	    			while(rs2.next())
	    			{
	    				al3.add(String.valueOf(rs2.getInt("personID")));
	    				//al2.add(rs2.getInt("personID"));
	    				//System.out.println(rs2.getInt("personID"));
	    			}
	    			rs2.close();
	    			
	    			
	    			
	    			
	    			
	    			/*
	    			XSSFWorkbook workbook = new XSSFWorkbook();
	    			XSSFSheet sheet = workbook.createSheet(finalFileName);
	    			
	    			Row row = sheet.createRow(0);
	    			row.createCell(0).setCellValue("Application Number");
	    			for(int i=0;i<al3.size();i++)
	    			{
	    				row = sheet.createRow(i+1);
	    				row.createCell(0).setCellValue(al3.get(i));
	    			}
	    			if(!file.exists())
	    			{
	    				try (FileOutputStream outputStream= new FileOutputStream(excelFile)) {
		    	            workbook.write(outputStream);
		    	        }
	    				
	    			}
	    			else
	    			{
	    				try(InputStream is = new FileInputStream(file))
	    				{
	    					workbook1 = new XSSFWorkbook(is);
	    				}
	    			}
	    			
	    			*/
	    			
	    			//System.out.println("al3: "+al3.size());
	    			
	    			for(int i=0;i<fileDirectory.length;i++)
	    			{
	    				if(!al3.contains(fileDirectory[i][1]))
	    				{
	    					//System.out.println(fileDirectory[i][1]);
	    					al4.add(fileDirectory[i][1]);
	    					
	    				}
	    					
	    					
	    			}
	    			if(!al4.isEmpty())
	    			{
	    				//System.out.println("al4(occupation_others): "+al4);
	    				finalOutput.add("Occupation others: "+al4.size());
	    				for(int i=0;i<fileDirectory.length;i++)
		    			{
		    				for(int j=0;j<al4.size();j++)
		    				{
		    					if(fileDirectory[i][1].equals(al4.get(j)))
		    					{
		    						String bb = fileDirectory[i][4];
		    						if(bb.length()>20)
		    						{
		    							occValue.put(fileDirectory[i][1], bb);
		    							continue;
		    						}
		    					}
		    				}
		    			}
	    				
	    				for(int i=0;i<al4.size();i++)
	    				{
	    					for(int j=0;j<fileDirectory.length;j++)
	    					{
	    						if(al4.get(i).equals(fileDirectory[j][1]))
	    						{
	    							finalOutput.add("|"+fileDirectory[j][1]+"|"+fileDirectory[j][2]+"|"+fileDirectory[j][3]+"|"+fileDirectory[j][4]+"|"+fileDirectory[j][5] );
	    						}
	    					}
	    				}
	    				/*
		    			for (HashMap.Entry<String,String> entry : occValue.entrySet())
		    	            System.out.println("App No = " + entry.getKey() +
		    	                             ", Occupation other Value = " + entry.getValue());
		    			System.out.println("occ size: "+occValue.size());
		    			*/
		    			remainingAppls -=occValue.size();
		    			//System.out.println("Remaining Applications: "+remainingAppls);
		    			finalOutput.add("Remaining Applications: "+remainingAppls);
	    			}
	    			
	    			if(remainingAppls>0)
	    			{
	    				for(int i=0;i<fileDirectory.length-1;i++)
	    				{
	    					for(int j=i+1;j<fileDirectory.length;j++)
	    					{
	    						
	    						if(fileDirectory[i][1].equals(fileDirectory[j][1]))
	    						{
	    							
	    							if(dup.contains(fileDirectory[i][1]))
	    							{
	    								break;
	    							}
	    							else
	    							{
	    								dup.add(fileDirectory[i][1]);
	    							}	    							
	    						}
	    					}
	    				}
	    				//System.out.println("Duplicate Application Numbers: "+dup);
		    			
		    			for(int i=0;i<dup.size();i++)
		    			{
		    				
		    				for(int j=0;j<fileDirectory.length;j++)
		    				{
		    					if(dup.get(i).equals(fileDirectory[j][1]))
		    					{
		    						
		    						if(freq.containsKey(fileDirectory[j][1]))
		    						{
		    							freq.put(fileDirectory[j][1],freq.get(fileDirectory[j][1])+1);
	    								
		    						}
		    						else
		    						{
		    							freq.put(fileDirectory[j][1],1);
		    						}
		    					}
		    				}	    				
		    			}
		    			int count = 0;
		    			//System.out.println(freq);
		    			for (HashMap.Entry<String,Integer> entry : freq.entrySet())
		    			{
		    				count += entry.getValue();
		    			}
		    			count -= freq.size();
		    			remainingAppls -= count;
		    			//System.out.println("Remaining Applications: "+remainingAppls);
		    			
		    			StringBuilder sb2 = new StringBuilder();
		    			StringBuilder duplicte = new StringBuilder();
				    	String appNo2="";
				    	//System.out.println("dup loop start");
				    	for(int i=0;i<dup.size()-1;i++)
				    	{
				    		sb2.append("'");
				    		appNo2 = dup.get(i); 
				    		sb2.append(appNo2);
				    		sb2.append("',");
				    	}
				    	//System.out.println("dup loop start");
		    			sb2.append("'");
		    			sb2.append(dup.get(dup.size()-1));
		    			sb2.append("'");
		    			String finalone2 = sb2.toString();
		    			
		    			
		    			String query3 = "select personID, FirstName from applist where personID in ("+finalone2+");";
		    			String dupArr[][] = new String[freq.size()][2];
		    			
		    			String dupArr2[][] = new String [count][5];
		    			
		    			//System.out.println(query3);
		    			ResultSet rs3 = statement.executeQuery(query3); 
		    			
		    			
		    				
		    				for(int i=0;i<freq.size();i++)
		    				{
		    					while(rs3.next())
		    					{
		    						dupArr[i][0] = String.valueOf(rs3.getInt("PersonID"));
		    						dupArr[i][1] = rs3.getString(2);
		    						i++;
		    					}
		    				}
		    				
		    				//System.out.println(rs3.getInt("PersonID"));
		    				
		    			rs3.close();
		    			/*
		    			for(int i=0;i<dupArr.length;i++)
		    			{
		    				for(int j=0;j<2;j++)
		    				{
		    					System.out.println(i+" "+j+" "+dupArr[i][j]);
		    				}
		    			}
		    			*/
		    			for(int i=0;i<dupArr.length;i++)
		    			{
		    				
		    				for(int j=0;j<fileDirectory.length;j++)
		    				{
		    					
		    					if(dupArr[i][0].equals(fileDirectory[j][1]) && !(dupArr[i][1].equals(fileDirectory[j][2])))
		    					{
		    						//System.out.println(fileDirectory[j][1]+" "+fileDirectory[j][2]+" "+fileDirectory[j][3]+" "+fileDirectory[j][4]+" "+fileDirectory[j][5]);
		    						//System.out.println("i: "+i);
		    						duplicte.append("|"+fileDirectory[j][1]+"|"+fileDirectory[j][2]+"|"+fileDirectory[j][3]+"|"+fileDirectory[j][4]+"|"+fileDirectory[j][5] );
		    						al5.add(duplicte.toString());
		    						finalOutput.add(duplicte.toString());
		    						duplicte.setLength(0);
		    					}
		    				}
		    			}
		    			finalOutput.add("Remaining applications: "+remainingAppls);
		    			//System.out.println(al5);
		    			/*
		    			for(int i=0;i<al5.size();i++)
		    			{
		    				System.out.println(al5.get(i));
		    			}
		    			*/
		    			System.out.println("Final Output\n------------------");
		    			/*
		    			for(int i=0;i<finalOutput.size();i++)
		    			{
		    				System.out.println(finalOutput.get(i));
		    			}
		    			*/
	    			}
	    			
		    	}
		    	connection.close();
		    	finalOutput.add("Connection closed");
		    	//System.out.println("Connection closed");
		    	
		    	File file = new File(excelFile);
		    	
		    	finalOutput.add("printing excel");
		    	
		    	//XSSFWorkbook workbook1 = new XSSFWorkbook();
		    	
		    	
		    	if(file.exists())
    			{
		    		
    				try(InputStream is = new FileInputStream(file))
    				{
    					XSSFWorkbook workbook = new XSSFWorkbook(is);
    					
    					if (workbook.getNumberOfSheets() != 0) {
    						
    				        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
    				        	
    				           if (workbook.getSheetName(i).equals(finalFileName)) 
    				           {
    				               workbook.removeSheetAt(i);
    				               System.out.println("Sheet removed at: "+i);
    				           }
    				        }
    				    }
    					
    					Font headerFont = workbook.createFont();
    			    	headerFont.setBold(true);
    			    	
    			    	
    			    	
    			    	CellStyle headerStyle = workbook.createCellStyle();
    			    	headerStyle.setFont(headerFont);
    			    	headerStyle.setAlignment(HorizontalAlignment.CENTER);
    			    	
    			    	CellStyle headerStyle2 = workbook.createCellStyle();
    			    	headerStyle2.setAlignment(HorizontalAlignment.CENTER);
    					
    					XSSFSheet sheet = workbook.createSheet(finalFileName);
    					Row row = sheet.createRow(0);
    					
    	    			row.createCell(0).setCellValue("Application Number");
    	    			row.getCell(0).setCellStyle(headerStyle);
    	    			
    	    			for(int i=0;i<al3.size();i++)
    	    			{
    	    				row = sheet.createRow(i+1);
    	    				row.createCell(0).setCellValue(Integer.valueOf(al3.get(i)));
    	    				row.getCell(0).setCellStyle(headerStyle2);
    	    			}
    	    			
    	    			
    	    			sheet.setColumnWidth(0, 5100);
    	    			
    	    			
    	    			FileOutputStream outputStream = new FileOutputStream("C:\\\\Users\\\\Geetha Rapolu\\\\Desktop\\\\Creditor Case Status "+att+".xlsx");
    	                
    	    			workbook.write(outputStream);
    	                workbook.close();
    	                outputStream.close();
    	                
    				}
    			}
    			else
    			{
    				XSSFWorkbook workbook = new XSSFWorkbook();
    				XSSFSheet sheet = workbook.createSheet(finalFileName);
    				
    				Font headerFont = workbook.createFont();
			    	headerFont.setBold(true);
    				
    				CellStyle headerStyle = workbook.createCellStyle();
			    	headerStyle.setFont(headerFont);
			    	headerStyle.setAlignment(HorizontalAlignment.CENTER);
			    	
			    	CellStyle headerStyle2 = workbook.createCellStyle();
			    	headerStyle2.setAlignment(HorizontalAlignment.CENTER);
    				
					Row row = sheet.createRow(0);
	    			row.createCell(0).setCellValue("Application Number");
	    			row.getCell(0).setCellStyle(headerStyle);
	    			
	    			for(int i=0;i<al3.size();i++)
	    			{
	    				row = sheet.createRow(i+1);
	    				row.createCell(0).setCellValue(Integer.valueOf(al3.get(i)));
	    				row.getCell(0).setCellStyle(headerStyle2);
	    			}
	    			
	    			sheet.setColumnWidth(0, 5100);
	    			
	    			FileOutputStream outputStream = new FileOutputStream("C:\\\\Users\\\\Geetha Rapolu\\\\Desktop\\\\Creditor Case Status "+att+".xlsx");
	                workbook.write(outputStream);
	                
	                workbook.close();
	                outputStream.close();
    			}
		    	finalOutput.add("Done excel");
		    	
		    }
		    
		    return finalOutput; 
		   
		}  
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	public String[][] textIntoArray(String fileDirectory) {
		//String[][] applFinal = new String[][];
		try {
			List<String> applicationList = Files.readAllLines(new File(fileDirectory).toPath(), Charset.defaultCharset());
			int listSize = applicationList.size();
			System.out.println("Total no. of Applications: "+listSize);
			int fixedSize = 6;
			String[][] appl = new String[listSize][fixedSize];
			
			for(int i=0;i<listSize;i++) {
				String b[] = applicationList.get(i).split("\\|");
				//System.out.println(Arrays.toString(b));
				
				for(int j=1;j<fixedSize;j++)
				{
					//System.out.println(b[j]);
					appl[i][j] = b[j];
				}
				b = new String[b.length];
			}
			
			/*System.out.println("Final output:");
			for(int i=0;i<listSize;i++)
			{
				for(int j=1;j<fixedSize;j++)
				{
					
					//System.out.print(appl[i][j]+" ");
					//applFinal[i][j] = appl[i][j];
				}
				//System.out.println();
			}
			*/
			return appl;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dbURL = "jdbc:mysql://localhost:3306/creditors";
		String userID = "root";
		String password = "1234";
		String fileName = "TO_ML_NEW_19_01_2022";
		
		SearchJDBC searchJDBC = new SearchJDBC();
		String finalFileName = searchJDBC.fileNameConverter(fileName);
		System.out.println(finalFileName);
		
		String fileDirectory = "C:\\Users\\Geetha Rapolu\\Desktop\\"+fileName+".txt";
		
		String[][] textFile = searchJDBC.textIntoArray(fileDirectory);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
        Date date = new Date();
        String da = formatter.format(date);
        //System.out.println(da);
        int db = Integer.parseInt(da.substring(3, 5)) ;
        //System.out.println(db);
        String mn  = searchJDBC.numToMonth(db);
        //System.out.println(mn);
        String att = da.substring(0, 2)+mn+da.substring(8,10);
        //System.out.println(att);
        
        
        String excelFile = "C:\\Users\\Geetha Rapolu\\Desktop\\Creditor Case Status "+ att+".xlsx";
        System.out.println(excelFile);
        
        
		
		ArrayList<String> required = new ArrayList<String>();
		required = searchJDBC.connectJDBC(dbURL, userID, password,textFile,finalFileName,excelFile,att);
		for(int i=0;i<required.size();i++)
		{
			System.out.println(required.get(i));
		}
		
		
	}

}
