package lu.uni.serval.ikora.core.model;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.builder.resolver.ValueResolver;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseType;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LibraryVariable implements Node, Dependable {
    protected enum Format{
        SCALAR, LIST, DICTIONARY
    }

    protected final BaseType type;
    protected final Format format;
    protected final Pattern pattern;

    private final Set<SourceNode> dependencies;

    protected LibraryVariable(BaseType type, Format format){
        this.type = type;
        this.format = format;

        this.dependencies = new HashSet<>();

        String patternString = ValueResolver.escape(getName());
        patternString = ValueResolver.getGenericVariableName(patternString);
        this.pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public String getName() {
        String prefix = "$";

        switch (this.format) {
            case SCALAR: prefix = "$"; break;
            case LIST: prefix = "@"; break;
            case DICTIONARY: prefix = "&"; break;
        }

        return String.format("%s{%s}", prefix, this.type.getName());
    }

    @Override
    public String getLibraryName() {
        return Library.BUILTIN;
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

    @Override
    public boolean matches(Token name) {
        String generic = ValueResolver.getGenericVariableName(name.getText());

        Matcher matcher = pattern.matcher(generic);
        return matcher.matches();
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {

    }
}
