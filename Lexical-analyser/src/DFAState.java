import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFAState {

    private Set<NFAState> nfaStates;
    private boolean accept;
    private Map<Character, DFAState> transition;

    public DFAState(Set<NFAState> nfaStates) {
        this.nfaStates = new HashSet<>(nfaStates); // defensive copy
        this.accept = false;
        this.transition = new HashMap<>();
    }

    public void addTransition(char symbol, DFAState state) {
        transition.put(symbol, state);
    }

    public DFAState getTransition(char symbol) {
        return transition.get(symbol);
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public boolean isAccept() {
        return accept;
    }

    public Set<NFAState> getNFAStates() {
        return nfaStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DFAState)) return false;
        DFAState other = (DFAState) o;
        return nfaStates.equals(other.nfaStates);
    }

    @Override
    public int hashCode() {
        return nfaStates.hashCode();
    }
}