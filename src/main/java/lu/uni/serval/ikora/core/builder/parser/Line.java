package lu.uni.serval.ikora.core.builder.parser;

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

public class Line {
    private String text;
    private int number;
    private boolean isComment;
    private boolean isEmpty;

    public Line(String text, int number, boolean isComment, boolean isEmpty) {
        this.text = text;
        this.number = number;
        this.isComment = isComment;
        this.isEmpty = isEmpty;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public boolean isCode() {
        return !ignore();
    }

    public boolean isValid() {
        return text != null;
    }

    public boolean isComment() {
        return isComment;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean ignore() {
        return isComment || isEmpty;
    }


}
