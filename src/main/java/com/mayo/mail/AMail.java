package com.mayo.mail;

public class AMail {

	protected String to;
	protected String subject;
	protected String text;

	public AMail(String to, String subject, String text){
		this.to = to;
		this.subject = subject;
		this.text = text;
	}
	
	// For tests
	public AMail(String to, String subject){
		this.to = to;
		this.subject = subject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AMail other = (AMail) obj;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	public static void main(String[] args) {
		long init = System.currentTimeMillis();
		String to = "jsotogaviard@gmail.com";
		String subject = "Test";
		String message = 
				"<a href=\"http://www.google.com\"> click</a> \n";

		AMail sendMail = new AMail(to, subject, message);
		EmailDispatcherThread.send(sendMail, MailSession.createGmailSession());
		System.out.println(System.currentTimeMillis()  - init);
	}
}