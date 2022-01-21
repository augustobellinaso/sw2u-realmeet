package br.com.sw2u.realmeet.email;

import br.com.sw2u.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2u.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2u.realmeet.email.model.Attachment;
import br.com.sw2u.realmeet.email.model.EmailInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EmailInfoBuilder {
    
    private EmailConfigProperties emailConfigProperties;
    private TemplateConfigProperties templateConfigProperties;
    
    public EmailInfoBuilder(
            EmailConfigProperties emailConfigProperties, TemplateConfigProperties templateConfigProperties) {
        this.emailConfigProperties = emailConfigProperties;
        this.templateConfigProperties = templateConfigProperties;
    }
    
    public EmailInfo createEmailInfo(String email, TemplateType templateType, Map<String, Object> templateData) {
        return createEmailInfo(email, templateType, templateData, null);
    }
    
    
    public EmailInfo createEmailInfo(String email, TemplateType templateType, Map<String, Object> templateData, List<Attachment> attachments) {
        var emailTemplate = templateConfigProperties.getEmailTemplate(templateType);
        
        return EmailInfo.newEmailInfoBuilder()
                .from(emailConfigProperties.getFrom())
                .subject(emailTemplate.getSubject())
                .to(List.of(email))
                .templateData(templateData)
                .attachments(attachments)
                .template(emailTemplate.getTemplateName())
                .build();
    }
}
