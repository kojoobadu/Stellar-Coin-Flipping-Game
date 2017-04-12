package accounts;

import java.io.IOException;

public class mainClass {
	
	public static void main(String args[]) throws IOException{
		
		Create_User_Account source = new Create_User_Account("Kojo");
		Create_User_Account destination = new Create_User_Account("Nate");
		System.out.println(source.getAccountId());
		System.out.println(destination.getAccountId());
		
		source.transferFunds(source.getSecretSeed(), destination.getAccountId(), "500");
		source.transferFunds(source.getSecretSeed(), destination.getAccountId(), "2000");
		
		System.out.println(source.querryAccountBalance(source.getKeyPair()));
		System.out.println(destination.querryAccountBalance(destination.getKeyPair()));
		
		
	}

}
