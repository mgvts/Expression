package expression.operations;


import expression.Expression;

public class Min extends AbstractOperations {
    public Min(Expression c1, Expression c2) {
        super(c1, c2, "min");
    }

    public int action(int x, int y) {
        if (x >= y) {
            return y;
        } else {
            return x;
        }
    }
}