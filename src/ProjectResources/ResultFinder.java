package ProjectResources;

import java.util.Stack;

// ResultFinder
// Programmer: Emerson Moniz
// Last Modified: 10/7/16

/**
 * To find the result of an equation
 * @author Emerson
 */
public class ResultFinder {
    private String equation;
    // Create operandStack to store operands
    private Stack<Integer> operandStack = new Stack<>();
    // Create operatorStack to store operators
    private Stack<Character> operatorStack = new Stack<>();
    private Integer result = null;
    private static boolean isNotEvenDivision;

    /**
     * Constructor
     * @param equation
     */
    public ResultFinder(String equation) {
        isNotEvenDivision = false;
        this.equation = equation;
        equation = insertBlanks(equation);  // Insert blanks around (, ), +, -, /, and *
        String[] tokens = equation.split(" ");  // Extract operands and operators

        // Phase 1: Scan tokens
        for (String token: tokens) {
            if (token.length() == 0) // Blank space
                continue; // Back to the while loop to extract the next token
            else if (token.charAt(0) == '+' || (token.charAt(0) == '-' && token.length() == 1)) {
                // Process all +, -, *, / in the top of the operator stack
                while (!operatorStack.isEmpty() &&
                        (operatorStack.peek() == '+' ||
                                operatorStack.peek() == '-' ||
                                operatorStack.peek() == '*' ||
                                operatorStack.peek() == '/')) {
                    processAnOperator(operandStack, operatorStack);
                }

                // Push the + or - operator into the operator stack
                operatorStack.push(token.charAt(0));
            }
            else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
                // Process all *, / in the top of the operator stack
                while (!operatorStack.isEmpty() &&
                        (operatorStack.peek() == '*' ||
                                operatorStack.peek() == '/')) {
                    processAnOperator(operandStack, operatorStack);
                }

                // Push the * or / operator into the operator stack
                operatorStack.push(token.charAt(0));
            }
            else if (token.trim().charAt(0) == '(') {
                operatorStack.push('('); // Push '(' to stack
            }
            else if (token.trim().charAt(0) == ')') {
                // Process all the operators in the stack until seeing '('
                while (operatorStack.peek() != '(') {
                    processAnOperator(operandStack, operatorStack);
                }

                operatorStack.pop(); // Pop the '(' symbol from the stack
            }
            else { // An operand scanned
                // Push an operand to the stack
                operandStack.push(new Integer(token));
            }
        }

        // Phase 2: process all the remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            processAnOperator(operandStack, operatorStack);
        }

        result = operandStack.pop();
    }

    /**
     * To validate if the numerator is divisible by the denominator
     * @return
     */
    public static boolean isNotEvenDivision() {
        return isNotEvenDivision;
    }

    /**
     * To get the result
     * @return
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Process one operator: Take an operator from operatorStack and
     * apply it on the operands in the operandStack
     * @param operandStack
     * @param operatorStack
     */
    private static void processAnOperator(
            Stack<Integer> operandStack, Stack<Character> operatorStack) {
        char op = operatorStack.pop();
        int op1 = operandStack.pop();
        int op2 = operandStack.pop();
        if (op == '+')
            operandStack.push(op2 + op1);
        else if (op == '-')
            operandStack.push(op2 - op1);
        else if (op == '*')
            operandStack.push(op2 * op1);
        else if (op == '/') {
            if (op2 % op1 != 0 || op1 == 0) {
                isNotEvenDivision = true;
            }
            operandStack.push(op2 / op1);
        }
    }

    /**
     * To insert blanks along the operators
     * @param eq
     * @return
     */
    private static String insertBlanks(String eq) {
        String result = "";
        Character previous = null;

        for (int i = 0; i < eq.length(); i++) {
            if (eq.charAt(i) == '(' || eq.charAt(i) == ')' ||
                    eq.charAt(i) == '+' || eq.charAt(i) == '*' || eq.charAt(i) == '/') {
                result += " " + eq.charAt(i) + " ";
                previous = eq.charAt(i);
            }
            else if (eq.charAt(i) == '-') {
                if (previous == null || previous == '+' || previous == '-' || previous == '*' ||
                        previous == '/' || previous == '(') {
                    result += eq.charAt(i);
                }
                else {
                    result += " " + eq.charAt(i) + " ";
                }
                previous = eq.charAt(i);
            }
            else if (Character.isDigit(eq.charAt(i))) {
                result += eq.charAt(i);
                previous = eq.charAt(i);
            }
        }
        return result;
    }
}
