package de.fractalsoft.digraph;

import java.util.Objects;

public class DummyEdge {

    private int id;
    private String name;

    public DummyEdge(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyEdge dummyNode = (DummyEdge) o;
        return id == dummyNode.id &&
                Objects.equals(name, dummyNode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
