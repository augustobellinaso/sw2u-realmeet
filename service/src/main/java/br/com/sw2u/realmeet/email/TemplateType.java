package br.com.sw2u.realmeet.email;

public enum TemplateType {
    
    ALLOCATION_CREATED("allocationCreated"),
    ALLOCATION_UPDATED("allocationUpdated"),
    ALLOCATION_DELETED("allocationDeleted");
    
    String templateName;
    
    TemplateType(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateName() {
        return templateName;
    }
}
