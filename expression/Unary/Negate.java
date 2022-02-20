package expression.Unary;


import expression.Expression;

public class Negate extends AbstractUnary {
    public Negate(Expression c1) {
        super(c1, "-");
    }

    @Override
    public int action(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -x;
    }
}
