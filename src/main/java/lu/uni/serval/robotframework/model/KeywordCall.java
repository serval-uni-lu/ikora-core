package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

public class KeywordCall extends Step {
    private Keyword keyword;

    public void setKeyword(Keyword keyword){
        this.keyword = keyword;

        if(this.keyword != null) {
            this.getParent().getNode().add(this.keyword);
        }
    }

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
