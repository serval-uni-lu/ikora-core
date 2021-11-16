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

import java.io.File;
import java.util.List;

public class Resources {
    private final Token name;
    private final List<Token> arguments;
    private final Token comment;
    private final File file;

    private SourceFile sourceFile;

    public Resources(Token name, File file, List<Token> arguments, Token comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
        this.file = file;
    }

    public Token getName() {
        return this.name;
    }

    public File getFile() {
        return file;
    }

    public SourceFile getSourceFile() {
        return this.sourceFile;
    }

    public void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public boolean isValid() {
        return file != null && sourceFile != null;
    }
}
