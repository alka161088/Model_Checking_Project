using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace ModelCheckCTL.Objects
{
    /// <summary>
    /// Class to hold CTL Formula
    /// </summary>
    public class CtlFormula
    {
        /// <summary>
        /// Enum determining Type of SAT
        /// </summary>
        public enum TypeSAT
        {
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
        }

        public KripkeStructure _kripke { get; set; }
        public State _state { get; set; }
        public string _expression { get; set; }
        public Dictionary<string, string> _convertionString;
        
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="state"></param>
        /// <param name="kripke"></param>
        public CtlFormula(string expression, State state, KripkeStructure kripke)
        {
            this._convertionString = new Dictionary<string, string>();
            this._convertionString.Add("and", "&");
            this._convertionString.Add("or", "|");
            this._convertionString.Add("->", ">");
            this._convertionString.Add("not", "!");

            this._kripke = kripke;
            this._state = state;
            this._expression = ConvertToSystemFormula(expression);
        }

        /// <summary>
        /// Replace input symbols with known system symbols
        /// </summary>
        /// <param name="expression"></param>
        /// <returns></returns>
        public string ConvertToSystemFormula(string expression)
        {
            foreach (KeyValuePair<string, string> entry in this._convertionString)
            {
                expression = expression.Replace(entry.Key.ToString(), entry.Value.ToString());
            }

            return expression;
        }

        /// <summary>
        /// Determine whether given state satisfy given CTL formula
        /// </summary>
        /// <returns></returns>
        public bool IsSatisfy()
        {
            List<State> states = SAT(_expression);
            if (states.Contains(_state))
                return true;
            return false;
        }

        /// <summary>
        /// Determine Type of SAT for a given expression
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="leftExpression"></param>
        /// <param name="rightExpression"></param>
        /// <returns></returns>
        private TypeSAT GetTypeSAT(string expression, ref string leftExpression, ref string rightExpression)
        {
            //remove extra brackets
            expression = RemoveExtraBrackets(expression);

            //look for binary implies
            if (expression.Contains(">"))
            {
                if (IsBinaryOp(expression, ">", ref leftExpression, ref rightExpression))
                    return TypeSAT.Implies;
            }
            //look for binary and
            if (expression.Contains("&"))
            {
                if (IsBinaryOp(expression, "&", ref leftExpression, ref rightExpression))
                    return TypeSAT.And;
            }
            //look for binary or
            if (expression.Contains("|"))
            {
                if (IsBinaryOp(expression, "|", ref leftExpression, ref rightExpression))
                    return TypeSAT.Or;
            }
            //look for binary AU
            if (expression.StartsWith("A("))
            {
                string strippedExpression = expression.Substring(2, expression.Length - 3);
                if (IsBinaryOp(strippedExpression, "U", ref leftExpression, ref rightExpression))
                    return TypeSAT.AU;
            }
            //look for binary EU
            if (expression.StartsWith("E("))
            {
                string strippedExpression = expression.Substring(2, expression.Length - 3);
                if (IsBinaryOp(strippedExpression, "U", ref leftExpression, ref rightExpression))
                    return TypeSAT.EU;
            }

            //look for unary T, F, !, AX, EX, AG, EG, AF, EF, atomic
            if (expression.Equals("T"))
            {
                leftExpression = expression;
                return TypeSAT.AllTrue;
            }
            if (expression.Equals("F"))
            {
                leftExpression = expression;
                return TypeSAT.AllFalse;
            }
            if (IsAtomic(expression))
            {
                leftExpression = expression;
                return TypeSAT.Atomic;
            }
            if (expression.StartsWith("!"))
            {
                leftExpression = expression.Substring(1, expression.Length - 1);
                return TypeSAT.Not;
            }
            if (expression.StartsWith("AX"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.AX;
            }
            if (expression.StartsWith("EX"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.EX;
            }
            if (expression.StartsWith("EF"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.EF;
            }
            if (expression.StartsWith("EG"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.EG;
            }
            if (expression.StartsWith("AF"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.AF;
            }
            if (expression.StartsWith("AG"))
            {
                leftExpression = expression.Substring(2, expression.Length - 2);
                return TypeSAT.AG;
            }

            return TypeSAT.Unknown;
        }

        /// <summary>
        /// Determine states that satisfy given expression
        /// </summary>
        /// <param name="expression"></param>
        /// <returns></returns>
        private List<State> SAT(string expression)
        {
            System.Diagnostics.Debug.WriteLine(string.Format("Original Expression: {0}", expression));
            List<State> states = new List<State>();

            //from Logic in Computer Science, page 227
            string leftExpression = string.Empty, rightExpression = string.Empty;

            //TypeSAT typeSAT = DetermineTypeSAT(expression, ref leftExpression, ref rightExpression);
            TypeSAT typeSAT = GetTypeSAT(expression, ref leftExpression, ref rightExpression);

            System.Diagnostics.Debug.WriteLine(string.Format("Type SAT: {0}", typeSAT.ToString()));
            System.Diagnostics.Debug.WriteLine(string.Format("Left Expression: {0}", leftExpression));
            System.Diagnostics.Debug.WriteLine(string.Format("Right Expression: {0}", rightExpression));
            System.Diagnostics.Debug.WriteLine("------------------------------------");

            switch (typeSAT)
            {
                case TypeSAT.AllTrue:
                    //all states
                    states.AddRange(_kripke.States.ToArray());
                    break;
                case TypeSAT.AllFalse:
                    //empty 
                    break;
                case TypeSAT.Atomic:
                    foreach (State state in _kripke.States)
                    {
                        if (state.Atoms.Contains(leftExpression))
                            states.Add(state);
                    }
                    break;
                case TypeSAT.Not:
                    //S − SAT (φ1)
                    states.AddRange(_kripke.States.ToArray());
                    List<State> f1States = SAT(leftExpression);

                    foreach (State state in f1States)
                    {
                        if (states.Contains(state))
                            states.Remove(state);
                    }
                    break;
                case TypeSAT.And:
                    //SAT (φ1) ∩ SAT (φ2)
                    List<State> andF1States = SAT(leftExpression);
                    List<State> andF2States = SAT(rightExpression);

                    foreach (State state in andF1States)
                    {
                        if (andF2States.Contains(state))
                            states.Add(state);
                    }
                    break;
                case TypeSAT.Or:
                    //SAT (φ1) ∪ SAT (φ2)
                    List<State> orF1States = SAT(leftExpression);
                    List<State> orF2States = SAT(rightExpression);

                    states = orF1States;
                    foreach (State state in orF2States)
                    {
                        if (!states.Contains(state))
                            states.Add(state);
                    }
                    break;
                case TypeSAT.Implies:
                    //SAT (¬φ1 ∨ φ2)
                    //TODO: reevaluate impliesFormula
                    string impliesFormula = string.Concat("!", leftExpression, "|", rightExpression);
                    states = SAT(impliesFormula);
                    break;
                case TypeSAT.AX:
                    //SAT (¬EX¬φ1)
                    //TODO: reevaluate axFormula
                    string axFormula = string.Concat("!", "EX", "!", leftExpression);
                    states = SAT(axFormula);

                    //check if states actually has link to next state
                    List<State> tempStates = new List<State>();
                    foreach (State sourceState in states)
                    {
                        foreach (Transition transition in _kripke.Transitions)
                        {
                            if (sourceState.Equals(transition.FromState))
                            {
                                tempStates.Add(sourceState);
                                break;
                            }
                        }
                    }
                    states = tempStates;
                    break;
                case TypeSAT.EX:
                    //SATEX(φ1)
                    //TODO: reevaluate exFormula
                    string exFormula = leftExpression;
                    states = SAT_EX(exFormula);
                    break;
                case TypeSAT.AU:
                    //A[φ1 U φ2]
                    //SAT(¬(E[¬φ2 U (¬φ1 ∧¬φ2)] ∨ EG¬φ2))
                    //TODO: reevaluate auFormulaBuilder
                    StringBuilder auFormulaBuilder = new StringBuilder();
                    auFormulaBuilder.Append("!(E(!");
                    auFormulaBuilder.Append(rightExpression);
                    auFormulaBuilder.Append("U(!");
                    auFormulaBuilder.Append(leftExpression);
                    auFormulaBuilder.Append("&!");
                    auFormulaBuilder.Append(rightExpression);
                    auFormulaBuilder.Append("))|(EG!");
                    auFormulaBuilder.Append(rightExpression);
                    auFormulaBuilder.Append("))");
                    states = SAT(auFormulaBuilder.ToString());
                    break;
                case TypeSAT.EU:
                    //SATEU(φ1, φ2)
                    //TODO: reevaluate euFormula
                    states = SAT_EU(leftExpression, rightExpression);
                    break;
                case TypeSAT.EF:
                    //SAT (E( U φ1))
                    //TODO: reevaluate efFormula
                    string efFormula = string.Concat("E(TU", leftExpression, ")");
                    states = SAT(efFormula);
                    break;
                case TypeSAT.EG:
                    //SAT(¬AF¬φ1)
                    //TODO: reevaulate egFormula
                    string egFormula = string.Concat("!AF!", leftExpression);
                    states = SAT(egFormula);
                    break;
                case TypeSAT.AF:
                    //SATAF (φ1)
                    //TODO: reevaluate afFormula
                    string afFormula = leftExpression;
                    states = SAT_AF(afFormula);
                    break;
                case TypeSAT.AG:
                    //SAT (¬EF¬φ1)
                    //TODO: reevaluate agFormula
                    string agFormula = string.Concat("!EF!", leftExpression);
                    states = SAT(agFormula);
                    break;
                case TypeSAT.Unknown:
                    throw new FormatException("Invalid CTL expression");
            }

            return states;
        }

        /// <summary>
        /// Handling EX
        /// </summary>
        /// <param name="expression"></param>
        /// <returns></returns>
        private List<State> SAT_EX(string expression)
        {
            //X := SAT (φ);
            //Y := pre∃(X);
            //return Y
            List<State> x = new List<State>();
            List<State> y = new List<State>();
            x = SAT(expression);
            y = PreE(x);
            return y;
        }

        /// <summary>
        /// Handling EU
        /// </summary>
        /// <param name="leftExpression"></param>
        /// <param name="rightExpression"></param>
        /// <returns></returns>
        private List<State> SAT_EU(string leftExpression, string rightExpression)
        {
            List<State> w = new List<State>();
            List<State> x = new List<State>();
            List<State> y = new List<State>();

            w = SAT(leftExpression);
            x.AddRange(_kripke.States.ToArray());
            y = SAT(rightExpression);

            while (!AreListStatesEqual(x, y))
            {
                x = y;
                List<State> newY = new List<State>();
                List<State> preEStates = PreE(y);

                newY.AddRange(y.ToArray());
                List<State> wAndPreE = new List<State>();
                foreach (State state in w)
                {
                    if (preEStates.Contains(state))
                        wAndPreE.Add(state);
                }

                foreach (State state in wAndPreE)
                {
                    if (!newY.Contains(state))
                        newY.Add(state);
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
        private List<State> SAT_AF(string expression)
        {
            List<State> x = new List<State>();
            x.AddRange(_kripke.States.ToArray());
            List<State> y = new List<State>();
            y = SAT(expression);

            while (!AreListStatesEqual(x, y))
            {
                x = y;
                List<State> newY = new List<State>();
                List<State> preAStates = PreA(y);
                newY.AddRange(y.ToArray());

                foreach (State state in preAStates)
                {
                    if (!newY.Contains(state))
                        newY.Add(state);
                }

                y = newY;
            }

            return y;
        }

        /// <summary>
        /// PreE
        /// </summary>
        /// <param name="y"></param>
        /// <returns></returns>
        private List<State> PreE(List<State> y)
        {
            //{s ∈ S | exists s, (s → s and s ∈ Y )}
            List<State> states = new List<State>();

            List<Transition> transitions = new List<Transition>();
            foreach (State sourceState in _kripke.States)
            {
                foreach (State destState in y)
                {
                    Transition myTransition = new Transition(sourceState, destState);
                    if (_kripke.Transitions.Contains(myTransition))
                    {
                        if (!states.Contains(sourceState))
                            states.Add(sourceState);
                    }
                }
            }

            return states;
        }

        /// <summary>
        /// PreA
        /// </summary>
        /// <param name="y"></param>
        /// <returns></returns>
        private List<State> PreA(List<State> y)
        {
            //pre∀(Y ) = pre∃y − pre∃(S − Y)
            List<State> PreEY = PreE(y);

            List<State> S_Minus_Y = new List<State>();
            S_Minus_Y.AddRange(_kripke.States.ToArray());

            foreach (State state in y)
            {
                if (S_Minus_Y.Contains(state))
                    S_Minus_Y.Remove(state);
            }

            List<State> PreE_S_Minus_Y = PreE(S_Minus_Y);

            //PreEY - PreE(S-Y)
            foreach (State state in PreE_S_Minus_Y)
            {
                if (PreEY.Contains(state))
                    PreEY.Remove(state);
            }

            return PreEY;
        }

        /// <summary>
        /// Determine whether the list contain same set of states
        /// </summary>
        /// <param name="list1"></param>
        /// <param name="list2"></param>
        /// <returns></returns>
        private bool AreListStatesEqual(List<State> list1, List<State> list2)
        {
            if (list1.Count != list2.Count)
                return false;

            foreach (State state in list1)
            {
                if (!list2.Contains(state))
                    return false;
            }

            return true;
        }

        /// <summary>
        /// Determine whether this is an atom
        /// </summary>
        /// <param name="expression"></param>
        /// <returns></returns>
        private bool IsAtomic(string expression)
        {
            if (_kripke.Atoms.Contains(expression))
                return true;
            return false;
        }

        /// <summary>
        /// Determine whether given expression contains binary operation for the next checking
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="symbol"></param>
        /// <param name="leftExpression"></param>
        /// <param name="rightExpression"></param>
        /// <returns></returns>
        private bool IsBinaryOp(string expression, string symbol, ref string leftExpression, ref string rightExpression)
        {
            bool isBinaryOp = false;
            if (expression.Contains(symbol))
            {
                int openParanthesisCount = 0;
                int closeParanthesisCount = 0;

                for (int i = 0; i < expression.Length; i++)
                {
                    string currentChar = expression.Substring(i, 1);
                    if (currentChar.Equals(symbol) && openParanthesisCount == closeParanthesisCount)
                    {
                        leftExpression = expression.Substring(0, i);
                        rightExpression = expression.Substring(i + 1, expression.Length - i - 1);
                        isBinaryOp = true;
                        break;
                    }
                    else if (currentChar.Equals("("))
                    {
                        openParanthesisCount++;
                    }
                    else if (currentChar.Equals(")"))
                    {
                        closeParanthesisCount++;
                    }
                }
            }
            return isBinaryOp;
        }

        /// <summary>
        /// Removing extra brackets
        /// </summary>
        /// <param name="expression"></param>
        /// <returns></returns>
        private string RemoveExtraBrackets(string expression)
        {
            string newExpression = expression;
            int openParanthesis = 0;
            int closeParanthesis = 0;

            if (expression.StartsWith("(") && expression.EndsWith(")"))
            {
                for (int i = 0; i < expression.Length - 1; i++)
                {
                    string charExpression = expression.Substring(i, 1);

                    if (charExpression.Equals("("))
                        openParanthesis++;
                    else if (charExpression.Equals(")"))
                        closeParanthesis++;
                }

                if (openParanthesis - 1 == closeParanthesis)
                    newExpression = expression.Substring(1, expression.Length - 2);
            }
            return newExpression;
        }
    }
}
