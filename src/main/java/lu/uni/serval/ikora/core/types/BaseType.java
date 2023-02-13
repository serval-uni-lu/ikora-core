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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class BaseType {
    private final String name;
    protected final boolean isOptional;

    protected BaseType(String name, boolean isOptional) {
        this.name = name;
        this.isOptional = isOptional;
    }

    public String getName() {
        return name;
    }

    public boolean isValid(BaseType other) {
        if (other.getClass() == StringType.class) return true;
        return other.getClass() == this.getClass();
    }

    public boolean isOptional() {
        return isOptional;
    }

    public abstract boolean isSingleValue();

    public abstract Optional<String> asString();

    public abstract <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException;

    public abstract <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException;
}
