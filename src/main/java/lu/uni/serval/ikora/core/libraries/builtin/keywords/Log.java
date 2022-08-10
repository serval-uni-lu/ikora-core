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
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.LogLevelType;
import lu.uni.serval.ikora.core.types.StringType;

public class Log extends LibraryKeyword {
    public Log(){
        super(Type.LOG,
                new StringType("message"),
                new LogLevelType("level", "INFO"),
                new BooleanType("html", BooleanType.FALSE),
                new BooleanType("console", BooleanType.FALSE),
                new BooleanType("repr", BooleanType.FALSE),
                new StringType("formatter", "str")
        );
    }

    @Override
    public void execute(Runtime runtime) {
        runtime.setMessage("INFO", "message");
    }
}
