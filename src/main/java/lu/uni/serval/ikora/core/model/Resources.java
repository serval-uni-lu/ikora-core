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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class Resources extends SourceNode {
    private final Token label;
    private final Token filePath;
    private final List<Token> arguments;

    public Resources(Token label, Token filePath, List<Token> arguments) {
        this.label = label;
        this.arguments = arguments;
        this.filePath = filePath;
    }

    public Token getLabel() {
        return this.label;
    }

    public boolean isValid(){
        if(this.filePath.isEmpty()){
            return false;
        }

        return getFile().exists();
    }

    public File getFile() {
        final File file = new File(this.filePath.getText());

        if(file.isAbsolute()){
            return file;
        }

        return new File(new File(getSourceFile().getDirectory()), this.filePath.getText());
    }

    public Optional<SourceFile> getTarget(){
        return getProject().getSourceFile(getFile().toURI());
    }

    public Token getFilePath() {
        return this.filePath;
    }

    public List<Token> getArguments() {
        return this.arguments;
    }

    @Override
    public Token getDefinitionToken() {
        return null;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        return null;
    }

    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        // runtime not implemented yet
    }
}
