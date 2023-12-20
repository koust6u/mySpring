package com.fm.unit;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Player {
    private int id;
    private String name;
    private int height;
    private String nationality;
    private String club;
    private int overall;
    private List<String> positions;

    public Player() {}
    public Player(int id, String name, int height, String nationality, String club, int overall, List<String> positions) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.nationality = nationality;
        this.club = club;
        this.overall = overall;
        this.positions = positions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public String getNationality() {
        return nationality;
    }

    public String getClub() {
        return club;
    }

    public int getOverall() {
        return overall;
    }

    public List<String> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Object[] toArray() {
        Field[] fields = Player.class.getDeclaredFields();
        fields = Arrays.stream(fields).filter(f -> f.getModifiers() == 18).toArray(Field[]::new);
        Object[] result = new Object[fields.length];
        try {
            for (int i = 0; i < fields.length; i++)
                if (fields[i].getModifiers() == 18)
                    result[i] = fields[i].get(this);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return result;
    }

}
