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
package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.ScopeNode;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.Optional;

public class Ast {
    private Ast() {}

    public static <T> Optional<T> getParentByType(SourceNode node, Class<T> parentType){
        T parent = null;
        SourceNode current = node.getAstParent();

        while(current != null){
            if(parentType.isAssignableFrom(current.getClass())){
                parent = (T) current;
                break;
            }

            current = current.getAstParent();
        }

        return Optional.ofNullable(parent);
    }

    public static <T> Optional<T> getParentByType(ScopeNode node, Class<T> parentType){
        if(SourceNode.class.isAssignableFrom(node.getClass())){
            return getParentByType((SourceNode)node, parentType);
        }

        return Optional.empty();
    }


}
