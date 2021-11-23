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

public class Resources extends SourceNode {
    private final Token label;
    private final Token filePath;
    private final File file;
    private final List<Token> arguments;
    private final Token comment;

    public Resources(Token label, Token filePath, List<Token> arguments, Token comment) {
        this.label = label;
        this.arguments = arguments;
        this.comment = comment;
        this.filePath = filePath;

        this.file = new File(this.filePath.getText());
    }

    public boolean isValid(){

    }

    public File getFile() {
        return this.file;
    }

    public Token getFilePath() {
        return this.filePath;
    }

    @Override
    public Token getDefinitionToken() {
        return null;
    }

    @Override
    public double distance(SourceNode other) {
        return 0;
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

    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {

    }
}
