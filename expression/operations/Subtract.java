package expression.operations;


import expression.Expression;

public class Subtract extends AbstractOperations {
    public Subtract(Expression op1, Expression op2) {
        super(op1, op2, "-");
    }

    @Override
    public int action(int x, int y) {
        if (x <= 0 && y > 0 && x - y > 0) {
            throw new ArithmeticException("overflow");
        } else if (x >= 0 && y <= 0 && x - y < 0) {
            throw new ArithmeticException("overflow");
        }
        return x - y;
    }
}
