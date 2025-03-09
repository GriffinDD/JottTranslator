package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott expression node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ExprNode implements JottTree{

    private JottTree expression;

    /**
     * Constructor that will try to build an expression node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative expression
     */
    public ExprNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if it is a string and then some end expression, we know it is just a string. Otherwise, it may be the start
        //of a string rel_op string
        if(tokens.size() >= 2 && tokens.get(0).getTokenType() == STRING && (tokens.get(1).getTokenType() == SEMICOLON || tokens.get(1).getTokenType() == R_BRACKET
                || tokens.get(1).getTokenType() == COMMA)) {
            expression = new SExprNode(tokens);
        }else{
            //we have no idea what kind of expr this is, so we should test which ones we can make
            ArrayList<Token> boolTester = new ArrayList<Token>();
            ArrayList<Token> dblTester = new ArrayList<Token>();
            ArrayList<Token> intTester = new ArrayList<Token>();
            for(Token t: tokens){
                boolTester.add(t);
                dblTester.add(t);
                intTester.add(t);
            }
            TestDBI tester = new TestDBI();
            TestDBI tester2 = new TestDBI();
            TestDBI tester3 = new TestDBI();
            if(tester.testBool(boolTester)){
                expression = new BExprNode(tokens);
            }else if(tester2.testDouble(dblTester)){
                expression = new DExprNode(tokens);
            }else if(tester3.testInt(intTester)){
                expression = new IExprNode(tokens);
            }else{
                //if we get to this point, then we know that whatever we are parsing cannot correctly be evaluated as any
                //type of expression. To get the most relevant error, we compare our test functions to see which expression
                //was able to parse the most terms before failing to find the most relevant syntax error.
                if(intTester.size() <= dblTester.size() && intTester.size() <= boolTester.size()){
                    expression = new IExprNode(tokens);
                }else if(dblTester.size() <= intTester.size() && dblTester.size() <= boolTester.size()){
                    expression = new DExprNode(tokens);
                } else{
                    expression = new BExprNode(tokens);
                }
            }

        }
        //if this is the end of our full expression we can stop here
        if(tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA) {
            return;
        }else {
            printErrorParser("expected ;|,|] got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }

    }

    /**
     * Responsible for parsing an expression
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
       return expression.convertToJott();
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
