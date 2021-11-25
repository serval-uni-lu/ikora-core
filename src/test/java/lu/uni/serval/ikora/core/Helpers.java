package lu.uni.serval.ikora.core;

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

import lu.uni.serval.ikora.core.builder.*;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Token;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class Helpers {
    public static double epsilon = 0.0001;

    public static Project compileProject(String resourcesPath, boolean resolve) {
        File projectFolder = null;
        try {
            projectFolder = lu.uni.serval.ikora.core.utils.FileUtils.getResourceFile(resourcesPath);
        } catch (Exception e) {
            fail(String.format("Failed to load '%s': %s", resourcesPath, e.getMessage()));
        }

        final BuildResult result = Builder.build(projectFolder, getConfiguration(), resolve);
        Assertions.assertEquals(1, result.getProjects().size());

        return result.getProjects().iterator().next();
    }

    public static File getNewTmpFolder(String name){
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath);
        File directory = new File(tmpDir, name);
        deleteDirectory(directory);

        assertFalse(directory.exists());

        return directory;
    }

    public static void deleteDirectory(File directory){
        if(directory.exists()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                fail("Failed to clean " + directory.getAbsolutePath());
            }
        }
    }

    public static BuildConfiguration getConfiguration(){
        final BuildConfiguration configuration = new BuildConfiguration();
        configuration.setExtensions(Collections.singletonList("robot"));
        configuration.setIgnorePath(Collections.emptyList());

        return configuration;
    }

    public static LineReader getLineReader(String text) throws IOException {
        LineReader lineReader = new LineReader(text);
        lineReader.readLine();

        return lineReader;
    }

    public static LineReader getLineReader(String... tokens) throws IOException {
        String line = String.join("\t", tokens);
        return getLineReader(line);
    }

    public static Iterator<Token> getTokenIterator(LineReader reader) throws IOException {
        return TokenScanner.from(LexerUtils.tokenize(reader))
                .skipIndent(true)
                .skipTypes(Token.Type.CONTINUATION)
                .iterator();
    }
}
