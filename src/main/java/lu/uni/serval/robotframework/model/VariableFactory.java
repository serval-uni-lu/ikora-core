package lu.uni.serval.robotframework.model;

public class VariableFactory {
    public static Variable create(String name){
        Variable variable = null;

        String first = name.trim().substring(0, 1);
        if(first.equals("$")){
            variable = new ScalarVariable();
        }
        else if(first.equals("@")){
            variable = new ListVariable();
        }
        else if(first.equals("&")){
            variable = new DictionaryVariable();
        }

        if(variable != null){
            variable.setName(name);
        }

        return variable;
    }
}
