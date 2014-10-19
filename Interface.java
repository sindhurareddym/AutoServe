package main.java;
import main.java.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

class UserInterface extends JFrame implements ActionListener{
	//session information
	private String userId;
	private String userType;

	//main windows
	private JPanel userInterface = new JPanel();
	private CardLayout card = new CardLayout();
	
	//fields for logInPanel
	private JPanel logInPanel = new JPanel();
	private JLabel autoServeLogo = new JLabel(new ImageIcon("../img/autoServeLogo.png"));
	private JLabel idLabel = new JLabel("ID : ", JLabel.LEFT);
	private JLabel passLabel = new JLabel("PW : ", JLabel.LEFT);
	private JTextField idText = new JTextField(10);
	private JPasswordField passPass = new JPasswordField(10);
	private JButton logInButton = new JButton("Log in");
	private JButton noMembershipButton = new JButton("No membership");


	//fields for managerPanel
	private JPanel managerPanel = new JPanel();
	private JButton mngLogOutBtn = new JButton("Log out");
	private JButton mngManageMenu = new JButton("Manage");

	private JPanel kitchenAssistantPanel = new JPanel();
	private JPanel waiterPanel = new JPanel();
	private JPanel menuPanel = new JPanel();


	public void setManagerPanel(){
		this.managerPanel.setLayout(null);

		this.managerPanel.setBackground(Color.GRAY);

		JLabel mngLabel = new JLabel("Manager Panel");
		mngLabel.setBounds(200,0, 100, 25);
		this.managerPanel.add(mngLabel);
		this.managerPanel.setBounds(200,400,100,50);
		this.managerPanel.add(this.mngManageMenu);
		this.mngLogOutBtn.setBounds(200,500,100,50);
		this.managerPanel.add(this.mngLogOutBtn);
	}
	public void setKitchenAssistantPanel(){
		this.kitchenAssistantPanel.add(new JLabel("Kitchen Assistant Panel", JLabel.CENTER));
	}
	public void setWaiterPanel(){
		this.waiterPanel.add(new JLabel("Waiter Panel", JLabel.CENTER));
	}
	public void setMenuPanel(){
		this.menuPanel.add(new JLabel("Menu Panel", JLabel.CENTER));
	
	}
	//constructors
	public UserInterface(){
		super("AutoServe");

		this.setEvent();

		this.init();
	}
	
	//methods
	public void logIn(String tempId, String tempPass){
		DatabaseControlObject dbCO = new DatabaseControlObject();
		dbCO.openConnection();
		
		String query = "select * from employee where id = ? and pass = ?";
		try{
			dbCO.setPreparedStatement(dbCO.getConnection().prepareStatement(query));
				dbCO.getPreparedStatement().setString(1, tempId);
				dbCO.getPreparedStatement().setString(2, tempPass);

			dbCO.setResultSet(dbCO.getPreparedStatement().executeQuery());

			if(dbCO.getResultSet().next()){	//valid id & password
				//save session information
				this.userId = tempId;
				this.userType = dbCO.getResultSet().getString(4);

				if(this.userType.equals("M")){
					this.card.show(this.userInterface, "managerPanel");
				}
				else if(this.userType.equals("KA")){
				}
				else if(this.userType.equals("W")){
				}
			}
			else{
				JDialog dlg = new JDialog(this, "Warning");
					Container dlgcon = dlg.getContentPane();
					dlgcon.setLayout(new BorderLayout());
					dlgcon.add("Center", new JLabel("ID or password does not exist", JLabel.CENTER));
				dlg.setSize(300, 150);
				dlg.setLocation(300, 300);
				dlg.setVisible(true);
			}
		}catch(SQLException sqle){
			System.err.println("SQL Exception");
			return;
		}

		dbCO.closeConnection();
	}
	public void logOut(){
		this.idText.setText("");
		this.passPass.setText("");
		this.card.show(this.userInterface, "logInPanel");
	}

	//methods
	public void setLogInPanel(){
		this.logInPanel.setLayout(null);
		this.logInPanel.setBackground(new Color(250, 255, 168));

		this.idLabel.setBounds(95, 450, 30, 25);
		this.idLabel.setFont(new Font(this.idLabel.getName(), Font.BOLD, 11));
		this.logInPanel.add(this.idLabel);

		this.idText.setBounds(130, 450, 155, 25);
		this.logInPanel.add(this.idText);

		this.passLabel.setBounds(95, 480, 30, 25);
		this.passLabel.setFont(new Font(this.passLabel.getName(), Font.BOLD, 11));
		this.logInPanel.add(this.passLabel);

		this.passPass.setBounds(130, 480, 155, 25);
		this.logInPanel.add(this.passPass);

		this.logInButton.setBounds(290, 450, 55, 55);
		this.logInButton.setFont(new Font(this.logInButton.getName(), Font.BOLD, 11));
		this.logInButton.setHorizontalAlignment(SwingConstants.CENTER);
		this.logInButton.setText("<html><body>Log<br>in</body></html>");
		this.logInPanel.add(this.logInButton);
		
		this.noMembershipButton.setBounds(350, 450, 55, 55);
		this.noMembershipButton.setFont(new Font(this.noMembershipButton.getName(), Font.BOLD, 11));
		this.noMembershipButton.setHorizontalAlignment(SwingConstants.CENTER);
		this.noMembershipButton.setText("<html><body>No<br>member</body></html>");
		this.logInPanel.add(this.noMembershipButton);

		this.autoServeLogo.setBounds(125, 600, 250, 70);
		this.logInPanel.add(this.autoServeLogo);
	}
	public void init(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
		this.setSize(500, 750);
		this.setLocation(250, 250);

		this.userInterface.setLayout(this.card);
			this.setLogInPanel();
			this.userInterface.add("logInPanel", this.logInPanel);

			this.setManagerPanel();
			this.userInterface.add("managerPanel", this.managerPanel);

		this.add(this.userInterface);
		this.setVisible(true);
		this.card.show(this.userInterface, "logInPanel");
	}
	public void setEvent(){
		this.logInButton.addActionListener(this);
		this.mngLogOutBtn.addActionListener(this);
		this.mngManageMenu.addActionListener(this);
		this.noMembershipButton.addActionListener(this);
	}

	//event handlers
	public void actionPerformed(ActionEvent e){
		String tempId = null;
		String tempPass = null;

		if(e.getSource() == this.logInButton){
			tempId = new String(this.idText.getText());
			tempPass = new String(this.passPass.getPassword()); 

			this.logIn(tempId, tempPass);
		}
		else if(e.getSource() == this.mngLogOutBtn){
			this.userId = "";
			this.userType = "";

			this.logOut();
		}
		else if(e.getSource() == this.noMembershipButton){
		
		}



	}
}
