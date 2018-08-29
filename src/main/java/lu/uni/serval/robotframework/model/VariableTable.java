package lu.uni.serval.robotframework.model;

import java.util.HashMap;

public class VariableTable extends HashMap<String, Variable> {
    private String file;

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
