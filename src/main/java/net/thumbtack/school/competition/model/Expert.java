package net.thumbtack.school.competition.model;

import java.util.Objects;
import java.util.Set;

public class Expert extends Person{

    private Set<Direction> fields;

    public Expert(String firstName, String secondName, Set<Direction> fields, String login, String password) {
        super(firstName, secondName, login, password);
        this.fields = fields;

    }

    public Set<Direction> getFields() {
        return fields;
    }

    public void setFields(Set<Direction> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Expert expert = (Expert) o;
        return Objects.equals(fields, expert.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fields);
    }
}
