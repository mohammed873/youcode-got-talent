package com.gotTalent.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

import com.gotTalent.config.Config;
import com.gotTalent.enums.EnumConfig;
import com.gotTalent.enums.EnumMessage;
import com.gotTalent.models.User;

public class UserController {
	

	Config config;
	Scanner scanner;
	Connection connection;
	User user;
	
	public UserController() throws ClassNotFoundException, SQLException {
		
		config = new Config(EnumConfig.DBURL.getLabel(),EnumConfig.DBUSERNAME.getLabel(),EnumConfig.DBPASSWORD.getLabel());
		scanner = new Scanner(System.in);
		connection = config.connect();
		user  = new User();
		
	}
	
	//phone number regex
	  String phoneRegex = "^(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}$";
	//email regex
	  String emilRegex = "^(.+)@(.+)$";
	//check id   
	  String rgexNum = "^[0-9]{9}$";
	
	//adding new users
	public void addUser() throws SQLException, ClassNotFoundException {
		
		  //generate random id
		  Random rd = new Random();
	      long id = (long)(rd.nextDouble()*100000000L);
		
	  	  System.out.println("Enter your first name:");
		  String fname = scanner.nextLine();
		  //Validating first name
		  if(fname.length() < 3) {
			  System.out.println(EnumMessage.FNAMEVALID.getLabel());
			  addUser();
		  }
		  
		  System.out.println("Enter your last name:");
		  String lname = scanner.nextLine();
		  //Validating last name
		  if(lname.length() < 3) {
			  System.out.println(EnumMessage.LNAMEVALID.getLabel());
			  addUser();
		  }
		  
		  System.out.println("Enter your email:");
		  String email = scanner.nextLine();
		  //Validating email
          if(email.matches(emilRegex)) {
        	  //correct email format
          }else {
        	  System.out.println(EnumMessage.EMAILVALID.getLabel());
        	  addUser();
          }
		  
		  System.out.println("Enter your phone number:");
		  String phone = scanner.nextLine();
		  //Validating email
          if(phone.matches(phoneRegex)) {
        	  //correct email format
          }else {
        	  System.out.println(EnumMessage.PHONEVALID.getLabel());
        	  addUser();
          }
		 
		  String sqlString = "INSERT into users (user_id ,first_name, last_name, email, phone) values(?,?,?,?,?)";
		  PreparedStatement statement = connection.prepareStatement(sqlString);
			statement.setLong(1, id);
			statement.setString(2, fname);
			statement.setString(3, lname);
			statement.setString(4, email);
			statement.setString(5, phone );
			statement.executeUpdate();
		 
		System.out.println(EnumMessage.SIGNUP.getLabel()+id);
	}
	
	
	
	//Finding user by id
	public  User findUserById() throws SQLException, ClassNotFoundException {
		
		System.out.println("Enter the id of the user you are looking for:");
		long id = scanner.nextLong();
		String sqlString = "SELECT * FROM users WHERE user_id = ?";
		PreparedStatement statement = connection.prepareStatement(sqlString);
		statement.setLong(1, id);
		
		ResultSet resultSet = statement.executeQuery();

		//checking if user exist
		if(resultSet.next()) {
			user.setId(id);
			user.setFname(resultSet.getString("first_name"));
			user.setLname(resultSet.getString("last_name"));
			user.setEmail(resultSet.getString("email"));
			user.setPhone(resultSet.getString("phone"));
				
		}
		else {
			System.out.println(EnumMessage.USERNOTFOUND.getLabel());
			findUserById();
		}
		
		return user;
	}
	
	
	
	//Update user info
	public void updateUser() throws ClassNotFoundException, SQLException {
		
		  long id = 0;
		  System.out.println("Enter your id:");
		  String idstr = scanner.nextLine();
		  
		  if(idstr.matches(rgexNum)) {
			   id = Long.parseLong(idstr);
		  }else {
               System.out.println(EnumMessage.ONLYDEGITID.getLabel());
		  }
		 
		  
		  //checking if user exist
		  String query1 = "SELECT * FROM users WHERE user_id = '"+ id +"' ";
	      PreparedStatement pst1 = connection.prepareStatement(query1);
	      ResultSet r1= pst1.executeQuery();
	      
	     
	      
	      if(r1.next()) {
	    	  System.out.println("Enter your first name:");
			  String fname = scanner.nextLine();
			  //Validating first name
			  if(fname.length() < 3) {
				  System.out.println(EnumMessage.FNAMEVALID.getLabel());
				  updateUser();
			  }
			  
			  System.out.println("Enter your last name:");
			  String lname = scanner.nextLine();
			  //Validating last name
			  if(lname.length() < 3) {
				  System.out.println(EnumMessage.LNAMEVALID.getLabel());
				  updateUser();
			  }
			  
			  System.out.println("Enter your email:");
			  String email = scanner.nextLine();
			  //Validating email
	          if(email.matches(emilRegex)) {
	        	  //correct email format
	          }else {
	        	  System.out.println(EnumMessage.EMAILVALID.getLabel());
	        	  updateUser();
	          }
			  
			  System.out.println("Enter your phone number:");
			  String phone = scanner.nextLine();
			 
	          if(phone.matches(phoneRegex)) {
	        	 
	          }else {
	        	  System.out.println(EnumMessage.PHONEVALID.getLabel());
	        	  updateUser();
	          }
	          
			  String sqlString = "update  users SET  first_name=?, last_name=?, email=?, phone=? WHERE user_id=?";
			  java.sql.PreparedStatement statement = connection.prepareStatement(sqlString);
				statement.setString(1, fname);
				statement.setString(2, lname);
				statement.setString(3, email);
				statement.setString(4, phone );
				statement.setLong(5, id);
				statement.executeUpdate();
			 
			System.out.println(EnumMessage.UPDATE.getLabel());
		
	      }else {
	    	  System.out.printf(EnumMessage.USERNOTFOUND.getLabel(),EnumMessage.REGISTER.getLabel());
	    	  updateUser();
	      }
	}
}
