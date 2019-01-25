package org.ukwikora.utils;

import org.ukwikora.model.Statement;
import org.ukwikora.model.StatementVisitor;

public class VisitorUtils {
    public static void traverseDependencies(StatementVisitor visitor, Statement statement){
        for(Statement dependency: statement.getDependencies()){
            if(visitor.isAcceptable(dependency)){
                dependency.accept(visitor);
            }
        }
    }
}
