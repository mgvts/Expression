package expression.operations;


import expression.Expression;

public class Max extends AbstractOperations {
    public Max(Expression c1, Expression c2) {
        super(c1, c2, "max");
    }

    public int action(int x, int y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }
}