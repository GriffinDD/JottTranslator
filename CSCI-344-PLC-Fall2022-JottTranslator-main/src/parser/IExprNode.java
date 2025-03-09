package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott integer expression node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class IExprNode implements JottTree {

    private Token sign1 = null;
    private Token il1 = null;
    private JottTree func1 = null;
    private Token op1 = null;
    private Token sign2 = null;
    private Token il2 = null;
    private JottTree func2 = null;
    private Token op2 = null;
    private JottTree iExpr2 = null;

    /**
     * Constructor that will try to build a integer expression node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative integer expression
     */
    public IExprNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //handles items that can start our expression
        if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == ID_KEYWORD
                && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (tokens.get(1).getTokenType() == L_BRACKET){
                func1 = new FuncCallNode(tokens);
            }
            else {
                //we just have a regular id
                il1 = tokens.remove(0);
            }
        }
        else if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == NUMBER){
            if(!tokens.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid integer with no sign
                il1 = tokens.remove(0);
            }
            else{
                printErrorParser("Expected int got double", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }
        else if(tokens.size() >= 3 && (tokens.get(0).getToken().equals("+") || tokens.get(0).getToken().equals("-"))
                && tokens.get(1).getTokenType() == NUMBER){
            if(!tokens.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid integer with a sign
                sign1 = tokens.remove(0);
                il1 = tokens.remove(0);
            }
            else{
                printErrorParser("Expected int got double", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }
        else{
            if(tokens.get(0).getTokenType() == MATH_OP){
                printErrorParser("unaccompanied -/+ missing term or expr", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
            printErrorParser("Expected i expr start got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
        //if this is the end of our expression we can stop searching here
        if(tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA || tokens.get(0).getTokenType() == REL_OP) {
            return;
        }else if(tokens.get(0).getTokenType() == MATH_OP){
            //we have an op after our first term
            op1 = tokens.remove(0);
        }else {
            String pre = "";
            if(il1 != null){
                pre = "int";
            }else if(func1 != null){
                pre = "func call";
            }
            printErrorParser("Unexpected token - " + generateErrorString(tokens.get(0)) + " after " + pre, tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

        //again check to see if the right half is a valid i_expr
        if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == ID_KEYWORD
                && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")) {
            //if we see an L bracket next, we have a func call
            if (tokens.get(1).getTokenType() == L_BRACKET) {
                func2 = new FuncCallNode(tokens);
            } else {
                //we just have a regular id
                il2 = tokens.remove(0);
            }
        }else if(tokens.size() >= 2 &&  tokens.get(0).getTokenType() == NUMBER){
            if(!tokens.get(0).getToken().contains(Character.toString('.'))){
                //we have a valid integer with no sign
                il2 = tokens.remove(0);
            }else{
                printErrorParser("Expected int got double", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }else if(tokens.size() >= 3 && (tokens.get(0).getToken().equals("+") || tokens.get(0).getToken().equals("-"))
                && tokens.get(1).getTokenType() == NUMBER) {
            if(!tokens.get(1).getToken().contains(Character.toString('.'))){
                //we have a valid integer with a sign
                sign2 = tokens.remove(0);
                il2 = tokens.remove(0);
            }else{
                printErrorParser("Expected int got double", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }else{
            if(tokens.get(0).getTokenType() == MATH_OP){
                printErrorParser("unaccompanied -/+ missing term or expr", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
            printErrorParser("Expected i expr term got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
        //if this is the end of our full expression we can stop here
        if(tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA || tokens.get(0).getTokenType() == REL_OP) {
            return;
        }else if(tokens.get(0).getTokenType() == MATH_OP){
            //then this is the starting i expr of a 2 i expr term
            op2 = tokens.remove(0);
            iExpr2 = new IExprNode(tokens);
        }else {
            String pre = "";
            if(il2 != null){
                pre = "int";
            }else if(func2 != null){
                pre = "func call";
            }
            printErrorParser("Unexpected token - " + generateErrorString(tokens.get(0)) + " after " + pre, tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }

    /**
     * Responsible for parsing a integer expression
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "";
        if(sign1 != null){
            readout += sign1.getToken();
        }
        if(il1 != null){
            readout += il1.getToken();
        }
        if(func1 != null){
            readout += func1.convertToJott();
        }
        if(op1 != null){
            readout += op1.getToken();
        }
        if(sign2 != null){
            readout += sign2.getToken();
        }
        if(il2 != null){
            readout += il2.getToken();
        }
        if(func2 != null){
            readout += func2.convertToJott();
        }
        if(op2 != null){
            readout += op2.getToken() + iExpr2.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
