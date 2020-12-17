package com.gotTalent.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.gotTalent.config.Config;
import com.gotTalent.enums.EnumConfig;
import com.gotTalent.enums.EnumMessage;


public class ParticipationController {

	Config config;
	Scanner scanner;
	Connection connection;
	
	
	public ParticipationController() throws ClassNotFoundException, SQLException {
		
		config = new Config(EnumConfig.DBURL.getLabel(),EnumConfig.DBUSERNAME.getLabel(),EnumConfig.DBPASSWORD.getLabel());
		scanner = new Scanner(System.in);
		connection = config.connect();
	}
	
	//check id   
	  String rgexNum = "^[0-9]{9}$";
	  String timeRegex = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]";
	 
	  AdminController admin = new  AdminController();
	
	// add participation method 
	public void addParticipation() throws SQLException {
		
		long id = 0;
		  System.out.println("Enter your id:");
		  String idstr = scanner.nextLine();
		  
		  if(idstr.matches(rgexNum)) {
			   id = Long.parseLong(idstr);
		  }else {
             System.out.println(EnumMessage.ONLYDEGITID.getLabel());
             addParticipation();
		  }
		
		//checking if user exist
		String query1 = "SELECT * FROM users WHERE user_id = '"+ id +"' ";
        PreparedStatement pst1 = connection.prepareStatement(query1);
        ResultSet r1= pst1.executeQuery();
        
        if(r1.next()) {
        	System.out.println("Choose your category:");

    		String sqlString = "SELECT * FROM categories";
    		PreparedStatement statement = connection.prepareStatement(sqlString);
    		ResultSet resultSet = statement.executeQuery();
    		
    		while(resultSet.next()) {
    			System.out.println(resultSet.getLong("id_category")+". "+resultSet.getString("category_name"));
    			
    		}
    		
    		String categoryStr = scanner.nextLine();
    		long category = Long.parseLong(categoryStr);
    		//Checking if id category exist
    		if(category > 6) {
    			System.out.println(EnumMessage.CATNOTFOUND.getLabel());
    			addParticipation();
    		}
    		
    		String query11 = "SELECT * FROM participation JOIN users ON participation.user_id=users.user_id WHERE participation.user_id="+id+" AND id_category="+category;
     		PreparedStatement statement1 = connection.prepareStatement(query11);
     		ResultSet resultSet1 = statement1.executeQuery();
     		
     		if(resultSet1.next()) {
     			System.out.println(EnumMessage.CATCHOOSEN.getLabel());
     			addParticipation();
     		}else {
     			System.out.println("Enter the description of your talent:");
        		String desc = scanner.nextLine();
        		//Validate description
        		if(desc.length() < 14) {
        			System.out.println(EnumMessage.DESCRIPTION.getLabel());
        			addParticipation();
        		}
        		
        		
        		
        
        		System.out.println("Enter the start time of your show: (yyyy-mm-dd hh:mm:ss)");
        		String startTime = scanner.nextLine();
        		Timestamp startTimestamp = null;
        		//Check time format
        		if(startTime.matches(timeRegex)) {
        			 startTimestamp = Timestamp.valueOf(startTime);
        		}else {
        			System.out.println(EnumMessage.TIMEFORMAT.getLabel());
        			addParticipation();
        		}
        		
        		System.out.println("Enter the end time of your show: (yyyy-mm-dd hh:mm:ss)");
        		String endTime = scanner.nextLine();
        		Timestamp endTimestamp = null;
        		//still 
        		if(endTime.matches(timeRegex)) {
        			 endTimestamp =Timestamp.valueOf(endTime);
        		}else {
        			System.out.println(EnumMessage.TIMEFORMAT.getLabel());
        			addParticipation();
        		}
        		
        		
        		System.out.println("Enter the path of your attached file:");
        		String file = scanner.nextLine();
        		
        		boolean is_accepted = false;
        		
        		String query = "INSERT into participation (user_id ,id_category, description, show_start_time, show_end_time,attached_file,is_accepted) values(?,?,?,?,?,?,?)";
        		  PreparedStatement statement11 = connection.prepareStatement(query);
        			statement11.setLong(1, id);
        			statement11.setLong(2, category);
        			statement11.setString(3, desc);
        			statement11.setTimestamp(4, startTimestamp);
        			statement11.setTimestamp(5, endTimestamp );
        			statement11.setString(6, file);
        			statement11.setBoolean(7, is_accepted );
        			
        			statement11.executeUpdate();
        		 
        		System.out.println(EnumMessage.CHECKEMAIL.getLabel());
     		}
    		
    		
    		
        }
        else {
        	System.out.printf(EnumMessage.USERNOTFOUND.getLabel(),EnumMessage.REGISTER.getLabel());
        	addParticipation();
        }
       
				
	}
}
