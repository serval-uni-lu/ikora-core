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
package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.StringType;
import org.apache.commons.lang3.NotImplementedException;

public class ReplaceVariables extends LibraryKeyword {
    public ReplaceVariables(){
        super(Type.SET, new StringType("text"));
    }

    @Override
    public void execute(Runtime runtime) {
        throw new NotImplementedException("Execution logic is not implemented yet!");
    }
}
