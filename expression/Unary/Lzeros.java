package expression.Unary;


import expression.Expression;

public class Lzeros extends AbstractUnary {

    public Lzeros(Expression c1) {
        super(c1, "l0");
    }

    int action(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}