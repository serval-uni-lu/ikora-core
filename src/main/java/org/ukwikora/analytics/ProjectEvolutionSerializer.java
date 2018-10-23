package org.ukwikora.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ukwikora.model.Project;
import org.ukwikora.model.Sequence;
import org.ukwikora.model.TestCase;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.*;

public class ProjectEvolutionSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project: results.getProjects()){
            ProjectStatistics statistics = new ProjectStatistics(project);

            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("commit ID", project.getCommitId());
            jsonGenerator.writeStringField("time", project.getDateTime().toString());

            jsonGenerator.writeNumberField("number files", statistics.getNumberFiles());
            jsonGenerator.writeNumberField("number keywords", statistics.getNumberKeywords(UserKeyword.class));
            jsonGenerator.writeNumberField("number test cases", statistics.getNumberKeywords(TestCase.class));
            jsonGenerator.writeNumberField("documentation length", statistics.getDocumentationLength());
            jsonGenerator.writeNumberField("lines of code", statistics.getLoc());
            jsonGenerator.writeNumberField("sequence steps number", getSequenceStepNumber(results, project));

            jsonGenerator.writeNumberField("changes sequence steps", getSequenceDifferences(results, project));
            writeDifferences(results, jsonGenerator, project);

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private int getSequenceStepNumber(EvolutionResults results, Project project) {
        int size = 0;

        for(Sequence sequence: results.getSequence(project)){
            size += sequence.size();
        }

        return size;
    }

    private int getSequenceDifferences(EvolutionResults results, Project project) {
        int sequenceDifferences = 0;

        for(Difference difference: results.getSequenceDifferences(project)){
            if(difference == null){
                continue;
            }

            for (Action action: difference.getActions()){
                switch (action.getType()){
                    case ADD_SEQUENCE:
                    case REMOVE_SEQUENCE:
                        sequenceDifferences += ((Sequence)difference.getValue()).size();
                        break;
                    default:
                        sequenceDifferences++;
                }
            }
        }

        return sequenceDifferences;
    }

    private void writeDifferences(EvolutionResults results, JsonGenerator jsonGenerator, Project project) throws IOException {
        Map<Action.Type, Integer> changes = new HashMap<>();

        for(Action.Type changeType: Action.Type.values()){
            changes.put(changeType, 0);
        }

        for(Difference difference: results.getDifferences(project)){
            for(Action action: difference.getActions()){
                changes.put(action.getType(), changes.get(action.getType()) + 1);
            }
        }

        jsonGenerator.writeObjectFieldStart("changes");

        for(Map.Entry<Action.Type, Integer> change: changes.entrySet()){
            jsonGenerator.writeNumberField(DifferencesJson.cleanName(change.getKey().name()), change.getValue());
        }

        jsonGenerator.writeEndObject();
    }

    public static class DifferentiableStringList implements List<ProjectStatistics.DifferentiableString> {
        private List<ProjectStatistics.DifferentiableString> stringList;

        public DifferentiableStringList(){
            stringList = new ArrayList<>();
        }

        @Override
        public int size() {
            return stringList.size();
        }

        @Override
        public boolean isEmpty() {
            return stringList.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return stringList.contains(o);
        }

        @Override
        public Iterator<ProjectStatistics.DifferentiableString> iterator() {
            return stringList.iterator();
        }

        @Override
        public Object[] toArray() {
            return stringList.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return stringList.toArray(a);
        }

        @Override
        public boolean add(ProjectStatistics.DifferentiableString differentiable) {
            return stringList.add(differentiable);
        }

        public boolean add(String string){
            return stringList.add(new ProjectStatistics.DifferentiableString(string));
        }

        @Override
        public boolean remove(Object o) {
            return stringList.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return stringList.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends ProjectStatistics.DifferentiableString> c) {
            return stringList.addAll(c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends ProjectStatistics.DifferentiableString> c) {
            return stringList.addAll(index, c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return stringList.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return stringList.retainAll(c);
        }

        @Override
        public void clear() {
            stringList.clear();
        }

        @Override
        public ProjectStatistics.DifferentiableString get(int index) {
            return stringList.get(index);
        }

        @Override
        public ProjectStatistics.DifferentiableString set(int index, ProjectStatistics.DifferentiableString element) {
            return stringList.set(index, element);
        }

        @Override
        public void add(int index, ProjectStatistics.DifferentiableString element) {
            stringList.add(index, element);
        }

        @Override
        public ProjectStatistics.DifferentiableString remove(int index) {
            return stringList.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            return stringList.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return stringList.lastIndexOf(o);
        }

        @Override
        public ListIterator<ProjectStatistics.DifferentiableString> listIterator() {
            return stringList.listIterator();
        }

        @Override
        public ListIterator<ProjectStatistics.DifferentiableString> listIterator(int index) {
            return stringList.listIterator(index);
        }

        @Override
        public List<ProjectStatistics.DifferentiableString> subList(int fromIndex, int toIndex) {
            return stringList.subList(fromIndex, toIndex);
        }

        public static DifferentiableStringList fromTextBlock(String block){
            DifferentiableStringList list = new DifferentiableStringList();

            for(String line: block.split(StringUtils.lineBreak)){
                list.add(line);
            }

            return list;
        }
    }
}
