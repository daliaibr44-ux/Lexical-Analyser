
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DFA {

    private DFAState startState;
    private Set<DFAState> states;
    private Map<DFAState, Set<NFAState> > stateMap ;

    public DFA(DFAState startState) {
        this.startState = startState;
        stateMap = new HashMap<DFAState, Set<NFAState> >();
        stateMap.put(startState, startState.getNFAStates());
    }

    public DFAState getStartState() {
        return startState;
    }

    public DFAState transition(DFAState state, char input) {
        return state.getTransition(input);
    }

    public void addState(DFAState state) {
        states.add(state);
        stateMap.put(state, state.getNFAStates());
    }

    public Set<DFAState> getStates() {
        return states;
    }

    public Map<DFAState, Set<NFAState> > getMap(){
        return stateMap;
    }

}
