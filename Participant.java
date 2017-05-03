package accounts;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;


public class Participant {

	JFrame frame;
	private String userName = null;
	private Create_User_Account newUser;
	private JTextField nameTextField;
	private JTextField accountIDTextField;
	private JTextField amountTextField;
	private Coin_Flip coinFlip;
	private String randomN;
	private String hashVal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Participant window = new Participant(null);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Participant(String userName) {
		this.userName = userName;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 792, 416);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome "+userName);
		lblNewLabel.setBounds(307, 11, 135, 14);
		getFrame().getContentPane().add(lblNewLabel);
		
		JTextArea displayText = new JTextArea();
		displayText.setBounds(10, 27, 731, 177);
		frame.getContentPane().add(displayText);
	
		
		JButton viewAccountIDbtn = new JButton("View Account ID");
		viewAccountIDbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayText.setText("Your Account ID is : "+newUser.getAccountId());
			}
		});
		viewAccountIDbtn.setBounds(78, 284, 146, 28);
		getFrame().getContentPane().add(viewAccountIDbtn);
		
		JButton ViewAccountBalancebtn = new JButton("View Account Balance");
		ViewAccountBalancebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayText.setText(newUser.querryAccountBalance(newUser.getKeyPair()));
			}
		});
		ViewAccountBalancebtn.setBounds(51, 317, 173, 28);
		getFrame().getContentPane().add(ViewAccountBalancebtn);
		
		accountIDTextField = new JTextField();
		accountIDTextField.setBounds(133, 352, 278, 20);
		frame.getContentPane().add(accountIDTextField);
		accountIDTextField.setColumns(10);
		
		amountTextField = new JTextField();
		amountTextField.setColumns(10);
		amountTextField.setBounds(478, 346, 113, 20);
		frame.getContentPane().add(amountTextField);
		
		
		JButton TransferFundsbtn = new JButton("Transfer Funds");
		TransferFundsbtn.addActionListener(new ActionListener() {
			String status = "";
			public void actionPerformed(ActionEvent arg0) {
				try {
					status = newUser.transferFunds(newUser.getSecretSeed() ,accountIDTextField.getText(), amountTextField.getText());
					displayText.setText(status);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		TransferFundsbtn.setBounds(598, 335, 123, 28);
		getFrame().getContentPane().add(TransferFundsbtn);
		
		JLabel lblEnterYourName = new JLabel("Enter your name and click create account");
		lblEnterYourName.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterYourName.setBounds(20, 215, 256, 14);
		frame.getContentPane().add(lblEnterYourName);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(91, 231, 86, 20);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		JButton createAccountbtn = new JButton("Create Account");
		createAccountbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newUser = new Create_User_Account(nameTextField.getText());
				nameTextField.setText("");
				nameTextField.setVisible(false);
			}
		});
		createAccountbtn.setBounds(78, 251, 123, 28);
		frame.getContentPane().add(createAccountbtn);
		
		JLabel accountIDText = new JLabel("AccountID to transfer :");
		accountIDText.setHorizontalAlignment(SwingConstants.CENTER);
		accountIDText.setBounds(10, 355, 123, 14);
		frame.getContentPane().add(accountIDText);
		
		JLabel amountText = new JLabel("Amount :");
		amountText.setHorizontalAlignment(SwingConstants.CENTER);
		amountText.setBounds(418, 349, 71, 14);
		frame.getContentPane().add(amountText);
		
		JLabel lblCoinFlippingGame = new JLabel("Coin Flipping Game");
		lblCoinFlippingGame.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCoinFlippingGame.setHorizontalAlignment(SwingConstants.CENTER);
		lblCoinFlippingGame.setBounds(419, 215, 163, 28);
		frame.getContentPane().add(lblCoinFlippingGame);
		
		JButton btnGenerateRandomNumber = new JButton("Generate Random Number and Hash");
		btnGenerateRandomNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				coinFlip = new Coin_Flip();
				coinFlip.generateRandomNumber();
				randomN = coinFlip.getRandomNumber();
				hashVal = coinFlip.sha256RandomNumber(randomN);
				displayText.setText(randomN+"\n");
				displayText.append(hashVal);	
				newUser.updateRandHash(randomN, hashVal, newUser.getAccountId());
			}
		});
		btnGenerateRandomNumber.setBounds(419, 254, 242, 23);
		frame.getContentPane().add(btnGenerateRandomNumber);
		
		JButton btnSendHashTo = new JButton("Enter Bankers ID, 20 lumen, and place Bet");
		btnSendHashTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(newUser.updateBet()){
					try {
						newUser.transferFunds(newUser.getSecretSeed() ,accountIDTextField.getText(), amountTextField.getText());
						displayText.setText("Bet Placed!");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnSendHashTo.setBounds(443, 287, 292, 23);
		frame.getContentPane().add(btnSendHashTo);
		
		JButton bankerIDBtn = new JButton("View Banker Account ID");
		bankerIDBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bankerID = newUser.viewBankerID();
				displayText.setText(bankerID);
			}
		});
		bankerIDBtn.setBounds(211, 230, 173, 23);
		frame.getContentPane().add(bankerIDBtn);
		
		JButton btnViewWinner = new JButton("View Winner");
		btnViewWinner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String winner = newUser.getWinner();
				displayText.setText(winner);
			}
		});
		btnViewWinner.setBounds(271, 272, 123, 23);
		frame.getContentPane().add(btnViewWinner);
		
		JButton historyButton = new JButton("View History");
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String history = newUser.viewAccountHistory();
				displayText.setText(history);
			}
		});
		historyButton.setBounds(271, 307, 138, 23);
		frame.getContentPane().add(historyButton);
		
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String name){
		this.userName = name;
	}
	
	public boolean checkParticipant(){
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
			rs.last();
			if(rs.getRow() > 0){
				return true;
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
		
		return false;
	}
}
