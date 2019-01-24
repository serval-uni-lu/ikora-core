package org.ukwikora.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class ExecutionReport {
    Logger logger = LogManager.getLogger(ExecutionReport.class);

    private List<ExecutionResult> results;
    private String name;

    public ExecutionReport() {
        results = new ArrayList<>();
    }

    public void addResult(ExecutionResult result) {
        results.add(result);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print() {
        logger.info(name);

        for(ExecutionResult result : results) {
            if(result.isError()){
                logger.info(result.getMessage());
            }
            else{
                logger.error(result.getMessage());
            }
        }
    }
}
