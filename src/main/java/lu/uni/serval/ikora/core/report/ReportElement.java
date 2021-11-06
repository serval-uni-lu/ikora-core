package lu.uni.serval.ikora.core.report;

import lu.uni.serval.ikora.core.exception.BadElementException;

interface ReportElement {
    void addElement(ReportElement element) throws BadElementException;
}
