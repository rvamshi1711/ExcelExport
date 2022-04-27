import java.sql.*;
import java.util.Random;



public class JDBCTry1 {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			
			Class.forName("com.mysql.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/creditors","root","1234");

			// for adding row
			Statement stmt = con.createStatement();
			ResultSet  rs = stmt.executeQuery("Select * from applist");
			/*
			while(rs.next())
			{
				System.out.println(rs.getInt("personID") + "\t" + rs.getString("firstname") + "\t" + rs.getString("lastname") + "\t"+ rs.getString("occupation") + "\t" + rs.getString("extra"));
			}
			*/
			Random random = new Random();
			int r = random.nextInt(10000);
			System.out.println(r);
			
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuilder sb = new StringBuilder();
			int length = 35;
			
			for(int i=0;i<788;i++)
			{

				int personID = random.nextInt(10000);
				System.out.println(personID);
				for(int j = 0; j < length; j++) 
				{
				      int index = random.nextInt(alphabet.length());
				      char randomChar = alphabet.charAt(index);
				      sb.append(randomChar); 
				      
				}
				String fulltext = sb.toString();
				System.out.println(fulltext);
				String firstName = fulltext.substring(0, 8);
				String lastName = fulltext.substring(8, 15);
				String occupation = fulltext.substring(15, 32);
				String extra = fulltext.substring(32, 35);
				sb.setLength(0);
				System.out.println(firstName+"|"+lastName+"|"+occupation+"|"+extra);
				
				String inscmd = "Insert into applist(personID,firstName,LastName,occupation,extra) values( "+personID+",'"+firstName+"','"+lastName+"','"+occupation+"','"+extra+"');";
				int check = stmt.executeUpdate(inscmd);
				if(check>=1)
					System.out.println("inserted");
				else
					System.out.println("Not inserted");
				
			}
			

		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

}
