package lu.uni.serval.robotframework.model;

import java.util.*;

import static com.google.common.primitives.Ints.min;

public class Step {
    private String file;
    private String name;
    private List<String> arguments;
    private KeywordDefinition keyword;

    public void setName(String name) {
        this.name = name;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void setKeyword(KeywordDefinition keyword){
        this.keyword = keyword;
    }

    public String getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getCleanName() {
        return this.name.trim().replaceAll("(?i)^(given|when|then) ", "").trim();
    }

    public List<String> getArguments() {
        return this.arguments;
    }

    public boolean isKeywordLinked(){
        return keyword != null;
    }

    public Map<String, List<String>> fetchVariables(UserKeyword keyword) {
        Map<String, List<String>> variables = new HashMap<>();

        if(!keyword.getName().hasVariable()) {
            return variables;
        }

        List<String> keywordTokens = Arrays.asList(keyword.getName().toString().split(" "));
        List<String> stepTokens = Arrays.asList(this.getCleanName().split(" "));

        int end = min(keywordTokens.size(), stepTokens.size());

        for (int i = 0; i < end; ++i) {
            String keywordToken = keywordTokens.get(i).replace("\"", "");
            final String stepToken = stepTokens.get(i).replace("\"", "");

            if(keywordToken.equalsIgnoreCase(stepToken)) {
                continue;
            }

            if(!Argument.hasVariable(keywordToken)) {
                continue;
            }

            variables.put(keywordToken, Collections.singletonList(stepToken));
        }

        return variables;
    }
}
