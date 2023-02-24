/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.libraries;

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.*;

import static lu.uni.serval.ikora.core.runner.ArgumentFetcher.fetch;

public abstract class LibraryKeyword implements Keyword {
    protected final Type type;
    protected final BaseTypeList argumentTypes;
    protected final BaseTypeList returnTypes;
    private final Set<SourceNode> dependencies;

    protected LibraryKeyword(Type type, BaseType... argumentTypes) {
        this.type = type;
        this.argumentTypes = new BaseTypeList(Arrays.asList(argumentTypes));
        this.returnTypes = new BaseTypeList();
        this.dependencies = new HashSet<>();
    }

    protected LibraryKeyword(Type type, BaseTypeList argumentTypes, BaseTypeList returnTypes) {
        this.type = type;
        this.argumentTypes = argumentTypes;
        this.returnTypes = returnTypes;
        this.dependencies = new HashSet<>();
    }

    protected LibraryKeyword(Type type){
        this.type = type;
        this.argumentTypes = new BaseTypeList();
        this.returnTypes = new BaseTypeList();
        this.dependencies = new HashSet<>();
    }

    public abstract void execute(Runtime runtime) throws RunnerException;

    public Type getType(){
        return this.type;
    }

    public BaseTypeList getArgumentTypes(){
        return this.argumentTypes;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public String getName(){
        return toKeyword(this.getClass());
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    public String getLibraryName(){
        String[] packages = this.getClass().getCanonicalName().split("\\.");

        for(int i = 0; i < packages.length; ++i){
            if(packages[i].equalsIgnoreCase("libraries") && i < packages.length - 1){
                return packages[i + 1];
            }
        }

        return "";
    }

    protected <T> T getValue(String name, Runtime runtime, Class<T> type) throws RunnerException {
        return fetch(runtime.getArguments(), name, argumentTypes, type);
    }

    @Override
    public boolean matches(Token name) {
        return ValueResolver.matches(getName(), name.getText());
    }

    public Documentation getDocumentation(){
        return new Documentation();
    }

    @Override
    public void addDependency(SourceNode node) {
        if(node == null){
            return;
        }

        this.dependencies.add(node);
    }

    @Override
    public void removeDependency(SourceNode node) {
        this.dependencies.remove(node);
    }

    @Override
    public Set<SourceNode> getDependencies() {
        return this.dependencies;
    }
}
