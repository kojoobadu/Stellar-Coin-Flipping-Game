package accounts;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Network;
import org.stellar.sdk.PaymentOperation;
import java.net.*;
import java.io.*;
import java.util.*;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;

public class tutorials {

	static 	String lastPagingToken = null;

	public static void main(String args[]) throws MalformedURLException, IOException{


		// create a completely new and unique pair of keys.
		// see more about KeyPair objects: https://stellar.github.io/java-stellar-sdk/org/stellar/sdk/KeyPair.html
		KeyPair P1 = KeyPair.random();
		KeyPair P2 = KeyPair.random();
		String P1ss = new String(P1.getSecretSeed());
		String P2ss = new String(P2.getSecretSeed());
		String P1pk = new String(P1.getAccountId());
		String P2pk = new String(P2.getAccountId());
//		System.out.println("Person 1 Secret Seed: "+ P1ss);
//		System.out.println("Person 2 Public Key: "+ P2ss);
		System.out.println("Person 1 Secret Seed: "+ P1pk);
		System.out.println("Person 2 Public Key: "+ P2pk);


		// The SDK does not have tools for creating test accounts, so you'll have to
		// make your own HTTP request.
		String friendbotUrl = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", P1pk );
		InputStream response = new URL(friendbotUrl).openStream();
		String body = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
//		System.out.println("SUCCESS CREATED FIRST ACCOUNT\n" + body);

		String bot2 = String.format("https://horizon-testnet.stellar.org/friendbot?addr=%s", P2pk);
		InputStream response2 = new URL(bot2).openStream();
		String body2 = new Scanner(response2, "UTF-8").useDelimiter("\\A").next();
//		System.out.println("SUCCESSFULLY CREATED SECOND ACCOUNT\n" + body2);



		//Getting Account Details from the server
		Server server = new Server("https://horizon-testnet.stellar.org");


		//Info for Person 1
		AccountResponse account1 = server.accounts().account(P1);
		System.out.println("Balances for account1 " + P1.getAccountId());
		for (AccountResponse.Balance balance : account1.getBalances()) {
		  System.out.println(String.format(
		    "Type: %s, Code: %s, Balance: %s\n\n",
		    balance.getAssetType(),
		    balance.getAssetCode(),
		    balance.getBalance()));
		}

		//Info for Person 2
		AccountResponse account2 = server.accounts().account(P2);
		System.out.println("Balances for account2 " + P2.getAccountId());
		for (AccountResponse.Balance balance : account2.getBalances()) {
		  System.out.println(String.format(
		    "Type: %s, Code: %s, Balance: %s",
		    balance.getAssetType(),
		    balance.getAssetCode(),
		    balance.getBalance()));
		}

		
		Asset astroDollar = Asset.createNonNativeAsset("AstroDollar", P1);

		
		/*
		//Conducting transactions
		Network.useTestNetwork();
		KeyPair source = KeyPair.fromSecretSeed(P1ss);
		KeyPair destination = KeyPair.fromPublicKey(P2.getPublicKey());

		// First, check to make sure that the destination account exists.
		// You could skip this, but if the account does not exist, you will be charged
		// the transaction fee when the transaction fails.
		// It will throw HttpResponseException if account does not exist or there was another error.
		server.accounts().account(destination);

		// If there was no error, load up-to-date information on your account.
		AccountResponse sourceAccount = server.accounts().account(source);

		// Start building the transaction.
		Transaction transaction = new Transaction.Builder(sourceAccount)
		        .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), "10").build())
		        // A memo allows you to add your own metadata to a transaction. It's
		        // optional and does not affect how Stellar treats the transaction.
		        .addMemo(Memo.text("send 10 Lumens"))
		        .build();
		// Sign the transaction to prove you are actually the person sending it.
		transaction.sign(source);

		// And finally, send it off to Stellar!
		try {
		  SubmitTransactionResponse submitTransactionResponse = server.submitTransaction(transaction);
		  System.out.println("Success!");
		  System.out.println(submitTransactionResponse);
		}
		catch (Exception e) {
		  System.out.println("Something went wrong!");
		  System.out.println(e.getMessage());
		}
		*/
		
		
		
		

		/*
		// Create an API call to query payments involving the account.
		KeyPair account = KeyPair.fromPublicKey(P2.getPublicKey());
		PaymentsRequestBuilder paymentsRequest = server.payments().forAccount(account);

		// If some payments have already been handled, start the results from the
		// last seen payment. (See below in `handlePayment` where it gets saved.)
		String lastToken = loadLastPagingToken();
		if (lastToken != null) {
		  paymentsRequest.cursor(lastToken);
		}

		// `stream` will send each recorded payment, one by one, then keep the
		// connection open and continue to send you new payments as they occur.
		paymentsRequest.stream(new EventListener<OperationResponse>() {
		  @Override
		  public void onEvent(OperationResponse payment) {
		    // Record the paging token so we can start from here next time.
		    savePagingToken(payment.getPagingToken());
		    lastPagingToken = payment.getPagingToken();
		    System.out.println("Here");
		    // The payments stream includes both sent and received payments. We only
		    // want to process received payments here.
		    if (payment instanceof PaymentOperationResponse) {
		      if (((PaymentOperationResponse) payment).getTo().equals(account)) {
		        return;
		      }

		      String amount = ((PaymentOperationResponse) payment).getAmount();

		      Asset asset = ((PaymentOperationResponse) payment).getAsset();
		      String assetName;
		      if (asset.equals(new AssetTypeNative())) {
		        assetName = "lumens";
		      } else {
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

		private void savePagingToken(String pagingToken) {

		}
		});

		*/


	}

	public static String loadLastPagingToken(){
		return lastPagingToken;
	}

}
