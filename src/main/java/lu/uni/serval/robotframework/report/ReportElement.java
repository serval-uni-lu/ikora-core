package lu.uni.serval.robotframework.report;

public interface ReportElement {
    ReportElement getParent();
    ReportElement getRootElement();
    String getSource();
    int getChildPosition(ReportElement element, boolean ignoreGhosts);
}
