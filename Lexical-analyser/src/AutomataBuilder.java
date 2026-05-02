
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class AutomataBuilder {

    public DFA buildLexerDFA()

    private NFA buildKeywordNFA(String keyword)

    private NFA buildIdentifierNFA()

    private NFA buildNumberNFA()

    private NFA buildOperatorNFA(String operator)

    private NFA buildDelimiterNFA(char delimiter)

    private NFA combineNFAs(List<NFA> nfas) {
        Set<NFAStates> newAccepStates = new HashSet<NFAStates>();
        NFAState newStartState = new NFAState();

        for (Object nfa : nfas) {
            newAccepStates.addAll(nfa.getAcceptStates());
        }

        NFA newNFA = new NFA(newStartState, newAccepStates);

        for (Object nfa : nfas) {
            NFAState s = nfa.getStartState();
            newStartState.addEpsilonTransition(s);
            newNFA.addEpsilonTransition(newStartState, s);
        }
        return newNFA;

    }

    private DFA convertToDFA(NFA nfa) {
        Set<NFAState> nfaStart = new HashSet<NFAState>();
        String tokens = nfaStart.getTokenType();

        nfaStart.add(epsilonClosure(nfa.starState));

        DFAState dfaStart = new DFAState(nfaStart);
        DFA dfa = new DFA(dfaStart);

        Queue<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();
        Set<NFAState> currentSubset;

        queue.add(nfaStart);

        while (!queue.isEmpty()) {
            currentSubset = queue.poll();
            DFAState currentDFAState = new DFAState(currentSubset);
            dfa.addState(currentDFAState);

            for (Character symbol : tokens) {

                Set<NFAState> nextSubset = epsilonClosure(move(currentDFAState.getNFAStates(), symbol));

                if (!dfa.getMap().containsValue(nextSubset)) {
                    queue.add(nextSubset);
                }
            }

        }

        return dfa;

    }

    private Set<NFAState> epsilonClosure(Set<NFAState> states) {

        Set<NFAState> newStates = new HashSet<NFAStates>();
        Stack<NFAState> stack = new Stack<NFAState>();

        for (NFAState state : states) {
            stack.add(state);
            newStates.add(state);
        }
        while (!stack.empty()) {
            NFAState current = stack.pop();
            for (NFAState next : current.getEpsilonTransitions()) {
                if (!newStates.contains(next)) {
                    newStates.add(next);
                    stack.add(next);
                }
            }
        }

        return newStates;
    }

    private Set<NFAState> move(Set<NFAState> states, char symbol) {
        Set<NFAState> newStates = new HashSet<NFAState>();

        for (NFAState state : states) {
            List<NFAState> list = state.getTransitions(symbol);
            for (NFAState elem : list) {
                newStates.add(elem);
            }
        }
        return newStates;
    }

}
