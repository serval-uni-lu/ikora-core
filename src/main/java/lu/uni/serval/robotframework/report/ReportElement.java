package lu.uni.serval.robotframework.report;

public interface ReportElement {
    ReportElement getParent();
    ReportElement getRootElement();
    String getSource();
}
