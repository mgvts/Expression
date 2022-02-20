import expression.*;
import expression.operations.*;
import expression.Unary.*;


public class Main {
    public static void main(String[] args) {
        ExpressionParser p = new ExpressionParser();

        System.out.println(p.parse("x + \t z/1596703801 * 559656989 \r\n- \nl0(l0(-1548477713)\s)"));
        System.out.println(p.parse("- - -- ----\t\n\t\t--- ((((((--2))))))"));

        System.out.println(p.parse("x+x").evaluate(2, 0, 0));

        Expression exp  = new Add(new Const(100), new Variable("y"));
        System.out.println(exp.evaluate(0, 2, 2));
        // and a lot of more situations try some :)
        try{
//            System.out.println(p.parse("2/0"));
//            System.out.println(p.parse("2/x").evaluate(0, 0, 0));
            System.out.println(new Divide(new Const(Integer.MIN_VALUE), new Const(-1)));
            System.out.println(new Divide(new Const(Integer.MIN_VALUE), new Const(-1)).evaluate(0, 0, 0));
        }catch (Exception  e){
            e.printStackTrace();
        }
    }
}
