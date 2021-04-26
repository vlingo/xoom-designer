package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;

import java.util.List;

public class AppArguments implements TemplateArguments {

    public final List<AggregateData> aggregates;

    public AppArguments(List<AggregateData> aggregates) {
        this.aggregates = aggregates;
    }

    public String makePlural(String word){
        if (word.endsWith("y")){
            return word.substring(0, word.length()-1) + "ies";
        }else{
            return word + "s";
        }
    }
}
