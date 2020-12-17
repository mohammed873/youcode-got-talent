package com.gotTalent.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.gotTalent.config.Config;
import com.gotTalent.enums.EnumConfig;
import com.gotTalent.enums.EnumMail;
import com.gotTalent.enums.EnumMessage;
import com.gotTalent.models.Administrator;
import com.gotTalent.models.Participation;
import com.gotTalent.models.User;
import java.sql.Timestamp;

public class AdminController {

	Config config;
	Scanner scanner;
	Connection connection;

	public AdminController() throws ClassNotFoundException, SQLException {


		config = new Config(EnumConfig.DBURL.getLabel(),EnumConfig.DBUSERNAME.getLabel(),EnumConfig.DBPASSWORD.getLabel());
		scanner = new Scanner(System.in);
		connection = config.connect();
	}

	public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException {

		// declaring the array list

		ArrayList<User> usersList = new ArrayList<>();

		String sqlString = "SELECT * FROM users";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {

			User user = new User(resultSet.getLong("user_id"), resultSet.getString("first_name"),
					resultSet.getString("last_name"), resultSet.getString("email"), resultSet.getString("phone"));
			usersList.add(user);

		}

		for (User list : usersList) {
			System.out.println(list.toString());
		}

		return usersList;

	}


	
	//changing the status of admin to connected
	public void adminConnected() throws SQLException {
		
	String sqlString = "Update admin_session SET  is_connected=true WHERE id_admin=15970010";
	PreparedStatement statement = connection.prepareStatement(sqlString);
	statement.executeUpdate();
	System.out.println("Admin Logged in succesfully");
}


	
	//changing the status of admin to disconnected
	public void adminDisconnected() throws SQLException {

		String sqlString = "Update admin_session SET  is_connected=false WHERE id_admin=15970010";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		statement.executeUpdate();
		System.out.println("Admin Logged out succesfully");
	}

	
	
	public boolean isLoggedin() throws SQLException {
		String sqlString = "SELECT * FROM admin_session";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		ResultSet resultSet = statement.executeQuery();
		return resultSet.getBoolean("is_connected");

	}

	public ArrayList<Participation> getParticipations() throws SQLException {

		ArrayList<Participation> participationList = new ArrayList<>();

		String sqlString = "SELECT * FROM participation";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {

			Participation participation = new Participation(resultSet.getLong("user_id"),
					resultSet.getLong("id_category"), resultSet.getString("description"), resultSet.getTimestamp("show_start_time"),
					resultSet.getTimestamp("show_end_time"),resultSet.getString("attached_file"),resultSet.getBoolean("is_accepted"));
			
			participationList.add(participation);

		}



		return participationList;
	}
	

	public Participation getParticipationByEmail() throws SQLException {

		Participation participation  = new Participation();
		
		System.out.println("Enter the email our the participation you are looking for:");
		String email = scanner.nextLine();

		String sqlString = "SELECT * FROM participation JOIN users ON participation.user_id=users.user_id WHERE email=?";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		statement.setString(1, email);
		ResultSet resultSet = statement.executeQuery();

		if(resultSet.next()) {
		
			participation.setId_user(resultSet.getLong("user_id"));
			participation.setId_category(resultSet.getLong("id_category"));
			participation.setDescription(resultSet.getString("description"));
			participation.setShow_start_time(resultSet.getTimestamp("show_start_time"));
			participation.setShow_end_time(resultSet.getTimestamp("show_end_time"));
			participation.setAttached_file(resultSet.getString("attached_file"));
			participation.setIs_accepted(resultSet.getBoolean("is_accepted"));

		}else {
			System.out.println(EnumMessage.EMAILNOTFOUND.getLabel());
			getParticipationByEmail();
			}
		return participation;
		
	}
	
	public void validateParticipation() throws SQLException {
		
		System.out.println("To validate a participation you will need both the id and the category of the participant");
		System.out.println("Enter the id of the paricipant :");
		int userId = scanner.nextInt();
		System.out.println("Enter the id of the category of the paricipation :");
		int catId = scanner.nextInt();
		
		
		System.out.println("Select action :");
		System.out.println("1. Accept");
		System.out.println("2. Deny");
		int action = scanner.nextInt();
		
		if(action == 1) {
			String email;
			String query = "Update participation SET  is_accepted=true WHERE user_id="+userId+" AND id_category="+catId;
             PreparedStatement statement =connection.prepareStatement(query);
             statement.executeUpdate();
             
            String query1 = "SELECT * FROM participation JOIN users ON participation.user_id=users.user_id WHERE participation.user_id="+userId+" AND id_category="+catId;
     		PreparedStatement statement1 = connection.prepareStatement(query1);
     		ResultSet resultSet = statement1.executeQuery();
     		
     		if(resultSet.next()) {
    			email = resultSet.getString("email");
    			sendMail(email);
    		}
     		//976211752
            System.out.println(EnumMessage.PARTACC.getLabel());
            
		}else if(action == 0) {
			
			System.out.println(EnumMessage.PARTDENIED.getLabel());
		}
	
		
	
		
	}
	

	public void sendMail(String recEmail) {

        Properties properties = new Properties();

        properties.put(EnumMail.MAILAUTH.getLabel(), EnumMail.MAILAUTH.getLabel1());
        properties.put(EnumMail.MAILSTARTTLS.getLabel(), EnumMail.MAILSTARTTLS.getLabel1());
        properties.put(EnumMail.MAILAUTH.getLabel(), EnumMail.MAILAUTH.getLabel1());
        properties.put(EnumMail.MAILHOST.getLabel(), EnumMail.MAILHOST.getLabel1());
        properties.put(EnumMail.MAILPORT.getLabel(), EnumMail.MAILPORT.getLabel1());
        String myEmail = EnumMail.MAILADR.getLabel();
        String password = EnumMail.MAILPSWRD.getLabel();

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });


	
	


        try {
             MimeMessage message = new MimeMessage(session);
             message.setFrom(new InternetAddress(myEmail));
             message.addRecipient(Message.RecipientType.TO,new InternetAddress(recEmail));
             message.setSubject("YouCode Got Talent");
             message.setText("Congratulations ! Your participation to Youcode Got Talent has been accepted!");

            //send the message
             Transport.send(message);

             System.out.println(EnumMessage.EMAILSENT.getLabel());

             } catch (MessagingException e) {e.printStackTrace();}
         }
	
	
	
	
	
	
	
	
	
	
	

}
