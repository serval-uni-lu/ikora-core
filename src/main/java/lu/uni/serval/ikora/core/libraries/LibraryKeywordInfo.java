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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.model.LibraryInfo;
import lu.uni.serval.ikora.core.types.BaseType;
import org.apache.commons.lang3.NotImplementedException;

@JsonDeserialize(using = LibraryKeywordInfoReader.class)
public class LibraryKeywordInfo extends LibraryKeyword {
    private final String name;
    private LibraryInfo library;

    public LibraryKeywordInfo(Type type, String name, BaseTypeList argumentTypes) {
        super(type, argumentTypes.toArray(new BaseType[0]));

        this.name = name;
        this.library = null;
    }

    public LibraryInfo getLibrary() {
        return library;
    }

    public void setLibrary(LibraryInfo library) {
        this.library = library;
    }

    @Override
    public String getLibraryName(){
        return this.library.getName();
    }

    @Override
    public void execute(Runtime runtime) {
        throw new NotImplementedException("Execution logic is not implemented yet!");
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
