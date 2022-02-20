package expression.operations;


import expression.Expression;

public class Multiply extends AbstractOperations {
    public Multiply(Expression op1, Expression op2) {
        super(op1, op2, "*");
    }

    @Override
    public int action(int x, int y) {
        int res = x * y;

        if (x > 0 && y > 0 && x * y <= 0) {
            throw new ArithmeticException("overflow");
        } else if (x < 0 && y < 0 && x * y <= 0) {
            throw new ArithmeticException("overflow");
        } else if (x != 0 && y != 0 && !(res / x == y || res / y == x)) {
            throw new ArithmeticException("overflow");
        }

        return x * y;
    }
}
