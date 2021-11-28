package lu.uni.serval.ikora.core.builder.parser;

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

import lu.uni.serval.ikora.core.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LineReaderTest {
    @Test
    void checkReadLine_SpecialCharacterReading(){
        try {
            final File robot = FileUtils.getResourceFile("robot/special-characters.robot");
            LineReader reader = new LineReader(robot);

            Line line = reader.readLine();
            assertTrue(line.isValid());
            assertEquals("Line without special characters", line.getText());

            line = reader.readLine();
            assertTrue(line.isValid());
            assertEquals("Line with special characters: êôçàéèù!", line.getText());

            line = reader.readLine();
            assertFalse(line.isValid());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkReadLine_FileEncodedInUTF8WithBom(){
        try {
            final File utf8bom = FileUtils.getResourceFile("files/file-in-utf8-bom.txt");
            LineReader reader = new LineReader(utf8bom);

            Line line = reader.readLine();
            assertTrue(line.isValid());
            assertEquals("Text with strange characters: éèàçù", line.getText());

            line = reader.readLine();
            assertFalse(line.isValid());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}