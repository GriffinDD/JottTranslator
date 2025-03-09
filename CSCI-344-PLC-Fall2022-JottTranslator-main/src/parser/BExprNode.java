package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott boolean expression node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class BExprNode implements JottTree{

    private Boolean singleExpr = false;
    private JottTree expression1 = null;
    private Token b1 = null;
    private Token op1 = null;
    private JottTree expression2 = null;
    private Token b2 = null;
    private Token op2 = null;
    private JottTree bExpr2 = null;

    /**
     * Constructor that will try to build a boolean expression node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative boolean expression
     */
    public BExprNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //make to test array copies to verify if we should consider our first term either a d_expr or an i_expr
        ArrayList<Token> intTester = new ArrayList<Token>();
        ArrayList<Token> dblTester = new ArrayList<Token>();
        ArrayList<Token> funcTester = new ArrayList<Token>();
        for(Token t: tokens){
            intTester.add(t);
            dblTester.add(t);
            funcTester.add(t);
        }
        TestDBI tester = new TestDBI();
        TestDBI tester2 = new TestDBI();
        //we need to see if this is a single id or func, if so then we can end immediately
        //if it is multiple func calls, then we know we have some sort of expr before a rel op
        if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == ID_KEYWORD
                && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            JottTree func1 = null;
            Token id = null;
            //if we see an L bracket next, we have a func call
            if (tokens.get(1).getTokenType() == L_BRACKET) {
                func1 = new FuncCallNode(funcTester);
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
                    expression1 = new FuncCallNode(tokens);
                }else{
                    b1 = tokens.remove(0);
                }

            }

        }
        //if we already know that we are a single id or func call followed by an end expr, we don't need to check anything.
        if(!singleExpr){
            if (tester.testDouble(dblTester)) {
                expression1 = new DExprNode(tokens);
            } else if (tester2.testInt(intTester)) {
                expression1 = new IExprNode(tokens);
            }//if not, then we handle the easy cases like a string expression
            else if (tokens.size() >= 2 && tokens.get(0).getTokenType() == STRING) {
                expression1 = new SExprNode(tokens);
            } else {
                //now we handle boolean expression terms
                if (tokens.size() >= 2 && tokens.get(0).getToken().matches("True|False")) {
                    b1 = tokens.remove(0);
                } else {
                    printErrorParser("Expected b expr term got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
                }
            }
        }

        //if we had just an id, func call, or boolean we can end in one term. Otherwise, we have a different expr type and need a rel op
        if((tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA) && (singleExpr || b1 != null)) {
            return;
        }else if(tokens.get(0).getTokenType() == REL_OP){
            //we have an op after our first term
            op1 = tokens.remove(0);
        }else {
            if(b1 != null){
                printErrorParser("Unexpected token - " + generateErrorString(tokens.get(0)) + " after boolean", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }else{
                printErrorParser("Expected rel op after i/d/s expr got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }

        }

        //do the same set of tests on the right side, but we need to make sure that they match
        ArrayList<Token> intTester2 = new ArrayList<Token>();
        ArrayList<Token> dblTester2 = new ArrayList<Token>();
        for(Token t: tokens){
            intTester2.add(t);
            dblTester2.add(t);
        }
        TestDBI tester3 = new TestDBI();
        TestDBI tester4 = new TestDBI();
        if(tester3.testDouble(dblTester2)){
            expression2 = new DExprNode(tokens);
        }else if(tester4.testInt(intTester2)){
            expression2 = new IExprNode(tokens);
        }//if not, then we handle the easy cases like a string expression
        else if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == STRING){
            expression2 = new SExprNode(tokens);
        }else{
            //now we handle boolean expression terms
            if(tokens.size() >= 2 &&  tokens.get(0).getToken().matches("True|False")) {
                b2 = tokens.remove(0);
            }else{
                printErrorParser("Expected b expr term got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }


        //if this is the end of our full expression we can stop here
        if(tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA) {
            return;
        }else if(tokens.get(0).getTokenType() == REL_OP){
            //then this is the starting b expr of a 2 b expr term
            op2 = tokens.remove(0);
            bExpr2 = new BExprNode(tokens);
        }else {
            String pre = "";
            if(b2 != null){
                pre = "boolean";
            }else if(expression2 != null){
                pre = "expr";
            }
            printErrorParser("Unexpected token - " + generateErrorString(tokens.get(0)) + " after " + pre, tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
    }

    /**
     * Responsible for parsing a boolean expression
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "";
        if(expression1 != null){
            readout += expression1.convertToJott();
        }
        if(b1 != null){
            readout += b1.getToken();
        }

        if(op1 != null){
            readout += op1.getToken();
        }

        if(expression2 != null){
            readout += expression2.convertToJott();
        }
        if(b2 != null){
            readout += b2.getToken();
        }
        if(op2 != null){
            readout += op2.getToken() + bExpr2.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
