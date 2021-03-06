/**
 * 
 */
package com.webbuild.javabrains;

import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author gce
 *
 */
@SpringBootApplication
public class BeanFactory {
	//.\keytool –genkey –alias ShippingWebsiteSpringSQL –storetype PKCS12 –keyalg RSA –keysize 2048 –keystore bootsecurity.p12 –validity 3650
	
	@Bean
    public ServletWebServerFactory servletContainer() {
        // Enable SSL Trafic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection); 
                context.addConstraint(securityConstraint);
            }
        };

        // Add HTTP to HTTPS redirect
        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());

        return tomcat;
    }

    /*To redirect from HTTP to HTTPS. Without SSL, this application used
    port 8084. With SSL it will use port 8443. So, any request for 8084 needs to be
    redirected to HTTPS on 8443.*/
    private Connector httpToHttpsRedirectConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8084);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
    
    @Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.office365.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("aaron.larch@gce.org");
	    mailSender.setPassword("GC3W!ning!");
	    
	    Properties properties = mailSender.getJavaMailProperties();
		
	    // Your LAN must define the local SMTP server as "mailhost"
		properties.put("mail.transport.protocol", "smtp");//Declare Mail Protocol
		properties.put("mail.smtp.host", "smtp.office365.com"); //SMTP Host must match the from email address not to.
		properties.put("mail.smtp.port", "587"); //TLS Port
		properties.put("mail.smtp.auth", "true"); //enable authentication
		properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		properties.put("mail.debug", "true");
	    return mailSender;
	}
}
