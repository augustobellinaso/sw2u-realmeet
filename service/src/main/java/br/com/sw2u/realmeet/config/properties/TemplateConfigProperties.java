package br.com.sw2u.realmeet.config.properties;

import br.com.sw2u.realmeet.config.properties.model.EmailTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "realmeet.email.templates")
@ConstructorBinding
public class TemplateConfigProperties {
    
    private final Map<String, EmailTemplate> emailTemplateMap;
    
    public TemplateConfigProperties(Map<String, EmailTemplate> emailTemplateMap) {
        this.emailTemplateMap = emailTemplateMap;
    }
    
    public EmailTemplate getEmailTemplate(String property) {
        return emailTemplateMap.get(property);
    }
}
