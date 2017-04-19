package accounts;

import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Network;
import org.stellar.sdk.PaymentOperation;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Create_User_Account {

		private String userName;
		private KeyPair keyPair;
		private static final String serverAddress = "https://horizon-testnet.stellar.org";
		private Server server;
		private String pagingToken = null;
		private Coin_Flip coinflip = new Coin_Flip();
		

	public Create_User_Account( String userName){
		this.userName = userName;
		this.keyPair = KeyPair.random();
		setUpServer();
		connectToServer();
		updateDatabase(getUserName(), getAccountId(), getSecretSeed());
	}
	
	public String viewBankerID(){
		String participantInfo = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			ResultSet rs;
			rs = statement.executeQuery("select * from participants f where f.UserName = 'BANKER'");
			String id = "";
			rs.last();
			id = rs.getString("AccountID");
			participantInfo = "Banker's AccountID is : "+ id+"\n";
			statement.close();
			rs.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
		return participantInfo;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String decideWhoWon(String bankerSecretSeed) throws IOException{
		HashMap<String, String> players = new HashMap<String, String>();
		String winner = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			ResultSet rs;
			ResultSet alice = null;
			ResultSet bob = null;
			rs = statement.executeQuery("select * from participants f where Bet = TRUE");
			String randomNumber = "";
			String hashValue = "";
			String actualHash = "";
			int totalRand = 0;
			while (rs.next()) {
				randomNumber = rs.getString("RandomNumber");
				hashValue = rs.getString("HashValue");
				actualHash = coinflip.sha256RandomNumber(randomNumber);
				if( hashValue.equals(actualHash)){
					players.put(rs.getString("UserName"), randomNumber);
					totalRand += Integer.parseInt(rs.getString("RandomNumber"));
				}
			}
			String id = "";
			if((totalRand % 2) == 0){
				winner = "Alice Won!!!!!!!!!!!!!!!";
				System.out.println(winner);
				alice = statement.executeQuery("Select * from participants p where p.UserName = 'Alice'");
				alice.last();
				id = alice.getString("AccountID");
				transferFunds(bankerSecretSeed, id, "36");
			}
			else if((totalRand % 2) != 0){
				winner = "Bob Won!!!!!!!!!!!!!!!!!!!!!!!!!!";
				System.out.println(winner);
				bob = statement.executeQuery("Select * from participants p where p.UserName = 'Bob'");
				bob.last();
				id = bob.getString("AccountID");
				transferFunds(bankerSecretSeed, id, "36");
			}
			statement.close();
			rs.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
		return winner;
	}
	
	public boolean updateBet(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			String sql = "update participants set Bet = TRUE where AccountID = '"+keyPair.getAccountId()+"'";
			statement.executeUpdate(sql);
			statement.close();
			conn1.close();
			return true;
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    	return false;
	    }

	}
	
	public String verifyHash(String participant1, String hash){
		String verification = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			ResultSet rs;
			rs = statement.executeQuery("select from participants f");
			int i = 0;
			String hashval = "";
			String randomnum = "";
			String name = "";
			while (rs.next()) {
				hashval = rs.getString("HashValue");		
				randomnum = rs.getString("RandomNumber");
				if(coinflip.sha256RandomNumber(randomnum).equals(hashval)){
					verification = "Verified!";
				}
				System.out.println("Participant "+ ++i + "\nUserName is : " + name+"\nRandom Number is : "+ randomnum+"Hash Value is : "+hashval+"\n");
			}
			statement.close();
			rs.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
		return verification;
	}
	
	public void updateRandHash(String randomNumber, String hashValue, String accountID){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			String sql = "update participants set RandomNumber = '"+randomNumber+"', HashValue = '"+hashValue+"' where AccountID = '"+accountID+"'";
			statement.executeUpdate(sql);
			statement.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
	}
	
	public void updateDatabase(String userName, String accountID, String secretSeed){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			Statement statement2 = conn1.createStatement();
			ResultSet rs;
			String sql = "Insert into participants (UserName, AccountID, SecretSeed) values('"+userName+"' , '"+accountID+"' , '"+secretSeed+"')";
			statement2.executeUpdate(sql);
			rs = statement.executeQuery("select * from participants f");
			int i = 0;
			String name = "";
			String id = "";
			while (rs.next()) {
				name = rs.getString("UserName");		
				id = rs.getString("AccountID");
				System.out.println("Participant "+ ++i + "\nUserName is : " + name+"\nAccountID is : "+ id+"\n");
			}
			statement.close();
			rs.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }

	}
	
	public String viewParticipants(){
		String participantsInfo = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			ResultSet rs;
			rs = statement.executeQuery("select * from participants f");
			int i = 0;
			String name = "";
			String id = "";
			String secret = "";
			while (rs.next()) {
				name = rs.getString("UserName");		
				id = rs.getString("AccountID");
				secret = rs.getString("SecretSeed");
				String participantInfo = "Participant "+ ++i + "\nUserName is : " + name+"\nAccountID is : "+ id+"\nSecretSeed is :"+ secret+"\n";
				System.out.println(participantInfo);
				participantsInfo = participantsInfo + participantInfo;
			}
			statement.close();
			rs.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
		return participantsInfo;
	}
	
	public void deleteAccounts(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			Connection conn1;
			String dbUrl = "jdbc:mysql://localhost:3306/stellar_data";
			String user = "root";
			String password = "root";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");
			Statement statement = conn1.createStatement();
			String sql = "Delete from participants";
			statement.executeUpdate(sql);
			statement.close();
			conn1.close();
		}
	    catch (SQLException e) {
	    	System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    }
	}

	public void setUpServer(){
		server = new Server(serverAddress);
	}

	public void connectToServer(){
		String friendBotUrl = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", getAccountId());
		System.out.println(getResponseFromServer(friendBotUrl));
	}

	public String getResponseFromServer( String friendBotUrl ){
		InputStream response = null;
		try {
			response = new URL(friendBotUrl).openStream();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("resource")
		String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
		return "SUCCESS CREATED NEW ACCOUNT\n" + body;
	}

	public String querryAccountBalance(KeyPair keyPair){
		String result = "";
		AccountResponse account = null;
		try {
			System.out.print("Test keyPair: "+ getAccountId());
			account = server.accounts().account(keyPair);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Balances for "+ userName +"("+getAccountId()+").");
		for( AccountResponse.Balance balance : account.getBalances()){
			result = String.format("Type: %s, Code: %s, Balance: %s\n\n", balance.getAssetType(), balance.getAssetCode(), balance.getBalance());
		}
		return result;
	}

	public String getAccountId(){
		return new String(this.keyPair.getAccountId());
	}

	public String getSecretSeed(){
		return new String(this.keyPair.getSecretSeed());
	}

	public KeyPair getKeyPair(){
		return keyPair;
	}
	
	public void initializeNetwork(){
		Network.useTestNetwork();
	}
	
	public String transferFunds( String sourceSecretSeed, String targetAccountId, String ammount) throws IOException{
		String status = null;
		initializeNetwork();
		KeyPair destination = KeyPair.fromAccountId(targetAccountId);
		KeyPair source = KeyPair.fromSecretSeed(sourceSecretSeed);
		
		server.accounts().account(destination);
		server.accounts().account(source);
		
		AccountResponse sourceAccount = server.accounts().account(source);
		
		Transaction transaction = new Transaction.Builder(sourceAccount)
		        .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), ammount ).build())
		        // A memo allows you to add your own metadata to a transaction. It's
		        // optional and does not affect how Stellar treats the transaction.
		        .addMemo(Memo.text("send "+ammount+"/ Lumens"))
		        .build();
		// Sign the transaction to prove you are actually the person sending it.
		transaction.sign(source);
		
		try {
			  SubmitTransactionResponse submitTransactionResponse = server.submitTransaction(transaction);
			  System.out.println("Success!");
			  status = "Successful!!!!!!";
			  System.out.println(submitTransactionResponse);
			}
			catch (Exception e) {
				status = "Failure!!!!!!";
			  System.out.println("Something went wrong!");
			  System.out.println(e.getMessage());
			}
		return status;
	}
	
	/*
	public void updateAccountStatus(){
		PaymentsRequestBuilder paymentsRequest = server.payments().forAccount(keyPair);
		
		String lastToken = loadLastPagingToken();
		if (lastToken != null) {
			paymentsRequest.cursor(lastToken);
		}
		
		paymentsRequest.stream(new EventListener<OperationResponse>() {
			  @Override
			  public void onEvent(OperationResponse payment) {
			    // Record the paging token so we can start from here next time.
			    savePagingToken(payment.getPagingToken());
			    System.out.println("Here");
			    // The payments stream includes both sent and received payments. We only
			    // want to process received payments here.
			    if (payment instanceof PaymentOperationResponse) {
			      if (((PaymentOperationResponse) payment).getTo().equals(keyPair)) {
			        return;
			      }

			      String amount = ((PaymentOperationResponse) payment).getAmount();

			      Asset asset = ((PaymentOperationResponse) payment).getAsset();
			      String assetName;
			      if (asset.equals(new AssetTypeNative())) {
			        assetName = "lumens";
			      } 
			      else {
			        StringBuilder assetNameBuilder = new StringBuilder();
			        assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getCode());
			        assetNameBuilder.append(":");
			        assetNameBuilder.append(((AssetTypeCreditAlphaNum) asset).getIssuer().getAccountId());
			        assetName = assetNameBuilder.toString();
			      }

			      StringBuilder output = new StringBuilder();
			      output.append(amount);
			      output.append(" ");
			      output.append(assetName);
			      output.append(" from ");
			      output.append(((PaymentOperationResponse) payment).getFrom().getAccountId());
			      System.out.println(output.toString());
			    }
			  }
	}
	*/
	
	public void savePagingToken(String value){
		pagingToken = value;
	}
	
	public String loadLastPagingToken(){
		return pagingToken;
	}
	
	
}
