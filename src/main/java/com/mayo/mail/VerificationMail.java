package com.mayo.mail;

public class VerificationMail extends AMail{
	public static final String SUBJECT = "Verification Email";

	public VerificationMail(String to, Long userId){
		super(to,
			SUBJECT,
			"");
		
		// TODO change for real production system
		this.text = "Click on the link to verify your email <a href=\"http://ec2-54-214-124-166.us-west-2.compute.amazonaws.com:9090/rest/mayo/verify" +
				"Email/"+ userId+"?mainEmail=" + to + "\">click</a>";
				
	}

	/** Constructor
	 * 
	 */
	public VerificationMail(String to) {
		super(to, SUBJECT);
	}

}