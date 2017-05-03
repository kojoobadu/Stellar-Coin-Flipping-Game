package accounts;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Login {

	private JFrame frame;	
	private JTextField nameTextField;
	private Participant participant;
	private Banker banker;
	private int selected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblWelcomeToOur = new JLabel("Welcome to the Coin Flipping System");
		lblWelcomeToOur.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToOur.setBounds(93, 11, 247, 14);
		frame.getContentPane().add(lblWelcomeToOur);
		
		JLabel lblWhichKindOf = new JLabel("Which kind of user are you:");
		lblWhichKindOf.setBounds(27, 36, 150, 14);
		frame.getContentPane().add(lblWhichKindOf);
		
		String[] choices = {"Participant", "Banker"};
		
		JComboBox<String> selectTypeComboBox = new JComboBox<String>(choices);
		selectTypeComboBox.setBounds(187, 33, 87, 20);
		frame.getContentPane().add(selectTypeComboBox);
		
		JLabel lblEnterYourName = new JLabel("Enter Your Name:");
		lblEnterYourName.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterYourName.setBounds(27, 70, 150, 14);
		frame.getContentPane().add(lblEnterYourName);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(187, 67, 86, 20);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selected == 1){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								participant = new Participant(nameTextField.getText());
								participant.frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				else if(selected == 2){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								if( ! checkBanker()){
									banker = new Banker();
									banker.getFrame().setVisible(true);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		btnCreateAccount.setBounds(142, 118, 131, 23);
		frame.getContentPane().add(btnCreateAccount);
		
		
		selectTypeComboBox.addActionListener(new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selectItem =  (String) ((JComboBox<?>) arg0.getSource()).getSelectedItem();
				if(selectItem.equals("Participant")){
					selected = 1;
				}
				else if(selectItem.equals("Banker")){
					selected = 2;
					nameTextField.setVisible(false);
				}
			}
			
		});
	}
	
	public boolean checkBanker(){
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
