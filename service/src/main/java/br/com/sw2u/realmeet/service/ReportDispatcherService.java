package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.email.EmailInfoBuilder;
import br.com.sw2u.realmeet.email.EmailSender;
import br.com.sw2u.realmeet.email.model.Attachment;
import br.com.sw2u.realmeet.report.model.GeneratedReport;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ReportDispatcherService {
    
    private final EmailSender emailSender;
    private final EmailInfoBuilder emailInfoBuilder;
    
    public ReportDispatcherService(EmailSender emailSender, EmailInfoBuilder emailInfoBuilder) {
        this.emailSender = emailSender;
        this.emailInfoBuilder = emailInfoBuilder;
    }
    
    public void dispatch(GeneratedReport generatedReport) {
        emailSender.send(emailInfoBuilder.createEmailInfo(generatedReport.getEmail(), generatedReport.getTemplateType(), null, List.of(
                                                                  Attachment.newBuilder()
                                                                          .fileName(generatedReport.getFileName())
                                                                          .contentType(generatedReport.getReportFormat().getContentType())
                                                                          .inputStream(new ByteArrayInputStream(generatedReport.getBytes())).build()
                                                          )
                         )
        );
    }
    
}

