package com.mayo.mail;

import com.mayo.IMayoService;

public class VerificationMail extends AMail{
	public static final String SUBJECT = "Verification Email";

	public VerificationMail(String to, Long userId){
		super(to,
			SUBJECT,
			"");
		
		// TODO change for real production system
		this.text = "Click on the link to verify your email <a href=\""+ IMayoService.AWS_SERVER +"verify" +
				"Email/"+ userId+"?mainEmail=" + to + "\">click</a>";
				
	}

	/** Constructor
	 * 
	 */
	public VerificationMail(String to) {
		super(to, SUBJECT);
	}

}