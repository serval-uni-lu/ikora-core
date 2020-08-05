package tech.ikora.analytics.visitor;

import tech.ikora.model.UserKeyword;

public class UserKeywordCounterVisitor extends TreeVisitor {
    private int counter = 0;

    public int getCount() {
        return counter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        ++counter;
        super.visit(keyword, memory);
    }
}
