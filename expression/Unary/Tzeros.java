package expression.Unary;


import expression.Expression;

public class Tzeros extends AbstractUnary {

    public Tzeros(Expression c1) {
        super(c1, "t0");
    }

    int action(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}