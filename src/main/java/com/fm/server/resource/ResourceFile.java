package com.fm.server.resource;

import java.util.ArrayList;
import java.util.List;

public class ResourceFile {
    private static final String resourceFile = "players_20_short.csv";
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int HEIGHT = 2;
    private static final int NATIONALITY = 3;
    private static final int CLUB = 4;
    private static final int OVERALL = 5;
    private static final int POSITION = 6;

    public static String getResourceFileName() {
//        return Thread.currentThread().getContextClassLoader().getResource(resourceFile).getFile();
        return resourceFile;
    }

    public static int getOverall(String[] fields) {
        return Integer.parseInt(fields[OVERALL]);
    }

    public static String getClub(String[] fields) {
        return fields[CLUB];
    }

    public static String getNationality(String[] fields) {
        return fields[NATIONALITY];
    }

    public static int getHeight(String[] fields) {
        return Integer.parseInt(fields[HEIGHT]);
    }

    public static String getName(String[] fields) {
        return fields[NAME];
    }

    public static int getId(String[] fields) {
        return Integer.parseInt(fields[ID]);
    }

    public static List<String> getPosition(String[] fields) {
        List<String> positions = new ArrayList<>();
        if (fields[POSITION].startsWith("\"")) {
            for (int i = POSITION; i < fields.length; i++)
                positions.add(fields[i].replace("\"", "").trim());
        } else {
            positions.add(fields[POSITION]);
        }
        return positions;
    }


}
