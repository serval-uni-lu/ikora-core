package lu.uni.serval.ikora.core.utils;

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

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.Token;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Cfg {
    private Cfg() {}

    public static boolean isCalledBy(SourceNode node, KeywordDefinition caller){
        if(node == caller){
            return true;
        }

        final Set<KeywordDefinition> dependencies = new HashSet<>();
        final KeywordDefinition parent = getParentKeywordDefinition(node);

        if(parent != null){
            dependencies.addAll(KeywordStatistics.getDependencies(parent));
        }
        else{
            dependencies.addAll(KeywordStatistics.getDependencies(node));
        }

        return dependencies.contains(caller);
    }

    public static Optional<KeywordDefinition> getCallerByName(SourceNode node, Token name){
        KeywordDefinition parent = getParentKeywordDefinition(node);

        if(parent == null){
            return Optional.empty();
        }

        return KeywordStatistics.getDependencies(parent).stream()
                .filter(k -> k.matches(name))
                .findFirst();
    }

    private static KeywordDefinition getParentKeywordDefinition(SourceNode node) {
        KeywordDefinition parent = null;

        if (KeywordDefinition.class.isAssignableFrom(node.getClass())) {
            parent = (KeywordDefinition) node;
        } else {
            final Optional<KeywordDefinition> optionalParent = Ast.getParentByType(node, KeywordDefinition.class);
            if (optionalParent.isPresent()) {
                parent = optionalParent.get();
            }
        }
        return parent;
    }

}
