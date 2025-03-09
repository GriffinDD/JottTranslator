package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

/**
 * This class is responsible for representing a parsed Jott program node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ProgramNode implements JottTree{
    private JottTree node;

    /**
     * Constructor that will try to build a program node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative program
     */
    public ProgramNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        node = new FuncLstNode(tokens);
    }

    /**
     * Responsible for parsing a while statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){return node.convertToJott();}

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
