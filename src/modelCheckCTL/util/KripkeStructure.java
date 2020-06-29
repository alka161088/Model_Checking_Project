package modelCheckCTL.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KripkeStructure {
    public List<Transition> transitions;
    public List<State> states;
    public List<String> atoms;

    /// <summary>
    /// Constructor
    /// </summary>
    public KripkeStructure() {
        transitions = new ArrayList<Transition>();
        states = new ArrayList<State>();
        atoms = new ArrayList<String>();
    }

    /// <summary>
    /// Parameterized constructor
    /// </summary>
    /// <param name="kripkeStructureDefinition"></param>
    public KripkeStructure(String kripkeStructureDefinition) {
        transitions = new ArrayList<Transition>();
        states = new ArrayList<State>();
        atoms = new ArrayList<String>();

        try {
            /*PARSING*/
            List<String> parsedStructure = Arrays.asList(kripkeStructureDefinition
                    .replace("\n", "")
                    .replace("\r", "")
                    .split(";"));

            if (parsedStructure == null || parsedStructure.size() != 3)
                throw new IllegalArgumentException("Input file does not contain appropriate segments to construct kripke structure");

            List<String> stateNames = Arrays.asList(parsedStructure.get(0)
                    .replace(" ", "")
                    .split(","));
            List<String> transitions = Arrays.asList(parsedStructure.get(1)
                    .replace(" ", "")
                    .split(","));
            List<String> stateAtomStructures = Arrays.asList(parsedStructure.get(2)
                    .split(","));

            //load states
            for (String stateName : stateNames) {
                State state = new State(stateName);
                if (!states.contains(state))
                    states.add(new State(stateName));
                else
                    throw new IllegalArgumentException(String.format("State {0} is defined more than once", stateName));
            }

            //load transitions
            for (String transition : transitions) {
                List<String> parsedTransition = Arrays.asList(transition.split(":"));

                if (parsedTransition == null || parsedTransition.size() != 2)
                    throw new IllegalArgumentException("Transition is not in the valid format");

                String transitionName = parsedTransition.get(0);
                List<String> parsedFromToStates = Arrays.asList(parsedTransition.get(1).split("-"));

                if (parsedFromToStates == null || parsedFromToStates.size() != 2)
                    throw new IllegalArgumentException(String.format("Transition {0} is not in [from state] - [to state] format", transitionName));

                String fromStateName = parsedFromToStates.get(0);
                String toStateName = parsedFromToStates.get(1);
                State fromState = FindStateByName(fromStateName);
                State toState = FindStateByName(toStateName);

                if (fromState == null || toState == null)
                    throw new IllegalArgumentException(String.format("Invalid state is detected in transition {0}", transitionName));

                Transition transitionObj = new Transition(transitionName, fromState, toState);
                if (!this.transitions.contains(transitionObj))
                    this.transitions.add(transitionObj);
                else {
                    throw new IllegalArgumentException(String.format("Transitions from state {0} to state {1} are defined more than once"
                            , fromStateName, toStateName));
                }
            }

            //load atoms
            for (String stateAtomStructure : stateAtomStructures) {
                List<String> parsedStateAtomStructure = Arrays.asList(stateAtomStructure.split(":"));

                if (parsedStateAtomStructure == null || parsedStateAtomStructure.size() !=2)
                    throw new IllegalArgumentException(String.format("{0} is not a valid state: atoms definition", stateAtomStructure));
                String stateName = parsedStateAtomStructure.get(0).replace(" ", "");
                String atomNames = parsedStateAtomStructure.get(1).trim();
                List<String> parsedAtoms = Arrays.asList(atomNames.split(" "));

                List<String> stateAtoms = new ArrayList<>();
                for (String atom : parsedAtoms) {
                    if (atom == null && atom.length() == 0) {
                    } else if (!stateAtoms.contains(atom))
                        stateAtoms.add(atom);
                    else
                        throw new IllegalArgumentException(String.format("Atom {0} is defined more than once for state {1}"
                                , atom, stateName));
                }

                State stateObj = FindStateByName(stateName);
                if (stateObj == null)
                    throw new IllegalArgumentException(String.format("State {0} is not defined", stateName));
                stateObj.atoms = stateAtoms;

                //load to list of atoms
                for (String atom : stateAtoms) {
                    if (!atoms.contains(atom))
                        atoms.add(atom);
                }
            }
        } catch (IllegalArgumentException fe) {
            throw fe;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid kripke input structure");
        }
    }

    /// <summary>
    /// Find the state by its name
    /// </summary>
    /// <param name="stateName"></param>
    /// <returns></returns>
    public State FindStateByName(String stateName) {
        for (State state : states) {
            if (state.stateName.equals(stateName))
                return state;
        }

        return null;
    }

    /// <summary>
    /// Override ToString method
    /// </summary>
    /// <returns></returns>
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("STATES\n");
        sb.append("-----------\n");
        sb.append(StatesToString());
        sb.append("\n");
        sb.append("\n");
        sb.append("TRANSITIONS\n");
        sb.append("-------------------\n");
        sb.append(transitionsToString());

        return sb.toString();
    }

    public String StatesToString() {
        StringBuilder sb = new StringBuilder();

        List<String> stateStrings = new ArrayList<String>();
        for (State state : states) {
            String atomNames = String.join(", ", state.atoms);
            stateStrings.add(String.format("{0}({1})", state.stateName, atomNames));
        }

        sb.append(String.join(", ", stateStrings));
        return sb.toString();
    }

    public String transitionsToString() {
        StringBuilder sb = new StringBuilder();

        List<String> transitionString = new ArrayList<String>();
        for (Transition transition : transitions) {
            transitionString.add(String.format("{0}({1} -> {2})", transition.transitionName
                    , transition.fromState.stateName, transition.toState.stateName));
        }

        sb.append(String.join(", ", transitionString));
        return sb.toString();
    }
}

