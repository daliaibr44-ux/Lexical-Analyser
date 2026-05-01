
import java.util.Set;

public class DFA {

    private DFAState startState;
    private Set<DFAState> states;

    public DFA(DFAState startState) {
        this.startState = startState;
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

}
