package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for holding methods that
 * test the given list of tokens to see if an expression
 * of a valid datatype (boolean, integer, or double) can be created
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class TestDBI {
    private Boolean singleExpr = false;
    private Token sign1 = null;
    private Token dbl1 = null;
    private JottTree func1 = null;
    private Token op1 = null;
    private Token sign2 = null;
    private Token dbl2 = null;
    private JottTree func2 = null;
    private Token op2 = null;
    private JottTree dExpr2 = null;
    private JottTree expression1 = null;
    private Token b1 = null;
    private JottTree expression2 = null;
    private Token b2 = null;
    private JottTree bExpr2 = null;
    private Token il1 = null;
    private Token il2 = null;
    private JottTree iExpr2 = null;

    /**
     * Tests if we can make a boolean expression from the given list of tokens
     *
     * @param bools the ArrayList of Jott tokens to parse
     * @return the boolean indicating whether a boolean expression
     *          can be built from the given tokens
     */
    public boolean testBool(ArrayList<Token> bools) {
        //make to test array copies to verify if we should consider our first term either a d_expr or an i_expr
        ArrayList<Token> intTester = new ArrayList<Token>();
        ArrayList<Token> dblTester = new ArrayList<Token>();
        ArrayList<Token> funcTester = new ArrayList<Token>();
        for(Token t: bools){
            intTester.add(t);
            dblTester.add(t);
            funcTester.add(t);
        }
        TestDBI tester = new TestDBI();
        TestDBI tester2 = new TestDBI();
        //we need to see if this is a single id or func, if so then we can end immediately
        //if it is multiple func calls, then we know we have some sort of expr before a rel op
        if(bools.size() >= 2 &&  bools.get(0).getTokenType() == ID_KEYWORD
                && !bools.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            JottTree func1 = null;
            Token id = null;
            //if we see an L bracket next, we have a func call
            if (bools.get(1).getTokenType() == L_BRACKET) {
                //try to make a func call, if any errors then we can't take this path
                try {
                    func1 = new FuncCallNode(funcTester);
                }catch (ParseSyntaxError p){
                    return false;
                }
            }
            else {
                //we just have a regular id
                id = funcTester.remove(0);
            }
            //we need to check if this is our single term for this b expr
            //if it is, then we were correct to make func call, so we can adjust our token list to match this
            if(funcTester.get(0).getTokenType() == SEMICOLON || funcTester.get(0).getTokenType() == R_BRACKET
                    || funcTester.get(0).getTokenType() == COMMA) {
                singleExpr = true;
                if(func1 != null){
                    //try to make a func call, if any errors then we can't take this path
                    try {
                        expression1 = new FuncCallNode(bools);
                    }catch (ParseSyntaxError p){
                        return false;
                    }
                }else{
                    b1 = bools.remove(0);
                }

            }

        }
        //if we already know that we are a single id or func call followed by an end expr, we don't need to check anything.
        if(!singleExpr){
            if (tester.testDouble(dblTester)) {
                //try to make a d expr, if any errors then we can't take this path
                try {
                    expression1 = new DExprNode(bools);
                }catch (ParseSyntaxError p){
                    return false;
                }
            } else if (tester2.testInt(intTester)) {
                //try to make an i expr, if any errors then we can't take this path
                try {
                    expression1 = new IExprNode(bools);
                }catch (ParseSyntaxError p){
                    return false;
                }
            }//if not, then we handle the easy cases like a string expression
            else if (bools.size() >= 2 && bools.get(0).getTokenType() == STRING) {
                //try to make an s expr, if any errors then we can't take this path
                try {
                    expression1 = new SExprNode(bools);
                }catch (ParseSyntaxError p){
                    return false;
                }
            } else {
                //now we handle boolean expression terms
                if (bools.size() >= 2 && bools.get(0).getToken().matches("True|False")) {
                    b1 = bools.remove(0);
                } else {
                   return false;
                }
            }
        }

        //if we had just an id, func call, or boolean we can end in one term. Otherwise, we have a different expr type and need a rel op
        if((bools.get(0).getTokenType() == SEMICOLON || bools.get(0).getTokenType() == R_BRACKET
                || bools.get(0).getTokenType() == COMMA) && (singleExpr || b1 != null)) {
            return true;
        }else if(bools.get(0).getTokenType() == REL_OP){
            //we have an op after our first term
            op1 = bools.remove(0);
        }else {
            return false;
        }

        //do the same set of tests on the right side
        ArrayList<Token> intTester2 = new ArrayList<Token>();
        ArrayList<Token> dblTester2 = new ArrayList<Token>();
        for(Token t: bools){
            intTester2.add(t);
            dblTester2.add(t);
        }
        TestDBI tester3 = new TestDBI();
        TestDBI tester4 = new TestDBI();
        if(tester3.testDouble(dblTester2)){
            //try to make a d_expr, if any errors then we can't take this path
            try {
                expression2 = new DExprNode(bools);
            }catch (ParseSyntaxError p){
                return false;
            }
        }else if(tester4.testInt(intTester2)){
            //try to make an i_expr, if any errors then we can't take this path
            try {
                expression2 = new IExprNode(bools);
            }catch (ParseSyntaxError p){
                return false;
            }
        }//if not, then we handle the easy cases like a string expression
        else if(bools.size() >= 2 &&  bools.get(0).getTokenType() == STRING){
            //try to make an s_expr, if any errors then we can't take this path
            try {
                expression2 = new SExprNode(bools);
            }catch (ParseSyntaxError p){
                return false;
            }
        }else{
            //now we handle boolean expression terms
            if(bools.size() >= 2 &&  bools.get(0).getToken().matches("True|False")) {
                b2 = bools.remove(0);
            }else{
                return false;
            }
        }


        //if this is the end of our full expression we can stop here
        if(bools.get(0).getTokenType() == SEMICOLON || bools.get(0).getTokenType() == R_BRACKET
                || bools.get(0).getTokenType() == COMMA) {
            return true;
        }else if(bools.get(0).getTokenType() == REL_OP){
            //then this is the starting d expr of a 2 d expr term
            op2 = bools.remove(0);
            //try to make a b_expr, if any errors then we can't take this path
            try {
                bExpr2 = new BExprNode(bools);
            }catch (ParseSyntaxError p){
                return false;
            }
        }else {
            return false;
        }
        return false;
    }

    /**
     * Tests if we can make an integer expression from the given list of tokens
     *
     * @param ints the ArrayList of Jott tokens to parse
     * @return the boolean indicating whether a integer expression
     *          can be built from the given tokens
     */
    public boolean testInt(ArrayList<Token> ints){
        //handles items that can start our expression
        if(ints.size() >= 2 &&  ints.get(0).getTokenType() == ID_KEYWORD
                && !ints.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (ints.get(1).getTokenType() == L_BRACKET) {
                //try to make a func call, if any errors then we can't take this path
                try {
                    func1 = new FuncCallNode(ints);
                }catch (ParseSyntaxError p){
                    return false;
                }
            }
            else {
                //we just have a regular id
                il1 = ints.remove(0);
            }
        }
        else if(ints.size() >= 2 &&  ints.get(0).getTokenType() == NUMBER){
            if(!ints.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid integer with no sign
                il1 = ints.remove(0);
            }
            else{
               return false;
            }
        }
        else if(ints.size() >= 3 && (ints.get(0).getToken().equals("+") || ints.get(0).getToken().equals("-"))
                && ints.get(1).getTokenType() == NUMBER){
            if(!ints.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid integer with a sign
                sign1 = ints.remove(0);
                il1 = ints.remove(0);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
        //if this is the end of our expression we can stop searching here
        if(ints.get(0).getTokenType() == SEMICOLON || ints.get(0).getTokenType() == R_BRACKET
                || ints.get(0).getTokenType() == COMMA || ints.get(0).getTokenType() == REL_OP) {
            return true;
        }else if(ints.get(0).getTokenType() == MATH_OP){
            //we have an op after our first term
            op1 = ints.remove(0);
        }else {
           return false;
        }

        //again check to see if the right half is a valid i_expr
        if(ints.size() >= 2 &&  ints.get(0).getTokenType() == ID_KEYWORD
                && !ints.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (ints.get(1).getTokenType() == L_BRACKET) {
                //try to make a func call, if any errors then we can't take this path
                try {
                    func2 = new FuncCallNode(ints);
                }catch (ParseSyntaxError p){
                    return false;
                }
            } else {
                //we just have a regular id
                il2 = ints.remove(0);
            }
        }else if(ints.size() >= 2 &&  ints.get(0).getTokenType() == NUMBER){
            if(!ints.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid integer with no sign
                il2 = ints.remove(0);
            }else{
                return false;
            }
        }else if(ints.size() >= 3 && (ints.get(0).getToken().equals("+") || ints.get(0).getToken().equals("-"))
                && ints.get(1).getTokenType() == NUMBER) {
            if(!ints.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid integer with a sign
                sign2 = ints.remove(0);
                il2 = ints.remove(0);
            }else{
                return false;
            }
        }else{
            return false;
        }
        //if this is the end of our full expression we can stop here
        if(ints.get(0).getTokenType() == SEMICOLON || ints.get(0).getTokenType() == R_BRACKET
                || ints.get(0).getTokenType() == COMMA || ints.get(0).getTokenType() == REL_OP) {
            return true;
        }else if(ints.get(0).getTokenType() == MATH_OP){
            //then this is the starting i expr of a 2 i expr term
            op2 = ints.remove(0);
            //try to make an i_expr, if any errors then we can't take this path
            try {
                iExpr2 = new IExprNode(ints);
            }catch (ParseSyntaxError p){
                return false;
            }

        }else {
            return false;
        }
        return false;
    }

    /**
     * Tests if we can make a double expression from the given list of tokens
     *
     * @param dbls the ArrayList of Jott tokens to parse
     * @return the boolean indicating whether a double expression
     *          can be built from the given tokens
     */
    public boolean testDouble(ArrayList<Token> dbls){
        //handles items that can start our expression
        if(dbls.size() >= 2 &&  dbls.get(0).getTokenType() == ID_KEYWORD
                && !dbls.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (dbls.get(1).getTokenType() == L_BRACKET) {
                //try to make a function call, if any errors then we can't take this path
                try {
                    func1 = new FuncCallNode(dbls);
                }catch (ParseSyntaxError p){
                    return false;
                }
            }
            else {
                //we just have a regular id
                dbl1 = dbls.remove(0);
            }
        }
        else if(dbls.size() >= 2 &&  dbls.get(0).getTokenType() == NUMBER){
            if(dbls.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid double with no sign
                dbl1 = dbls.remove(0);
            }
            else{
                return false;
            }
        }
        else if(dbls.size() >= 3 && (dbls.get(0).getToken().equals("+") || dbls.get(0).getToken().equals("-"))
                && dbls.get(1).getTokenType() == NUMBER){
            if(dbls.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid double with a sign
                sign1 = dbls.remove(0);
                dbl1 = dbls.remove(0);
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
        //if this is the end of our expression we can stop searching here
        if(dbls.get(0).getTokenType() == SEMICOLON || dbls.get(0).getTokenType() == R_BRACKET
                || dbls.get(0).getTokenType() == COMMA || dbls.get(0).getTokenType() == REL_OP) {
            return true;
        }else if(dbls.get(0).getTokenType() == MATH_OP){
            //we have an op after our first term
            op1 = dbls.remove(0);
        }else {
            return false;
        }

        //again check to see if the right half is a valid d_expr
        if(dbls.size() >= 2 &&  dbls.get(0).getTokenType() == ID_KEYWORD
                && !dbls.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (dbls.get(1).getTokenType() == L_BRACKET) {
                //try to make a function call, if any errors then we can't take this path
                try {
                    func2 = new FuncCallNode(dbls);
                }catch (ParseSyntaxError p){
                    return false;
                }
            } else {
                //we just have a regular id
                dbl2 = dbls.remove(0);
            }
        }else if(dbls.size() >= 2 &&  dbls.get(0).getTokenType() == NUMBER){
            if(dbls.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid double with no sign
                dbl2 = dbls.remove(0);
            }else{
                return false;
            }
        }else if(dbls.size() >= 3 && (dbls.get(0).getToken().equals("+") || dbls.get(0).getToken().equals("-"))
                && dbls.get(1).getTokenType() == NUMBER) {
            if(dbls.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid double with a sign
                sign2 = dbls.remove(0);
                dbl2 = dbls.remove(0);
            }else{
                return false;
            }
        }else{
            return false;
        }
        //if this is the end of our full expression we can stop here
        if(dbls.get(0).getTokenType() == SEMICOLON || dbls.get(0).getTokenType() == R_BRACKET
                || dbls.get(0).getTokenType() == COMMA || dbls.get(0).getTokenType() == REL_OP) {
            return true;
        }else if(dbls.get(0).getTokenType() == MATH_OP){
            //then this is the starting d expr of a 2 d expr term
            op2 = dbls.remove(0);
            //try to make a d_expr, if any errors then we can't take this path
            try {
                dExpr2 = new DExprNode(dbls);
            }catch (ParseSyntaxError p){
                return false;
            }
        }else {
            return false;
        }
        return false;
    }
}
