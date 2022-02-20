package expression.operations;


import expression.Expression;

public class Add extends AbstractOperations {
    public Add(Expression c1, Expression c2) {
        super(c1, c2, "+");
    }

    @Override
    public int action(int x, int y) {
        if (x > 0 && y > 0 && x + y < 0) {
            throw new ArithmeticException("overflow");
        } else if (x < 0 && y < 0 && x + y >= 0) {
            throw new ArithmeticException("overflow");
        }
        return x + y;
    }
}
