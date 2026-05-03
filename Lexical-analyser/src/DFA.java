
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFA {

    private DFAState startState;
    private Set<DFAState> states;
    Set<Character> alphabet;

    public DFA(DFAState startState) {
        alphabet = new HashSet<Character>();
        states = new HashSet<DFAState>();
        
        this.startState = startState;
        states.add(startState);
        
    }

    public DFAState getStartState() {
        return startState;
    }

    public DFAState transition(DFAState state, char input) {
        return state.getTransition(input);
    }

    public void addState(DFAState state) {
        states.add(state);
    }

    public Set<DFAState> getStates() {
        return states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }


}
