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

public class BooleanType extends BaseType {
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    private Boolean value = null;
    private Boolean defaultValue = null;

    public BooleanType(String name){
        super(name, false);
    }

    public BooleanType(String name, boolean defaultValue){
        super(name, true);
        this.defaultValue = defaultValue;
    }

    public BooleanType(String name, String defaultValue) throws InvalidTypeException {
        super(name, true);
        this.defaultValue = toBoolean(defaultValue);
    }

    public Optional<Boolean> getValue() {
        if (value == null){
            return Optional.of(defaultValue);
        }

        return Optional.of(value);
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public void setValue(String value) throws InvalidTypeException {
        this.value = toBoolean(value);
    }

    private static Boolean toBoolean(String value) throws InvalidTypeException {
        if(value.trim().equalsIgnoreCase(TRUE)) return true;
        if(value.trim().equalsIgnoreCase(FALSE)) return false;
        throw new InvalidTypeException(String.format("%s cannot be converted to a Boolean", value));
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        return Optional.empty();
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        if(from.size() != 1){
            final String input = String.join(",", from.stream()
                    .map(BaseType::asString)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList()
            );
            throw new InvalidTypeException(String.format("Cannot convert a List to a Boolean: [%s]", input));
        }

        if(to != Boolean.class && to != String.class){
            throw new InvalidTypeException(String.format("Boolean type is used to generate boolean values but got: %s", to.getCanonicalName()));
        }

        if(from.get(0) instanceof BooleanType booleanType){
            final Optional<Boolean> booleanValue = booleanType.getValue();
            if(booleanValue.isEmpty()){
                throw new InvalidTypeException("Missing value");
            }
            return (T)booleanValue.get();
        }

        final Optional<String> stringValue = from.get(0).asString();
        if(stringValue.isEmpty()){
            throw new InvalidTypeException("Missing value");
        }
        return (T)toBoolean(stringValue.get());
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }
}
