package com.mayo.mail;
import com.mayo.database.hibernate.Users;

public class ConnectionEmail extends AMail{
	public static final String SUBJECT = "Connetion Email";

	public ConnectionEmail(Users user1, Users user2){
		super(user1.getMainEmail(),
				SUBJECT,
				user2.getMainEmail()  + " is interested by you ");
	}
	
	/** For tests */
	public ConnectionEmail(String email) {
		super(email,
				SUBJECT);	}

}