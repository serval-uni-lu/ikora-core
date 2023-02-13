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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StringType extends BaseType {
    private String value = null;
    private String defaultValue = null;

    public StringType(String name) {
        super(name, false);
    }

    public StringType(String name, String defaultValue) {
        super(name, true);
        this.defaultValue = defaultValue;
    }

    public Optional<String> getValue() {
        if (value == null){
            return Optional.ofNullable(defaultValue);
        }

        return Optional.of(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        return getValue();
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        if(to != String.class){
            throw new InvalidTypeException(String.format("String type is used to generate strings but got: %s", to.getCanonicalName()));
        }

        return (T)String.join(" ", from.stream()
                .map(BaseType::asString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet())
        );
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isValid(BaseType other){
        return true;
    }
}
