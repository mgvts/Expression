package expression;

import java.util.Objects;

public class Const implements Expression {
    private final int value;


    public Const(int c) {
        this.value = c;
    }

    public Integer getValue() {
        return value;
    }


    public int evaluate(int x) {
        return value;
    }


    public int evaluate(int x, int y, int z) {
        return value;
    }


    @Override
    public String toString() {
        return Integer.toString(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Const aConst)) return false;
        return value == aConst.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
