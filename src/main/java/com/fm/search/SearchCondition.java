package com.fm.search;

public class SearchCondition {

    private final SearchField field;

    private final String condition;

    public SearchCondition(SearchField field, String condition){
        this.field = field;
        this.condition = condition;
    }

    public SearchField getField(){ return field;}

    public String getCondition(){return condition;}

    @Override
    public String toString() {
        return "SearchCondition{" +
                "field=" + field +
                ", condition='" + condition + '\'' +
                '}';
    }
}
