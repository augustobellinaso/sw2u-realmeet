package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.domain.entity.Allocation;
import br.com.sw2u.realmeet.email.EmailInfoBuilder;
import br.com.sw2u.realmeet.email.EmailSender;
import br.com.sw2u.realmeet.email.TemplateType;
import org.springframework.stereotype.Service;

import java.util.Map;

import static br.com.sw2u.realmeet.email.TemplateType.*;

@Service
public class NotificationService {
    
    private static final String ALLOCATION = "allocation";
    
    private final EmailSender emailSender;
    private final EmailInfoBuilder emailInfoBuilder;
    
    public NotificationService(EmailSender emailSender, EmailInfoBuilder emailInfoBuilder) {
        this.emailSender = emailSender;
        this.emailInfoBuilder = emailInfoBuilder;
    }
    
    public void notifyAllocationCreated(Allocation allocation) {
        notify(allocation, ALLOCATION_CREATED);
    }
    
    public void notifyAllocationUpdated(Allocation allocation) {
        notify(allocation, ALLOCATION_UPDATED);
    }
    
    public void notifyAllocationDeleted(Allocation allocation) {
        notify(allocation, ALLOCATION_DELETED);
    }
    
    private void notify(Allocation allocation, TemplateType templateType) {
        this.emailSender.send(emailInfoBuilder.createEmailInfo(allocation.getEmployee().getEmail(), templateType,
                                                               Map.of(ALLOCATION, allocation)));
    }
}
