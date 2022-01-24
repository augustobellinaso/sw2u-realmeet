package br.com.sw2u.realmeet.report.enumeration;

public enum ReportFormat {
    PDF("application/pdf"),
    XML("application/xml");
    
    String contentType;
    
    ReportFormat(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public String getExtension() {
        return "." + this.name().toLowerCase();
    }
    
    public static ReportFormat defaultReport() {
        return PDF;
    }
}
