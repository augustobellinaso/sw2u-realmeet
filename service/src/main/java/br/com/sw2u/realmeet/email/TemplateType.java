package br.com.sw2u.realmeet.email;

public enum TemplateType {
    
    ALLOCATION_CREATED("allocation-created"),
    ALLOCATION_UPDATED("allocation-updated"),
    ALLOCATION_DELETED("allocation-deleted");
    
    String templateName;
    
    TemplateType(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateName() {
        return templateName;
    }
}
