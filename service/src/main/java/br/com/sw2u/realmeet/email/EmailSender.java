package br.com.sw2u.realmeet.email;

import br.com.sw2u.realmeet.email.model.EmailInfo;
import br.com.sw2u.realmeet.exception.EmailSendingException;
import br.com.sw2u.realmeet.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static br.com.sw2u.realmeet.util.StringUtils.join;
import static java.util.Objects.nonNull;

@Service
public class EmailSender {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;
    
    public EmailSender(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    
    public void send(EmailInfo emailInfo) {
        LOGGER.info("Sending e-mail with subject '{}' to '{}'", emailInfo.getSubject(), emailInfo.getTo());
        
        var mimeMessage = javaMailSender.createMimeMessage();
        var multipart = new MimeMultipart();
    
        addBasicDetails(emailInfo, mimeMessage);
    }
    
    private void addBasicDetails(EmailInfo emailInfo, MimeMessage mimeMessage) {
        try {
            mimeMessage.setFrom(emailInfo.getFrom());
            mimeMessage.setSubject(emailInfo.getSubject());
            mimeMessage.addRecipients(Message.RecipientType.TO, join(emailInfo.getTo()));
        
            if (nonNull(emailInfo.getCc())) {
                mimeMessage.addRecipients(Message.RecipientType.CC, join(emailInfo.getCc()));
            }
        
            if (nonNull(emailInfo.getBcc())) {
                mimeMessage.addRecipients(Message.RecipientType.BCC, join(emailInfo.getBcc()));
            }
        } catch (MessagingException e) {
            throwEmailSendingException(e, "Error adding data to MIME Message");
        }
    }
    
    private void throwEmailSendingException(Exception exception, String errorMessage) {
        var fullErrorMessage = String.format("%s: %s", exception.getMessage(), errorMessage);
        LOGGER.error(fullErrorMessage);
        throw new EmailSendingException(fullErrorMessage, exception);
    }
}
