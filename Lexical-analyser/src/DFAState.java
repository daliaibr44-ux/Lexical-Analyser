
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DFAState {

    private Set<NFAState> NFAStates;
    private boolean accept;
    private String tokenType;
    private Map<Character, DFAState> transition;

    public DFAState(Set<NFAState> nfaStates) {
        // set accept var and tokenType ?? 
        NFAStates = nfaStates;
        transition = new HashMap<Character, DFAState>();
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

    public void setTokenType(String type) {
        tokenType = type;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Set<NFAState> getNFAStates() {
        return NFAStates;
    }

}
