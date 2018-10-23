package org.ukwikora.report;

import java.util.List;

public class Utils {
    public static <T> int getElementPosition(List<T> list, T element, boolean ignoreNull){
        int ignored = 0;

        for(int i = 0; i < list.size(); ++i){
            T child = list.get(i);

            if(!ignoreNull && child == null){
                ++ignored;
            }

            if(child == element){
                return i - ignored;
            }
        }

        return -1;
    }
}
