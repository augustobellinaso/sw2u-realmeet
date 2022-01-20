package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.email.EmailSender;
import br.com.sw2u.realmeet.email.model.EmailInfo;
import br.com.sw2u.realmeet.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

import static br.com.sw2u.realmeet.utils.TestUtils.sleep;
import static org.mockito.Mockito.*;

class SendEmailIntegrationTest extends BaseIntegrationTest {
    private static final String EMAIL_ADDRESS = "xyz@gmail.com";
    private static final String EMAIL_SUBJECT = "Teste de envio de email";
    private static final String EMAIL_TEMPLATE = "template-test.html";
    
    @Autowired
    private EmailSender victim;
    
    @MockBean
    private JavaMailSender javaMailSender;
    
    @Mock
    private MimeMessage mimeMessage;
    
    @Test
    void testSendEmail() {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        var emailInfo = EmailInfo.newEmailInfoBuilder()
                .from(EMAIL_ADDRESS)
                .to(List.of(EMAIL_ADDRESS))
                .subject(EMAIL_SUBJECT)
                .template(EMAIL_TEMPLATE)
                .templateData(Map.of("param", "some text"))
                .build();
        
        victim.send(emailInfo);
        sleep(1000);
        verify(javaMailSender).send(mimeMessage);
    }
}
