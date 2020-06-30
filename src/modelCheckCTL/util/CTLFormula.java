package modelCheckCTL.util;

import modelCheckCTL.model.ExpressionsHolder;
import modelCheckCTL.model.KripkeStructure;
import modelCheckCTL.model.State;
import modelCheckCTL.model.Transition;

import java.util.*;

public class CTLFormula {
    /// <summary>
    /// Enum determining Type of SAT
    /// </summary>
    public enum TypeSAT {
        Unknown,
        AllTrue,
        AllFalse,
        Atomic,
        Not,
        And,
        Or,
        Implies,
        AX,
        EX,
        AU,
        EU,
        EF,
        EG,
        AF,
        AG
    };

    public KripkeStructure _kripke;
    public State state;
    public String _expression;
    public Map<String, String> _convertionString;
//    public String leftExpression = "", rightExpression = "";

    public CTLFormula(String expression, State state, KripkeStructure kripke) {
        this._convertionString = new HashMap<String, String>();
        this._convertionString.put("and", "&");
        this._convertionString.put("or", "|");
        this._convertionString.put("->", ">");
        this._convertionString.put("not", "!");

        this._kripke = kripke;
        this.state = state;
        this._expression = ConvertToSystemFormula(expression);
    }

    /// <summary>
    /// Constructor
    /// </summary>
    /// <param name="expression"></param>
    /// <param name="state"></param>
    /// <param name="kripke"></param>
    public String ConvertToSystemFormula(String expression) {
        for (String key : this._convertionString.keySet()) {
            expression = expression.replace(key, this._convertionString.get(key));
        }

        return expression;
    }

    /// <summary>
    /// Replace input symbols with known system symbols
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>
    public boolean IsSatisfy() {
        List<State> states = SAT(_expression);
        if (states.contains(state))
            return true;
        return false;
    }

    /// <summary>
    /// Determine whether given state satisfy given CTL formula
    /// </summary>
    /// <returns></returns>
    //private TypeSAT getTypeSAT(String expression, String leftExpression, String rightExpression) {
    private TypeSAT getTypeSAT(String expression, ExpressionsHolder expressionsHolder) {
        //remove extra brackets
        expression = removeExtraBrackets(expression);

        //look for binary implies
        if (expression.contains(">")) {
            if (isBinaryOp(expression, ">", expressionsHolder))
                return TypeSAT.Implies;
        }
        //look for binary and
        if (expression.contains("&")) {
            if (isBinaryOp(expression, "&", expressionsHolder))
                return TypeSAT.And;
        }
        //look for binary or
        if (expression.contains("|")) {
            if (isBinaryOp(expression, "|", expressionsHolder))
                return TypeSAT.Or;
        }
        //look for binary AU
        if (expression.startsWith("A(")) {
            String strippedExpression = expression.substring(2, expression.length());
            if (isBinaryOp(strippedExpression, "U", expressionsHolder))
                return TypeSAT.AU;
        }
        //look for binary EU
        if (expression.startsWith("E(")) {
            String strippedExpression = expression.substring(2, expression.length());
            if (isBinaryOp(strippedExpression, "U", expressionsHolder))
                return TypeSAT.EU;
        }

        //look for unary T, F, !, AX, EX, AG, EG, AF, EF, atomic
        if (expression.equals("T")) {
            expressionsHolder.setLeftExpression(expression);
            return TypeSAT.AllTrue;
        }
        if (expression.equals("F")) {
            expressionsHolder.setLeftExpression(expression);
            return TypeSAT.AllFalse;
        }
        if (isAtomic(expression)) {
            expressionsHolder.setLeftExpression(expression);
            return TypeSAT.Atomic;
        }
        if (expression.startsWith("!")) {
            expressionsHolder.setLeftExpression(expression.substring(1, expression.length()));
            return TypeSAT.Not;
        }
        if (expression.startsWith("AX")) {
            expressionsHolder.setLeftExpression(expression.substring(2, expression.length()));
            return TypeSAT.AX;
        }
        if (expression.startsWith("EX")) {
            expressionsHolder.setLeftExpression(expression.substring(2, expression.length()));
            return TypeSAT.EX;
        }
        if (expression.startsWith("EF")) {
            expressionsHolder.setLeftExpression(expression.substring(2, expression.length()));
            return TypeSAT.EF;
        }
        if (expression.startsWith("EG")) {
            expressionsHolder.setLeftExpression(expression.substring(2, expression.length()));
            return TypeSAT.EG;
        }
        if (expression.startsWith("AF")) {
            expressionsHolder.setLeftExpression(expression.substring(2, expression.length()));
            return TypeSAT.AF;
        }
        if (expression.startsWith("AG")) {
            expressionsHolder.setRightExpression(expression.substring(2, expression.length()));
            return TypeSAT.AG;
        }

        return TypeSAT.Unknown;
    }

    /// <summary>
    /// Determine Type of SAT for a given expression
    /// </summary>
    /// <param name="expression"></param>
    /// <param name="leftExpression"></param>
    /// <param name="rightExpression"></param>
    /// <returns></returns>
    private List<State> SAT(String expression) {
        System.out.println(String.format("Original Expression: %s", expression));
        List<State> states = new ArrayList<State>();

        //from Logic in Computer Science, page 227
//        leftExpression = "";
//        rightExpression = "";
        ExpressionsHolder expressionsHolder = new ExpressionsHolder();

        expression = expression.trim();
        TypeSAT typeSAT = getTypeSAT(expression, expressionsHolder);

        System.out.println(String.format("Type SAT: %s", typeSAT.toString()));
        System.out.println(String.format("Left Expression: %s", expressionsHolder.getLeftExpression()));
        System.out.println(String.format("Right Expression: %s", expressionsHolder.getRightExpression()));

        System.out.println("-----------------------------------");

        switch (typeSAT) {
            case AllTrue:
                //all states
                states.addAll(_kripke.states);
                break;
            case AllFalse:
                //empty
                break;
            case Atomic:
                for (State state : _kripke.states) {
                    if (state.atoms.contains(expressionsHolder.getLeftExpression()))
                        states.add(state);
                }
                break;
            case Not:
                //S − SAT (φ1)
                states.addAll(_kripke.states);
                List<State> f1States = SAT(expressionsHolder.getLeftExpression());

                for (State state : f1States) {
                    if (states.contains(state))
                        states.remove(state);
                }
                break;
            case And:
                //SAT (φ1) ∩ SAT (φ2)
                List<State> andF1States = SAT(expressionsHolder.getLeftExpression());
                List<State> andF2States = SAT(expressionsHolder.getRightExpression());

                for (State state : andF1States) {
                    if (andF2States.contains(state))
                        states.add(state);
                }
                break;
            case Or:
                //SAT (φ1) ∪ SAT (φ2)
                List<State> orF1States = SAT(expressionsHolder.getLeftExpression());
                List<State> orF2States = SAT(expressionsHolder.getRightExpression());

                states = orF1States;
                for (State state : orF2States) {
                    if (!states.contains(state))
                        states.add(state);
                }
                break;
            case Implies:
                //SAT (¬φ1 ∨ φ2)
                //TODO: reevaluate impliesFormula
                String impliesFormula = "!" + expressionsHolder.getLeftExpression() + "|" + expressionsHolder.getRightExpression();
                states = SAT(impliesFormula);
                break;
            case AX:
                //SAT (¬EX¬φ1)
                //TODO: reevaluate axFormula
                String axFormula = "!" + "EX" + "!" + expressionsHolder.getLeftExpression();

                states = SAT(axFormula);

                //check if states actually has link to next state
                List<State> tempStates = new ArrayList<>();
                for (State sourceState : states) {
                    for (Transition transition : _kripke.transitions) {
                        if (sourceState.equals(transition.fromState)) {
                            tempStates.add(sourceState);
                            break;
                        }
                    }
                }
                states = tempStates;
                break;
            case EX:
                //SATEX(φ1)
                //TODO: reevaluate exFormula
                String exFormula = expressionsHolder.getLeftExpression();
                states = SAT_EX(exFormula);
                break;
            case AU:
                //A[φ1 U φ2]
                //SAT(¬(E[¬φ2 U (¬φ1 ∧¬φ2)] ∨ EG¬φ2))
                //TODO: reevaluate auFormulaBuilder
                StringBuilder auFormulaBuilder = new StringBuilder();
                auFormulaBuilder.append("!(E(!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("U(!");
                auFormulaBuilder.append(expressionsHolder.getLeftExpression());
                auFormulaBuilder.append("&!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("))|(EG!");
                auFormulaBuilder.append(expressionsHolder.getRightExpression());
                auFormulaBuilder.append("))");
                states = SAT(auFormulaBuilder.toString());
                break;
            case EU:
                //SATEU(φ1, φ2)
                //TODO: reevaluate euFormula
                states = SAT_EU(expressionsHolder.getLeftExpression(), expressionsHolder.getRightExpression());
                break;
            case EF:
                //SAT (E( U φ1))
                //TODO: reevaluate efFormula
                String efFormula = "E(TU" + expressionsHolder.getLeftExpression() + ")";
                states = SAT(efFormula);
                break;
            case EG:
                //SAT(¬AF¬φ1)
                //TODO: reevaulate egFormula
                String egFormula = "!AF!" + expressionsHolder.getLeftExpression();
                states = SAT(egFormula);
                break;
            case AF:
                //SATAF (φ1)
                //TODO: reevaluate afFormula
                String afFormula = expressionsHolder.getLeftExpression();
                states = SAT_AF(afFormula);
                break;
            case AG:
                //SAT (¬EF¬φ1)
                //TODO: reevaluate agFormula
//                String agFormula = "!EF!" + leftExpression;
                String agFormula = "";
                agFormula = agFormula.concat("!EF!").concat(expressionsHolder.getLeftExpression());
                states = SAT(agFormula);
                break;
            case Unknown:
                throw new IllegalArgumentException("Invalid CTL expression");
        }
        return states;
    }

    /// <summary>
    /// Determine states that satisfy given expression
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>
    private List<State> SAT_EX(String expression) {
        //X := SAT (φ);
        //Y := pre∃(X);
        //return Y
        List<State> x = new ArrayList<State>();
        List<State> y = new ArrayList<State>();
        x = SAT(expression);
        y = PreE(x);
        return y;
    }

    /// <summary>
    /// Handling EX
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>
    private List<State> SAT_EU(String leftExpression, String rightExpression) {
        List<State> w = new ArrayList<State>();
        List<State> x = new ArrayList<State>();
        List<State> y = new ArrayList<State>();

        w = SAT(leftExpression);
        x.addAll(_kripke.states);
        y = SAT(rightExpression);

        while (!AreListStatesEqual(x, y)) {
            x = y;
            List<State> newY = new ArrayList<State>();
            List<State> preEStates = PreE(y);

            newY.addAll(y);
            List<State> wAndPreE = new ArrayList<State>();
            for (State state : w) {
                if (preEStates.contains(state))
                    wAndPreE.add(state);
            }

            for (State state : wAndPreE) {
                if (!newY.contains(state))
                    newY.add(state);
            }
            y = newY;
        }

        return y;
    }

    /// <summary>
    /// Handling EU
    /// </summary>
    /// <param name="leftExpression"></param>
    /// <param name="rightExpression"></param>
    /// <returns></returns>
    private List<State> SAT_AF(String expression) {
        List<State> x = new ArrayList<State>();
        x.addAll(_kripke.states);
        List<State> y = new ArrayList<>();
        y = SAT(expression);

        while (!AreListStatesEqual(x, y)) {
            x = y;
            List<State> newY = new ArrayList<>();
            List<State> preAStates = PreA(y);
            newY.addAll(y);

            for (State state : preAStates) {
                if (!newY.contains(state))
                    newY.add(state);
            }

            y = newY;
        }

        return y;
    }

    /// <summary>
    /// Handling AF
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>
    private List<State> PreE(List<State> y) {
        //{s ∈ S | exists s, (s → s and s ∈ Y )}
        List<State> states = new ArrayList<>();

        List<Transition> transitions = new ArrayList<Transition>();
        for (State sourceState : _kripke.states) {
            for (State destState : y) {
                Transition myTransition = new Transition(sourceState, destState);
                if (_kripke.transitions.contains(myTransition)) {
                    if (!states.contains(sourceState))
                        states.add(sourceState);
                }
            }
        }

        return states;
    }

    /// <summary>
    /// PreE
    /// </summary>
    /// <param name="y"></param>
    /// <returns></returns>
    private List<State> PreA(List<State> y) {
        //pre∀(Y ) = pre∃y − pre∃(S − Y)
        List<State> PreEY = PreE(y);

        List<State> S_Minus_Y = new ArrayList<State>();
        S_Minus_Y.addAll(_kripke.states);

        for (State state : y) {
            if (S_Minus_Y.contains(state))
                S_Minus_Y.remove(state);
        }

        List<State> PreE_S_Minus_Y = PreE(S_Minus_Y);

        //PreEY - PreE(S-Y)
        for (State state : PreE_S_Minus_Y) {
            if (PreEY.contains(state))
                PreEY.remove(state);
        }

        return PreEY;
    }

    /// <summary>
    /// PreA
    /// </summary>
    /// <param name="y"></param>
    /// <returns></returns>
    private boolean AreListStatesEqual(List<State> list1, List<State> list2) {
        if (list1.size() != list2.size())
            return false;

        for (State state : list1) {
            if (!list2.contains(state))
                return false;
        }

        return true;
    }

    /// <summary>
    /// Determine whether the list contain same set of states
    /// </summary>
    /// <param name="list1"></param>
    /// <param name="list2"></param>
    /// <returns></returns>
    private boolean isAtomic(String expression) {
        if (_kripke.atoms.contains(expression))
            return true;
        return false;
    }

    /// <summary>
    /// Determine whether this is an atom
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>
    private boolean isBinaryOp(String expression, String symbol, ExpressionsHolder expressionsHolder) {
        boolean isBinaryOp = false;
        if (expression.contains(symbol)) {
            int openParanthesisCount = 0;
            int closeParanthesisCount = 0;

            for (int i = 0; i < expression.length() - 1; i++) {
                String currentChar = expression.substring(i, i + 1);
                if (currentChar.equals(symbol) && openParanthesisCount == closeParanthesisCount) {
                    expressionsHolder.setLeftExpression(expression.substring(0, i));
                    expressionsHolder.setRightExpression(expression.substring(i + 1, expression.length()));
                    isBinaryOp = true;
                    break;
                } else if (currentChar.equals("(")) {
                    openParanthesisCount++;
                } else if (currentChar.equals(")")) {
                    closeParanthesisCount++;
                }
            }
        }
        return isBinaryOp;
    }

    /// <summary>
    /// Determine whether given expression.contains binary operation for the next checking
    /// </summary>
    /// <param name="expression"></param>
    /// <param name="symbol"></param>
    /// <param name="leftExpression"></param>
    /// <param name="rightExpression"></param>
    /// <returns></returns>
    private String removeExtraBrackets(String expression) {
        String newExpression = expression;
        int openParanthesis = 0;
        int closeParanthesis = 0;

        if (expression.startsWith("(") && expression.endsWith(")")) {
            for (int i = 0; i < expression.length() - 1; i++) {
                String charExpression = expression.substring(i, i + 1);

                if (charExpression.equals("("))
                    openParanthesis++;
                else if (charExpression.equals(")"))
                    closeParanthesis++;
            }

            newExpression = expression.substring(1, expression.length());
        } else if (!expression.startsWith("(") && expression.endsWith(")")) {
            newExpression = expression.substring(0, expression.length() - 1);
        } else if (expression.startsWith("(") && !expression.endsWith(")")) {
            newExpression = expression.substring(1, expression.length());
        }
        
        return newExpression;
    }

    /// <summary>
    /// Removing extra brackets
    /// </summary>
    /// <param name="expression"></param>
    /// <returns></returns>

}
