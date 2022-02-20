package expression;


import expression.Unary.Lzeros;
import expression.Unary.Negate;
import expression.Unary.Tzeros;
import expression.operations.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

public class ExpressionParser {
    private String exp;
    private final String operators = "+-*/m";
    private final String unar = "-tl";
    private final String variable = "xyz";
    private final String allPosibleChars = "()1234567890+-*/mtlxyz";
    private int pos;
    int howopnen;


    public Expression parse(String expression) {
        /*
        main func to parse
         */
        this.pos = 0;
        this.exp = expression;
        howopnen = 0;
        List<Expression> values = new ArrayList<>();
        List<String> operations = new ArrayList<>();
        while (pos < exp.length()) {
            if (values.size() - operations.size() == 2 || values.size() - operations.size() == -1) {
                throw new IllegalArgumentException("bad with kolvo operations and value");
            }
            if (isDigit(exp.charAt(pos))) {
                values.add(parseConst(false));
                continue;
            }

            if (variable.contains(exp.charAt(pos) + "")) {
                values.add(parseVariable());
                continue;
            }

            if (exp.charAt(pos) == '-') {
                if (values.size() - 1 == operations.size()) {
                    operations.add(exp.charAt(pos) + "");
                    pos++;
                    continue;
                } else {
                    values.add(parseUnar());
                    continue;
                }
            }

            if (unar.contains(exp.charAt(pos) + "")) {
                values.add(parseUnar());
                continue;
            }

            if (operators.contains(exp.charAt(pos) + "")) {
                if (exp.charAt(pos) == 'm') {
                    switch (exp.substring(pos, pos + 3)) {
                        case "min" -> operations.add(exp.substring(pos, pos + 3));
                        case "max" -> operations.add(exp.substring(pos, pos + 3));
                        default -> throw new IllegalArgumentException("bad operation minMax");
                    }
                    if ((isWhitespace(exp.charAt(pos - 1)) || exp.charAt(pos - 1) == ')') &&
                            (isWhitespace(exp.charAt(pos + 3)) || exp.charAt(pos + 3) == '-' || exp.charAt(pos + 3) == '(')) {
                        pos += 3;
                        continue;
                    } else {
                        throw new IllegalArgumentException("bad _min_");
                    }
                } else {
                    operations.add(exp.charAt(pos++) + "");
                    continue;
                }
            }

            if (exp.charAt(pos) == '(') {
                pos++;
                values.add(parseBracket());
                continue;
            }

            if (!(allPosibleChars.contains(exp.charAt(pos) + "") || isWhitespace(exp.charAt(pos)))) {
                throw new IllegalArgumentException("unknown symbol");
            }

            if (exp.charAt(pos) == ')') {

                throw new IllegalArgumentException("bad )");
            }
            pos++;
            skipWhiteChars();
        }
        if (values.size() - operations.size() != 1) {
            throw new IllegalArgumentException("bad with kolvo operations and value");
        }
        // "*" "/"
        List<Object> mulDiv = mergeMulDiv(values, operations);
        values = (List<Expression>) mulDiv.get(0);
        operations = (List<String>) mulDiv.get(1);
        // "+" "-"
        List<Object> addSub = mergeAddSub(values, operations);
        values = (List<Expression>) addSub.get(0);
        operations = (List<String>) addSub.get(1);
        // "min" "max"
        List<Object> minMax = mergeMinMax(values, operations);
        values = (List<Expression>) minMax.get(0);
        operations = (List<String>) minMax.get(1);
        return values.get(0);
    }

    private Expression parseUnar() {
        switch (exp.charAt(pos)) {
            case 'l' -> {
                if (exp.startsWith("l0", pos)) {
                    if (isWhitespace(exp.charAt(pos + 2)) ||
                            exp.charAt(pos + 2) == '-' ||
                            exp.charAt(pos + 2) == '(') {
                        pos += 2;
                        return new Lzeros(parseElement());
                    } else {
                        throw new IllegalArgumentException("bad with \" \" in l0");
                    }
                } else {
                    throw new IllegalArgumentException("bad l0");
                }
            }
            case 't' -> {
                if (exp.startsWith("t0", pos)) {
                    if (isWhitespace(exp.charAt(pos + 2)) ||
                            exp.charAt(pos + 2) == '-' ||
                            exp.charAt(pos + 2) == '(') {
                        pos += 2;
                        return new Tzeros(parseElement());
                    } else {
                        throw new IllegalArgumentException("bad with \" \" in t0");
                    }
                } else {
                    throw new IllegalArgumentException("bad l0");
                }
            }
            case '-' -> {
                int howManyNegative = 0;
                Expression res;
                while (pos < exp.length() && exp.charAt(pos) == '-') {
                    howManyNegative++;
                    pos++;
                    skipWhiteChars();
                }
                skipWhiteChars();
                if (isDigit(exp.charAt(pos))) {
                    howManyNegative--;
                    res = parseConst(true);
                } else {
                    res = parseElement();
                }

                for (int i = 0; i < howManyNegative; i++) {
                    res = new Negate(res);
                }
                return res;
            }
            default -> throw new IllegalArgumentException("bad Unar");
        }
    }

    private Expression parseElement() {
        skipWhiteChars();
        if (isDigit(exp.charAt(pos))) {
            return parseConst(false);
        }

        if (variable.contains(exp.charAt(pos) + "")) {
            return parseVariable();
        }

        if (unar.contains(exp.charAt(pos) + "")) {
            return parseUnar();
        }

        if (exp.charAt(pos) == '(') {
            pos++;
            return parseBracket();
        }
        throw new IllegalArgumentException("bad element");
    }

    private Expression parseConst(Boolean isNegative) {
        StringBuilder num = new StringBuilder();
        while (pos < exp.length() && isDigit(exp.charAt(pos))) {
            num.append(exp.charAt(pos));
            pos++;
        }
        if (isNegative) {
            return new Const(Integer.parseInt("-" + num));
        } else {
            return new Const(Integer.parseInt("" + num));
        }
    }

    private Expression parseVariable() {
        return new Variable(Character.toString(exp.charAt(pos++)));
    }

    private void skipWhiteChars() {
        while (pos < exp.length() && isWhitespace(exp.charAt(pos))) {
            pos++;
        }
    }

    private Expression parseBracket() {
        List<Expression> values1 = new ArrayList<>();
        List<String> operations1 = new ArrayList<>();
        while (true) {
            if (values1.size() - operations1.size() == 2 || values1.size() - operations1.size() == -1) {
                throw new IllegalArgumentException("bad with kolvo operations and value");
            }
            if (isDigit(exp.charAt(pos))) {
                values1.add(parseConst(false));
                continue;
            }

            if (variable.contains(exp.charAt(pos) + "")) {
                values1.add(parseVariable());
                continue;
            }

            if (exp.charAt(pos) == '-') {
                if (values1.size() - 1 == operations1.size()) {
                    operations1.add(exp.charAt(pos) + "");
                    pos++;
                    continue;
                } else {
                    values1.add(parseUnar());
                    continue;
                }
            }

            if (unar.contains(exp.charAt(pos) + "")) {
                values1.add(parseUnar());
                continue;
            }

            if (operators.contains(exp.charAt(pos) + "")) {
                if (exp.charAt(pos) == 'm') {
                    switch (exp.substring(pos, pos + 3)) {
                        case "min" -> operations1.add(exp.substring(pos, pos + 3));
                        case "max" -> operations1.add(exp.substring(pos, pos + 3));
                        default -> throw new IllegalArgumentException("bad operation minMax");
                    }

                    if ((isWhitespace(exp.charAt(pos - 1)) || exp.charAt(pos - 1) == ')') &&
                            (isWhitespace(exp.charAt(pos + 3)) || exp.charAt(pos + 3) == '-' || exp.charAt(pos + 3) == '(')) {
                        pos += 3;
                        continue;
                    } else {
                        throw new IllegalArgumentException("bad ?min?");
                    }
                } else {
                    operations1.add(exp.charAt(pos++) + "");
                    continue;
                }
            }

            if (exp.charAt(pos) == '(') {
                pos++;
                values1.add(parseBracket());
                continue;
            }

            if (exp.charAt(pos) == ')') {
                pos++;
                break;
            }
            if (!(allPosibleChars.contains(exp.charAt(pos) + "") || isWhitespace(exp.charAt(pos)))) {
                throw new IllegalArgumentException("unknown symbol");
            }
            pos++;
            skipWhiteChars();
        }
        if (values1.size() - operations1.size() != 1) {
            throw new IllegalArgumentException("bad with kolvo operations and value");
        }
        // "*" "/"
        List<Object> mulDiv = mergeMulDiv(values1, operations1);
        values1 = (List<Expression>) mulDiv.get(0);
        operations1 = (List<String>) mulDiv.get(1);
        // "+" "-"
        List<Object> addSub = mergeAddSub(values1, operations1);
        values1 = (List<Expression>) addSub.get(0);
        operations1 = (List<String>) addSub.get(1);
        // "min" "max"
        List<Object> minMax = mergeMinMax(values1, operations1);
        values1 = (List<Expression>) minMax.get(0);
        operations1 = (List<String>) minMax.get(1);
        return values1.get(0);
    }

    private List<Object> mergeMulDiv(List<Expression> values, List<String> operations) {
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "*" -> {
                    values.set(i, new Multiply(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "/" -> {
                    values.set(i, new Divide(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
        ArrayList<Object> res = new ArrayList<>();
        res.add(values);
        res.add(operations);
        return res;
    }

    private List<Object> mergeAddSub(List<Expression> values, List<String> operations) {
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "+" -> {
                    values.set(i, new Add(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "-" -> {
                    values.set(i, new Subtract(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
        ArrayList<Object> res = new ArrayList<>();
        res.add(values);
        res.add(operations);
        return res;
    }

    private List<Object> mergeMinMax(List<Expression> values, List<String> operations) {
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "min" -> {
                    values.set(i, new Min(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "max" -> {
                    values.set(i, new Max(values.get(i), values.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
        ArrayList<Object> res = new ArrayList<>();
        res.add(values);
        res.add(operations);
        return res;
    }
}
