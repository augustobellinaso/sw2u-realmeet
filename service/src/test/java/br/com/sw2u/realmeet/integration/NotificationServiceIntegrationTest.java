package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.config.properties.EmailConfigProperties;
import br.com.sw2u.realmeet.config.properties.TemplateConfigProperties;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.entity.Allocation;
import br.com.sw2u.realmeet.email.EmailSender;
import br.com.sw2u.realmeet.email.TemplateType;
import br.com.sw2u.realmeet.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static br.com.sw2u.realmeet.util.Constants.ALLOCATION_KEY;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newAllocationBuilder;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

class NotificationServiceIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private NotificationService victim;
    
    @Autowired
    private TemplateConfigProperties templateConfigProperties;
    
    @Autowired
    private EmailConfigProperties emailConfigProperties;
    
    @MockBean
    private EmailSender emailSender;
    
    private Allocation allocation;
    
    @Override
    protected void setupEach() throws Exception {
        var room = newRoomBuilder().build();
        allocation = newAllocationBuilder(room).build();
    }
    
    @Test
    void testNotifyAllocationCreated() {
        victim.notifyAllocationCreated(allocation);
        testInteraction(TemplateType.ALLOCATION_CREATED);
    }
    
    @Test
    void testNotifyAllocationDeleted() {
        victim.notifyAllocationDeleted(allocation);
        testInteraction(TemplateType.ALLOCATION_DELETED);
    }
    
    @Test
    void testNotifyAllocationUpdated() {
        victim.notifyAllocationUpdated(allocation);
        testInteraction(TemplateType.ALLOCATION_UPDATED);
    }
    
    private void testInteraction(TemplateType templateType) {
        
        var emailTemplate = templateConfigProperties.getEmailTemplate(templateType);
        
        verify(emailSender).send(argThat(
                                         emailInfo -> emailInfo.getSubject().equals(emailTemplate.getSubject()) &&
                                                 emailInfo.getTo().get(0).equals(allocation.getEmployee().getEmail()) &&
                                                 emailInfo.getFrom().equals(emailConfigProperties.getFrom()) &&
                                                 emailInfo.getTemplate().equals(emailTemplate.getTemplateName()) &&
                                                 emailInfo.gettemplateData().get(ALLOCATION_KEY).equals(allocation)
                                 )
        );
    }
}
