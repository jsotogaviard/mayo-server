/*
 * (C) Quartet FS 2012
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.rest.services;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import com.mayo.IUserMatcher;
import com.mayo.mail.EmailDispatcherThread;
import com.mayo.mail.MailSession;
import com.mayo.mail.AMail;
//@PropertySource(value="classpath:tcph.properties")
@Configuration
@Import(value={
		MayoService.class
})
public class MayoConfig {


//    @Autowired
//    protected Environment env;
    
    @Bean(name="matcher")
	protected IUserMatcher matcher() {
		return new UserMatcher();
	}
    
    @Bean(name="tokenStore")
   	public TokenStore tokenStore() {
       	return new TokenStore();
   	}
    
    @Bean(name="emailQueue")
   	public ConcurrentLinkedQueue<AMail> emailQueue() {
       	return new ConcurrentLinkedQueue<AMail>();
   	}
    
    @DependsOn(value="emailQueue")
    @Bean()
   	public Void emailDispatcher() {
    	boolean test = Boolean.parseBoolean(System.getProperty(MayoService.TEST_ENV, "false"));
    	Session session ;
		if (test) {
			// It is a test
			session = MailSession.createFakeSession();
		} else {
			// It is a test
			session =  MailSession.createGmailSession();
		}
       	Thread t = new EmailDispatcherThread(session, emailQueue());
       	t.start();
       	return null;
   	}
  
}