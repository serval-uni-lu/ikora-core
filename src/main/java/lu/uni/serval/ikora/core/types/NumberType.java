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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NumberType extends BaseType {
    private Number value = null;
    private Number defaultValue = null;
    private boolean isValueSet = false;

    public NumberType(String name){
        super(name, false);
    }

    public NumberType(String name, Number defaultValue){
        super(name, true);
        this.defaultValue = defaultValue;
    }

    public NumberType(String name, String defaultValue) throws InvalidTypeException {
        super(name, true);
        this.defaultValue = toNumber(defaultValue);
    }

    public Optional<Number> getValue() {
        if(isValueSet) return Optional.of(value);
        if(isOptional) return Optional.of(defaultValue);
        return Optional.empty();
    }

    public void setValue(Number value) {
        this.isValueSet = true;
        this.value = value;
    }

    public void setValue(String value) throws InvalidTypeException {
        this.isValueSet = true;
        this.value = toNumber(value);
    }

    private static Number toNumber(String value) throws InvalidTypeException {
        try{
            if (value == null || value.trim().equalsIgnoreCase("None")){
                return null;
            }

            return NumberFormat.getInstance().parse(value);
        } catch (ParseException e) {
            throw new InvalidTypeException(String.format("%s cannot be converted to a Number", value));
        }
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        throw new NotImplementedException();
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        throw new NotImplementedException();
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }
}
