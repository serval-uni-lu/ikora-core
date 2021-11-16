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

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<Token, Token> data;

    public Metadata(){
        this.data = new HashMap<>();
    }

    public void addEntry(Token key, Token token){
        this.data.put(key, token);
    }

    public Token get(String name) {
        final Token key= data.keySet().stream()
                .filter(token -> token.getText().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);

        return data.get(key);
    }

    public int size() {
        return data.size();
    }
}
