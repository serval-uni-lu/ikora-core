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
package lu.uni.serval.ikora.core.analytics.resolver;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public class CallResolver {
    private CallResolver() {}

    public static void resolve(StaticScope staticScope, KeywordCall call, ErrorManager errorManager){
        final Set<? super Keyword> keywords = staticScope.findKeywords(call);

        for(Object keyword: keywords) {
            call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
        }

        if(keywords.isEmpty()){
            errorManager.registerSymbolError(
                    call.getSource(),
                    ErrorMessages.FOUND_NO_MATCH,
                    Range.fromTokens(call.getTokens())
            );
        }

        ArgumentResolver.resolve(staticScope, call, errorManager);
    }
}
