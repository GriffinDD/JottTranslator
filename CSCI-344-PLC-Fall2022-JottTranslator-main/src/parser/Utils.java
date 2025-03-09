package src.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilities class filled with various useful methods
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class Utils {

    public static String PROGRAM = "program";
    public static String FUNC_LIST = "function_list";

    public static String FUNC_DEF = "function_def";
    public static String FUNC_DEF_PARAMS = "func_def_params";
    public static String FUNC_DEF_PARAMS_T = "func_def_params_t";

    public static String BODY_STMT = "body_stmt";
    public static String RETURN_STMT = "return_stmt";
    public static String BODY = "body";
    public static String END_STMT = "end_stmt";

    public static String IF_STMT = "if_stmt";
    public static String ELSE = "else";
    public static String ELSEIF_LST = "elseif_lst";

    public static String WHILE_LOOP = "while_loop";

    public static String CHAR = "char";
    public static String L_CHAR = "l_char";
    public static String U_CHAR = "u_char";
    public static String DIGIT = "digit";
    public static String SIGN = "sign";
    public static String ID = "id";

    public static String STMT = "stmt";
    public static String FUNC_CALL = "func_call";

    public static String PARAMS = "params";
    public static String PARAMS_T = "params";

    public static String EXPR = "expr";

    public static String TYPE = "type";
    public static String FUNCTION_RETURN = "function_return";
    public static String VAR_DEC = "var_dec";
    public static String ASMT = "asmt";

    public static String OP = "op";
    public static String REL_OP = "rel_op";

    public static String DBL = "dbl";
    public static String D_EXPR = "d_expr";

    public static String BOOL = "bool";
    public static String B_EXPR = "b_expr";

    public static String INT = "int";
    public static String I_EXPR = "i_expr";

    public static String STR_LITERAL = "str_literal";
    public static String STR = "str";
    public static String S_EXPR = "s_expr";

    /**
     * @return The first set for the Jott language, where the keys are tokens
     * and the values are the regex needed to recognize it.
     */
    public static HashMap<String, Pattern> getFirstSetTokenKey() {
        HashMap<String, Pattern> firstSet = new HashMap<>();

        firstSet.put(PROGRAM, Pattern.compile("\\p{Lower}|^$"));
        firstSet.put(FUNC_LIST, Pattern.compile("\\p{Lower}|^$"));

        firstSet.put(FUNC_DEF, Pattern.compile("\\p{Lower}"));
        firstSet.put(FUNC_DEF_PARAMS, Pattern.compile("\\p{Lower}|^$"));
        firstSet.put(FUNC_DEF_PARAMS_T, Pattern.compile("^,$|^$"));

        firstSet.put(BODY_STMT, Pattern.compile("^if$|^while$|" +
                "^Double$|^Integer$|^String$|^Boolean$|\\p{Lower}"));
        firstSet.put(BODY, Pattern.compile("^if$|^while$|" +
                "^Double$|^Integer$|^String$|^Boolean$|\\p{Lower}|^return$|^$"));

        firstSet.put(RETURN_STMT, Pattern.compile("^return$"));
        firstSet.put(END_STMT, Pattern.compile("^;$"));

        firstSet.put(IF_STMT, Pattern.compile("^if$"));
        firstSet.put(ELSE, Pattern.compile("^else$|^$"));
        firstSet.put(ELSEIF_LST, Pattern.compile("^elseif$|^$"));
        firstSet.put(WHILE_LOOP, Pattern.compile("^while$"));

        firstSet.put(CHAR, Pattern.compile("\\p{Lower}|\\p{Upper}|\\d"));
        firstSet.put(L_CHAR, Pattern.compile("\\p{Lower}"));
        firstSet.put(U_CHAR, Pattern.compile("\\p{Upper}"));
        firstSet.put(DIGIT, Pattern.compile("\\d"));
        firstSet.put(SIGN, Pattern.compile("^-$|^\\+$|^$"));
        firstSet.put(ID, Pattern.compile("\\p{Lower}"));

        firstSet.put(STMT, Pattern.compile("^Double$|^Integer$|^String$|^Boolean$|\\p{Lower}"));
        firstSet.put(FUNC_CALL, Pattern.compile("\\p{Lower}"));
        firstSet.put(PARAMS, Pattern.compile("\\p{Lower}|^-$|^//+$|^$|^\"$|^True$|^False$"));
        firstSet.put(PARAMS_T, Pattern.compile("^,$|^$"));

        firstSet.put(EXPR, Pattern.compile("\\p{Lower}|^-$|^//+$|^$|^\"$|^True$|^False$"));
        firstSet.put(TYPE, Pattern.compile("^Double$|^Integer$|^String$|^Boolean$"));
        firstSet.put(FUNCTION_RETURN, Pattern.compile("^Double$|^Integer$|^String$|^Boolean$|^Void$"));
        firstSet.put(VAR_DEC, Pattern.compile("^Double$|^Integer$|^String$|^Boolean$"));
        firstSet.put(ASMT, Pattern.compile("^Double$|^Integer$|^String$|^Boolean$|\\p{Lower}"));

        firstSet.put(OP, Pattern.compile("^\\+$|^\\*$|^/$|^-$"));
        firstSet.put(REL_OP, Pattern.compile("^>$|^>=$|^<$|^<=$|^==$|^!=$"));

        firstSet.put(DBL, Pattern.compile("^-$|^\\+$|^$"));
        firstSet.put(D_EXPR, Pattern.compile("^-$|^\\+$|^$|\\p{Lower}"));

        firstSet.put(BOOL, Pattern.compile("^True$|^False$"));
        firstSet.put(B_EXPR, Pattern.compile("^-$|^\\+$|^$|\\p{Lower}|^True$|^False$"));

        firstSet.put(INT, Pattern.compile("^-$|^\\+$|^$"));
        firstSet.put(I_EXPR, Pattern.compile("^-$|^\\+$|^$|\\p{Lower}"));

        firstSet.put(STR_LITERAL, Pattern.compile("^\"$"));
        firstSet.put(STR, Pattern.compile("\\p{Lower}|\\p{Upper}|\\d|\\s|^$"));
        firstSet.put(S_EXPR, Pattern.compile("^\"$|\\p{Lower}"));

        return firstSet;
    }

    /**
     * @return The first set for the Jott language, where the keys are regexes
     * and the values are an ArrayList of all the possible tokens that could be.
     */
    public static HashMap<Pattern, ArrayList<String>> getFirstSetRegexKey() {
        HashMap<Pattern, ArrayList<String>> firstSet = new HashMap<>();

        firstSet.put(Pattern.compile("\\p{Lower}"), new ArrayList<>(List.of(
                PROGRAM, FUNC_LIST, FUNC_DEF, FUNC_DEF_PARAMS,
                BODY_STMT, BODY,
                CHAR, L_CHAR, ID,
                STMT, FUNC_CALL, PARAMS,
                EXPR, ASMT, D_EXPR, B_EXPR, I_EXPR, STR, S_EXPR
        )));

        firstSet.put(Pattern.compile("\\p{Upper}"), new ArrayList<>(List.of(
                CHAR, U_CHAR, STR
        )));

        firstSet.put(Pattern.compile("\\d"), new ArrayList<>(List.of(
                CHAR, DIGIT, STR
        )));

        firstSet.put(Pattern.compile("^if$"), new ArrayList<>(List.of(
                BODY, BODY_STMT, IF_STMT
        )));

        firstSet.put(Pattern.compile("^while$"), new ArrayList<>(List.of(
                BODY, BODY_STMT, WHILE_LOOP
        )));

        firstSet.put(Pattern.compile("^else$"), new ArrayList<>(List.of(
                ELSE
        )));

        firstSet.put(Pattern.compile("^elseif$"), new ArrayList<>(List.of(
                ELSEIF_LST
        )));

        firstSet.put(Pattern.compile("^return$"), new ArrayList<>(List.of(
                RETURN_STMT, BODY
        )));

        firstSet.put(Pattern.compile("^Double$"), new ArrayList<>(List.of(
                BODY_STMT, BODY, STMT,
                TYPE, FUNCTION_RETURN, VAR_DEC, ASMT
        )));

        firstSet.put(Pattern.compile("^Integer$"), new ArrayList<>(List.of(
                BODY_STMT, BODY, STMT,
                TYPE, FUNCTION_RETURN, VAR_DEC, ASMT
        )));

        firstSet.put(Pattern.compile("^String$"), new ArrayList<>(List.of(
                BODY_STMT, BODY, STMT,
                TYPE, FUNCTION_RETURN, VAR_DEC, ASMT
        )));

        firstSet.put(Pattern.compile("^Boolean$"), new ArrayList<>(List.of(
                BODY_STMT, BODY, STMT,
                TYPE, FUNCTION_RETURN, VAR_DEC, ASMT
        )));

        firstSet.put(Pattern.compile("^Void$"), new ArrayList<>(List.of(
                FUNCTION_RETURN
        )));

        firstSet.put(Pattern.compile("^-$"), new ArrayList<>(List.of(
                SIGN, PARAMS, EXPR, OP,
                DBL, D_EXPR, INT, I_EXPR, B_EXPR
        )));

        firstSet.put(Pattern.compile("^\\+$"), new ArrayList<>(List.of(
                SIGN, PARAMS, EXPR, OP,
                DBL, D_EXPR, INT, I_EXPR, B_EXPR
        )));

        firstSet.put(Pattern.compile("^\\*$"), new ArrayList<>(List.of(
                OP
        )));

        firstSet.put(Pattern.compile("^/$"), new ArrayList<>(List.of(
                OP
        )));

        firstSet.put(Pattern.compile("^\"$"), new ArrayList<>(List.of(
                PARAMS, EXPR, B_EXPR, STR_LITERAL, S_EXPR
        )));

        firstSet.put(Pattern.compile("^True$"), new ArrayList<>(List.of(
                PARAMS, EXPR, BOOL, B_EXPR
        )));

        firstSet.put(Pattern.compile("^False$"), new ArrayList<>(List.of(
                PARAMS, EXPR, BOOL, B_EXPR
        )));

        firstSet.put(Pattern.compile("^,$"), new ArrayList<>(List.of(
                FUNC_DEF_PARAMS_T, PARAMS_T
        )));

        firstSet.put(Pattern.compile("^;$"), new ArrayList<>(List.of(
                END_STMT
        )));

        firstSet.put(Pattern.compile("^>$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^>=$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^<$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^<=$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^==$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^!=$"), new ArrayList<>(List.of(
                REL_OP
        )));

        firstSet.put(Pattern.compile("^\\s$"), new ArrayList<>(List.of(
                STR
        )));

        firstSet.put(Pattern.compile("^$"), new ArrayList<>(List.of(
                PROGRAM, FUNC_LIST, FUNC_DEF_PARAMS, FUNC_DEF_PARAMS_T,
                BODY, ELSE, ELSEIF_LST, SIGN,
                PARAMS, PARAMS_T, EXPR,
                DBL, D_EXPR, B_EXPR, INT, I_EXPR, STR
        )));

        return firstSet;
    }

}
