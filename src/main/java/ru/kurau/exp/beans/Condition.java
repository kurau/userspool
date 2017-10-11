package ru.kurau.exp.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kurau (Yuri Kalinin)
 */
@Getter
@Setter
@Accessors(chain = true)
public class Condition {

    private List<String> conditions = new ArrayList<>();
    private String owner = "";


    public Condition add(String item) {
        conditions.add(item);
        return this;
    }

    public boolean contains(Condition condition) {
        return conditions.containsAll(condition.getConditions());

    }

    public static Condition condition() {
        return new Condition();
    }

}
