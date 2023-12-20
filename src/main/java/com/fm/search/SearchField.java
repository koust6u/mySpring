package com.fm.search;

public enum SearchField {

    NAME("name"),
    CLUB("club"),
    POSITION("position"),
    NATIONALITY("nationality"),
    INVALID("invalid");

    private String str;

    SearchField(String str){this.str = str;}


    public static SearchField fromString(String str){
        for (SearchField field: values()){
            if (field.toString().equalsIgnoreCase(str)){
                return field;
            }
        }
        return INVALID;
    }


    @Override
    public String toString() {
        return this.str;
    }
}
