package lu.uni.serval.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordData {
    public String name;
    public List<List<String>> arguments;
    public Map<List<String>, String> variables;
    public String file;
    public String documentation;

    public KeywordData(){
        this.arguments = new  ArrayList<List<String>>();
        this.variables =  new HashMap<List<String>, String>();
    }

    @Override
    public String toString() {
        String argumentString = new String();

        for(List<String> argument : arguments) {
            for (String token : argument) {
                argumentString += "\t" + token;
            }
        }

        return this.name + argumentString;
    }
}
