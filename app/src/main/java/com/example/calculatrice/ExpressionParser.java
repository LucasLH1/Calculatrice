package com.example.calculatrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/");
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getOperatorValue(String op) {
        switch (op) {
            case "+": case "-":
                return 1;
            case "*": case "/":
                return 2;
            default:
                return 0;
        }
    }

    public List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (int i=0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') { //si on tombe sur un chiffre ou un point alors on construit un nbr
                numberBuffer.append(c);
            } else { //si le nombre est complet on ajoute le token
                if (numberBuffer.length() > 0) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }

                if (c == '+' || c == '-' || c == '/' || c == '*') {
                    tokens.add(String.valueOf(c));
                }

            }
        }
        if (numberBuffer.length() > 0) {
            tokens.add(numberBuffer.toString());
        }

        return tokens;
    }

    public List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                // si nbr, on ajoute à la sortie
                output.add(token);

            } else if (isOperator(token)) {
                while (!operators.isEmpty() &&
                        isOperator(operators.peek()) &&
                        getOperatorValue(operators.peek()) >= getOperatorValue(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);

            }
        }

        // terminé, on vide le reste de la pile
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    public double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) throw new IllegalArgumentException("Expression invalide");

                double b = stack.pop();
                double a = stack.pop();
                double result = 0;

                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        if (b == 0) throw new ArithmeticException("Division par zéro");
                        result = a / b;
                        break;
                }

                stack.push(result);
            }
        }

        if (stack.size() != 1) throw new IllegalStateException("Erreur d’évaluation");

        return stack.pop();
    }
}
