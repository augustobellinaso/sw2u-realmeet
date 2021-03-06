package br.com.sw2u.realmeet.config.properties;

import br.com.sw2u.realmeet.config.properties.model.EmailTemplate;
import br.com.sw2u.realmeet.email.TemplateType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@ConfigurationProperties(prefix = "realmeet.email")
@ConstructorBinding
public class TemplateConfigProperties {
    
    private final Map<String, EmailTemplate> templates;
    
    public TemplateConfigProperties(Map<String, EmailTemplate> templates) {
        this.templates = templates;
    }
    
    public EmailTemplate getEmailTemplate(TemplateType templateType) {
        return templates.get(templateType.getTemplateName());
    }
}
