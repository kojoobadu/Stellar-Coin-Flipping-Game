package accounts;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Banker {

	private JFrame frame;
	private JTextArea displayArea;
	private JButton clearDisplaybtn;
	private JLabel lblSource;
	private JTextField sourceTextField;
	private JLabel lblDestination;
	private JTextField destinationTextField;
	private JLabel lblAmount;
	private JTextField amountTextField;
	private JButton btnTransferFunds;
	private Create_User_Account user = new Create_User_Account(userName);
	private JButton btnDeleteAllAccounts;
	private static String userName = "BANKER";
	private JButton btnVerifyHashValues;
	private JButton bankBalancebtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Banker window = new Banker();
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
	public Banker() {
		initialize();
		user.updateBankerID(1);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 657, 452);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		displayArea = new JTextArea();
		displayArea.setBounds(10, 31, 621, 204);
		frame.getContentPane().add(displayArea);
		
		JButton btnViewParticitpants = new JButton("View particitpants");
		btnViewParticitpants.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String participants = user.viewParticipants();
				displayArea.setText(participants);
			}
		});
		btnViewParticitpants.setBounds(26, 261, 143, 23);
		frame.getContentPane().add(btnViewParticitpants);
		
		clearDisplaybtn = new JButton("Clear View");
		clearDisplaybtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayArea.setText("");
			}
		});
		clearDisplaybtn.setBounds(36, 284, 107, 23);
		frame.getContentPane().add(clearDisplaybtn);
		
		lblSource = new JLabel("Participant1 :");
		lblSource.setBounds(9, 309, 75, 14);
		frame.getContentPane().add(lblSource);
		
		sourceTextField = new JTextField();
		sourceTextField.setBounds(86, 306, 143, 20);
		frame.getContentPane().add(sourceTextField);
		sourceTextField.setColumns(10);
		
		lblDestination = new JLabel("Participant2 :");
		lblDestination.setBounds(239, 309, 84, 14);
		frame.getContentPane().add(lblDestination);
		
		destinationTextField = new JTextField();
		destinationTextField.setColumns(10);
		destinationTextField.setBounds(333, 306, 143, 20);
		frame.getContentPane().add(destinationTextField);
		
		lblAmount = new JLabel("Amount :");
		lblAmount.setBounds(486, 309, 49, 14);
		frame.getContentPane().add(lblAmount);
		
		amountTextField = new JTextField();
		amountTextField.setBounds(545, 306, 86, 20);
		frame.getContentPane().add(amountTextField);
		amountTextField.setColumns(10);
		
		btnTransferFunds = new JButton("Transfer Funds");
		btnTransferFunds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String status = null;
				try {
					status = user.transferFunds(sourceTextField.getText(), destinationTextField.getText(), amountTextField.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				displayArea.setText(status);
			}
		});
		btnTransferFunds.setBounds(264, 334, 136, 23);
		frame.getContentPane().add(btnTransferFunds);
		
		btnDeleteAllAccounts = new JButton("Delete all Accounts");
		btnDeleteAllAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.deleteAccounts();
			}
		});
		btnDeleteAllAccounts.setBounds(264, 261, 170, 23);
		frame.getContentPane().add(btnDeleteAllAccounts);
		
		btnVerifyHashValues = new JButton("Verify Hash Values");
		btnVerifyHashValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				displayArea.setText(user.decideWhoWon(user.getSecretSeed()));	
				displayArea.append("\n$Winning transferred");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnVerifyHashValues.setBounds(89, 334, 143, 23);
		frame.getContentPane().add(btnVerifyHashValues);
		
		bankBalancebtn = new JButton("View Bank Balance");
		bankBalancebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayArea.setText(user.querryAccountBalance(user.getKeyPair()));
			}
		});
		bankBalancebtn.setBounds(472, 261, 159, 23);
		frame.getContentPane().add(bankBalancebtn);
		
		JLabel lblWelcomeBanker = new JLabel("Welcome Banker");
		lblWelcomeBanker.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeBanker.setBounds(239, 6, 170, 14);
		frame.getContentPane().add(lblWelcomeBanker);
		
		
	}
	
	
	

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	

}
