package net.thumbtack.school.competition.model;

import java.util.*;

public class Bid {

    private String name;
    private String description;
    private Set<Direction> fields;
    private long price;
    private String id;
    private User owner;
    private Map<String, Byte> grades = new HashMap<>();
    private float average;

    public Bid(String name, String description, Set<Direction> fields, long price) {
        this.name = name;
        this.description = description;
        this.fields = fields;
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Direction> getFields() {
        return fields;
    }

    public void setFields(Set<Direction> fields) {
        this.fields = fields;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Map<String, Byte> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Byte> grades) {
        this.grades = grades;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public void addGrade(Expert expert, byte grade) {
        this.grades.put(expert.getLogin(), grade);
    }

    public void removeGrade(Expert expert) {
        this.grades.remove(expert.getLogin());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return price == bid.price &&
                Objects.equals(name, bid.name) &&
                Objects.equals(description, bid.description) &&
                Objects.equals(fields, bid.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, fields, price, id);
    }

}
