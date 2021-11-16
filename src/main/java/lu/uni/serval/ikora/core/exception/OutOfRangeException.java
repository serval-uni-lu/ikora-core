package lu.uni.serval.ikora.core.exception;

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

public class OutOfRangeException extends RuntimeException {
    private final int index;
    private final int lower;
    private final int upper;

    public OutOfRangeException(int index, int lower, int upper) {
        super(String.format("Index %d is out of bound: [%d,%d]", index, lower, upper));

        this.index = index;
        this.lower = lower;
        this.upper = upper;
    }

    public int getIndex() {
        return index;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}
