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
package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

class SuiteFactory {
    private SuiteFactory() {}

    static Suite create(SourceFile sourceFile){
        return create(null, sourceFile);
    }

    static Suite create(Suite parent, SourceFile sourceFile){
        String name = computeName(parent, sourceFile, true);
        File source = computeSource(parent, sourceFile);

        Suite suite = new Suite(name, source);
        suite.addSourceFile(sourceFile);

        return suite;
    }

    public static String computeName(SourceFile sourceFile, boolean beautiful){
        return computeName(null, sourceFile, beautiful);
    }

    static String computeName(Suite parent, SourceFile sourceFile, boolean beautiful){
        String name = sourceFile.getName();

        if(parent != null){
            Path base = Paths.get(parent.getSource().getAbsolutePath().trim());
            Path path = Paths.get(sourceFile.getSource().getAbsolutePath().trim()).normalize();

            name = base.relativize(path).toString();
        }

        name =  Paths.get(name).getName(0).toString();

        if(beautiful){
            name = FilenameUtils.removeExtension(name);
            name = StringUtils.toBeautifulName(name);
        }

        return name;
    }

    private static File computeSource(Suite parent, SourceFile sourceFile){
        if(sourceFile.getSource().isInMemory()){
            return null;
        }

        File base;

        if(parent != null){
            base = parent.getSource();
        }
        else {
            base = sourceFile.getProject().getRootFolder().asFile();

            if(base.isFile()){
                base = base.getParentFile();
            }
        }

        return new File(base, computeName(parent, sourceFile, false));
    }
}
