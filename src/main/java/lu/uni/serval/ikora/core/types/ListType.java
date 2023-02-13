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
package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class ListType extends BaseType {
    private List<String> defaultValue = null;
    private List<String> value = null;

    public ListType(String name) {
        super(name, false);
    }

    public ListType(String name, List<String> defaultValue){
        super(name, true);
        this.defaultValue = defaultValue;
    }

    public Optional<List<String>> getValue() {
        if (value == null){
            return Optional.of(defaultValue);
        }

        return Optional.of(value);
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public boolean isSingleValue() {
        return false;
    }

    @Override
    public Optional<String> asString() {
        return Optional.empty();
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        throw new NotImplementedException();
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        if(List.class.isAssignableFrom(container)){
            final List<T> ts = new ArrayList<>();

            for(BaseType baseType: from){
                ts.add(baseType.convert(Collections.singletonList(baseType), to));
            }

            return (C)ts;
        }

        throw new InvalidTypeException("Cannot convert " + container.getName() + " using list convertor.");
    }
}
