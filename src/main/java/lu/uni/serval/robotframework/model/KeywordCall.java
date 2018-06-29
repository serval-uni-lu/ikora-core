package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import java.util.ArrayList;
import java.util.List;

public class KeywordCall extends Step {
    private Keyword keyword;
    private List<Argument> parameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
    }

    public void setKeyword(Keyword keyword){
        this.keyword = keyword;

        if(this.keyword != null) {
            addDependency(this.keyword);
        }
    }

    public void addParameter(String argument) {
        this.parameters.add(new Argument(argument));
    }

    public List<Argument> getParameters() {
        return this.parameters;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public Keyword getStep(int position) {
        return keyword.getStep(position);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof KeywordCall)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        KeywordCall call = (KeywordCall)other;

        boolean same = this.parameters.size() == call.parameters.size();

        for(int i = 0; same && i < this.parameters.size(); ++i) {
            same &= this.parameters.get(i).equals(call.parameters.get(i));
        }

        return  same;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
