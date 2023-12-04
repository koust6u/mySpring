package edu.pnu.myspring.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class MyJsonParser {

    public static Map<String, Object> parse(String target){
        target = target.replaceAll("\n", "");
        target = target.replaceAll(" ", "");
        target = target.substring(1);
        target = target.substring(0,target.length()-1);

        return Arrays.stream(target.split(","))
                .collect(Collectors.toMap(e -> e.split(":")[0]
                                .replaceAll("\"", "")
                        ,e -> e.split(":")[1]
                                .replaceAll("\"","")));

    }


}
