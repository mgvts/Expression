package expression.Unary;


import expression.Expression;

import java.util.Objects;

public abstract class AbstractUnary implements Expression {
    private final String expression;
    protected Expression c1;

    abstract int action(int x);


    public AbstractUnary(Expression c1, String symbol) {
        this.c1 = c1;
        expression = symbol + "(" + c1.toString() + ")";
    }


    public int evaluate(int x, int y, int z) {
        return action(c1.evaluate(x, y, z));
    }


    @Override
    public String toString() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUnary that)) return false;
        return c1.equals(that.c1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c1);
    }
}
