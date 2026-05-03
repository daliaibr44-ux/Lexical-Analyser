
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.queue;

public class AutomataBuilder {

    public DFA buildLexerDFA()

    private NFA buildKeywordNFA(String keyword)

    private NFA buildIdentifierNFA()

    private NFA buildNumberNFA()

    private NFA buildOperatorNFA(String operator)

    private NFA buildDelimiterNFA(char delimiter)

    private NFA combineNFAs(List<NFA> nfas) {

        // Set of accept states for the new combined NFA
        Set<NFAState> newAcceptStates = new HashSet<NFAState>();

        // New start state that will point to each NFA
        NFAState newStartState = new NFAState();

        // Create the resulting NFA with the new start and accept states
        NFA newNFA = new NFA(newStartState, newAcceptStates);

        // For each input NFA:
        for (NFA nfa : nfas) {

            // Add all its accept states to the new NFA
            newAcceptStates.addAll(nfa.getAcceptStates());

            // Add epsilon transition from new start state to this NFA's start state
            newStartState.addEpsilonTransition(nfa.getStartState());
        }

        return newNFA;
    }

    private DFA convertToDFA(NFA nfa) {
        // Alphabet of the automaton (input symbols)
        Set<Character> alphabet = nfa.getAlphabet();

        // Compute ε-closure of the NFA start state → DFA start state
        Set<NFAState> startSubset = epsilonClosure(Set.of(nfa.getStartState()));
        DFAState startState = new DFAState(startSubset);

        // Initialize DFA with the start state
        DFA dfa = new DFA(startState);

        // Maps each subset of NFA states → corresponding DFA state
        Map<Set<NFAState>, DFAState> stateMap = new HashMap<Set<NFAState>, DFAState>();

        // Queue for BFS traversal over subsets (DFA states)
        Queue<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();

        // Initialize processing with the start subset
        stateMap.put(startSubset, startState);
        queue.add(startSubset);

        // Process each discovered subset (a new DFA state)
        while (!queue.isEmpty()) {
            // Get next subset to process
            Set<NFAState> currentSet = queue.poll();
            DFAState currentState = stateMap.get(currentSet);

            // For each input symbol, compute the next subset
            for (char symbol : alphabet) {

                // move(currentSet, symbol): all states reachable from currentSet via 'symbol' 
                // epsilonClosure(...): include all states reachable via epsilon transitions afterward
                Set<NFAState> nextSet = epsilonClosure(move(currentSet, symbol));

                // Check if this subset has already been created as a DFA state
                DFAState nextState = stateMap.get(nextSet);

                if (nextState == null) {
                    // Create a new DFA state for this subset
                    nextState = new DFAState(nextSet);

                    // A DFA state is accepting if ANY NFA state in the subset is an accepting state.
                    boolean accept = false;
                    for (NFAState state : nextSet) {
                        if (state.isAccept()) {
                            accept = true;
                            break;
                        }
                    }
                    nextState.setAccept(accept);

                    // Add new state to DFA and tracking structures
                    dfa.addState(nextState);
                    stateMap.put(nextSet, nextState);
                    queue.add(nextSet);
                }

                // Add transition: currentState --symbol--> nextState
                currentState.addTransition(symbol, nextState);
            }
        }

        return dfa;
    }

    private Set<NFAState> epsilonClosure(Set<NFAState> states) {

        // Stores all states in the epsilon closure
        Set<NFAState> closure = new HashSet<NFAState>();

        // Queue for BFS traversal of epsilon transitions
        Queue<NFAState> queue = new LinkedList<NFAState>();

        // Initialize closure and queue with the starting states
        for (NFAState state : states) {
            queue.add(state);
            closure.add(state);
        }

        // Explore epsilon transitions until no new states are found
        while (!queue.isEmpty()) {

            // Get the next state to process
            NFAState current = queue.poll();

            // Get all states reachable via epsilon transitions from current
            Set<NFAState> nextSet = current.getEpsilonTransitions();

            // If there are epsilon transitions, process them
            if (nextSet != null) {
                for (NFAState next : nextSet) {

                    // Add unseen states to closure and continue exploration
                    if (!closure.contains(next)) {
                        closure.add(next);
                        queue.add(next);
                    }
                }
            }
        }

        return closure;
    }

    private Set<NFAState> move(Set<NFAState> states, char symbol) {

        // Stores all states reachable via 'symbol'
        Set<NFAState> reachableStates = new HashSet<NFAState>();

        // For each state, follow transitions labeled with the symbol
        for (NFAState state : states) {

            // Get all transitions from this state on the given symbol
            reachableStates.addAll(state.getTransitions(symbol));

        }

        return reachableStates;
    }

}
