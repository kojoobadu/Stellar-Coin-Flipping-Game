package accounts;

import java.security.MessageDigest;

public class Coin_Flip {
	
	private String randomNumber;
	
	public Coin_Flip(){
		
	}
	
	public void generateRandomNumber(){
		int random = (int) (Math.random() * 100 + 0);
		setRandomNumber(String.valueOf(random));
	}
	
	public void setRandomNumber(String number){
		this.randomNumber = number;
	}
	
	public String getRandomNumber(){
		return this.randomNumber;
	}
	
	public String sha256RandomNumber(String number){
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash =  digest.digest(number.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();	
			
			for(int i = 0; i < hash.length; i ++){
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1){
					hexString.append(hex);
				}
			}
			return hexString.toString();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
