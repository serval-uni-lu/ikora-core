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
package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.StringType;
import org.apache.commons.lang3.NotImplementedException;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private static final Pattern spacePattern = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?}$", Pattern.CASE_INSENSITIVE);

    public Space(){
        super(new StringType("SPACE"), Format.SCALAR);
    }

    @Override
    public boolean matches(Token name) {
        return spacePattern.matcher(name.getText()).matches();
    }

    @Override
    public BaseType execute(Runtime runtime) {
        throw new NotImplementedException();
    }
}
