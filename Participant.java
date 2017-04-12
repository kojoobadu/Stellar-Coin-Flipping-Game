package accounts;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JComboBox;

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
		getFrame().setBounds(100, 100, 792, 448);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome "+userName);
		lblNewLabel.setBounds(307, 11, 135, 14);
		getFrame().getContentPane().add(lblNewLabel);
		
		JTextArea displayText = new JTextArea();
		displayText.setBounds(10, 27, 731, 206);
		frame.getContentPane().add(displayText);
	
		
		JButton viewAccountIDbtn = new JButton("View Account ID");
		viewAccountIDbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayText.setText("Your Account ID is : "+newUser.getAccountId());
			}
		});
		viewAccountIDbtn.setBounds(68, 313, 146, 28);
		getFrame().getContentPane().add(viewAccountIDbtn);
		
		JButton ViewAccountBalancebtn = new JButton("View Account Balance");
		ViewAccountBalancebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayText.setText(newUser.querryAccountBalance(newUser.getKeyPair()));
			}
		});
		ViewAccountBalancebtn.setBounds(41, 346, 173, 28);
		getFrame().getContentPane().add(ViewAccountBalancebtn);
		
		accountIDTextField = new JTextField();
		accountIDTextField.setBounds(123, 381, 347, 20);
		frame.getContentPane().add(accountIDTextField);
		accountIDTextField.setColumns(10);
		
		amountTextField = new JTextField();
		amountTextField.setColumns(10);
		amountTextField.setBounds(523, 381, 113, 20);
		frame.getContentPane().add(amountTextField);
		
		
		JButton TransferFundsbtn = new JButton("Transfer Funds");
		TransferFundsbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					newUser.transferFunds(newUser.getSecretSeed() ,accountIDTextField.getText(), amountTextField.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		TransferFundsbtn.setBounds(643, 370, 123, 28);
		getFrame().getContentPane().add(TransferFundsbtn);
		
		JLabel lblEnterYourName = new JLabel("Enter your name and click create account");
		lblEnterYourName.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterYourName.setBounds(10, 244, 256, 14);
		frame.getContentPane().add(lblEnterYourName);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(81, 260, 86, 20);
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
		createAccountbtn.setBounds(68, 280, 123, 28);
		frame.getContentPane().add(createAccountbtn);
		
		JLabel accountIDText = new JLabel("AccountID to transfer :");
		accountIDText.setHorizontalAlignment(SwingConstants.CENTER);
		accountIDText.setBounds(0, 384, 123, 14);
		frame.getContentPane().add(accountIDText);
		
		JLabel amountText = new JLabel("Amount :");
		amountText.setHorizontalAlignment(SwingConstants.CENTER);
		amountText.setBounds(463, 384, 71, 14);
		frame.getContentPane().add(amountText);
		
		JLabel lblCoinFlippingGame = new JLabel("Coin Flipping Game");
		lblCoinFlippingGame.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCoinFlippingGame.setHorizontalAlignment(SwingConstants.CENTER);
		lblCoinFlippingGame.setBounds(409, 244, 163, 28);
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
		btnGenerateRandomNumber.setBounds(409, 283, 242, 23);
		frame.getContentPane().add(btnGenerateRandomNumber);
		
		JButton btnSendHashTo = new JButton("Enter Bankers ID, 20 lumen, and place Bet");
		btnSendHashTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(newUser.updateBet()){
					try {
						newUser.transferFunds(newUser.getSecretSeed(), accountIDTextField.getText(), amountTextField.getText());
						displayText.setText("Bet Placed!");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnSendHashTo.setBounds(433, 316, 292, 23);
		frame.getContentPane().add(btnSendHashTo);
		
		JButton bankerIDBtn = new JButton("View Banker Account ID");
		bankerIDBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bankerID = newUser.viewBankerID();
				displayText.setText(bankerID);
			}
		});
		bankerIDBtn.setBounds(226, 259, 173, 23);
		frame.getContentPane().add(bankerIDBtn);
		
		
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
}
