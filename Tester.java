package accounts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Tester {
	public static void main(String args[]){
		
		Create_User_Account user = new Create_User_Account("TEST");
		System.out.println("The user account id is: "+ user.getAccountId());
	}
	
}
