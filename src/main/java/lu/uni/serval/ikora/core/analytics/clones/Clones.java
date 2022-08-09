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
package lu.uni.serval.ikora.core.analytics.clones;

import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.*;
import java.util.stream.Collectors;

public class Clones<T extends SourceNode> {
    public enum Type{
        TYPE_1,
        TYPE_2,
        TYPE_3,
        TYPE_4,
        NONE
    }

    private final Map<Clones.Type, Map<Integer, CloneCluster<T>>> cloneMap;

    public Clones(){
        this.cloneMap = new EnumMap<>(Type.class);

        this.cloneMap.put(Type.TYPE_1, new HashMap<>());
        this.cloneMap.put(Type.TYPE_2, new HashMap<>());
        this.cloneMap.put(Type.TYPE_3, new HashMap<>());
        this.cloneMap.put(Type.TYPE_4, new HashMap<>());
    }

    public void update(T t1, T t2, int hash, Clones.Type cloneType){
        if(Clones.Type.NONE == cloneType){
            return;
        }

        Map<Integer, CloneCluster<T>> clones = this.cloneMap.get(cloneType);
        CloneCluster<T> cluster = clones.getOrDefault(hash, new CloneCluster<>(cloneType));

        cluster.add(t1);
        cluster.add(t2);

        clones.put(hash, cluster);
    }

    public Set<CloneCluster<T>> getClusters(){
        return this.cloneMap.values().stream()
                .flatMap(c -> c.values().stream())
                .collect(Collectors.toSet());
    }

    public Set<T> getClones(Clones.Type type){
        return cloneMap.get(type).values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Clones.Type getCloneType(T t) {
        for(Map.Entry<Type, Map<Integer, CloneCluster<T>>> entry: cloneMap.entrySet()){
            if(contains(t, entry.getValue())){
                return entry.getKey();
            }
        }

        return Type.NONE;
    }

    public int size(Clones.Type type) {
        return (int)cloneMap.get(type).values().stream()
                .mapToLong(Collection::size)
                .sum();
    }

    private boolean contains(T t, Map<Integer, CloneCluster<T>> cloneByType){
        for(CloneCluster<T> clones: cloneByType.values()){
            if(clones.contains(t)){
                return true;
            }
        }

        return false;
    }
}
