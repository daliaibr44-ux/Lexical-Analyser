import java.util.*;

public class DFAState {
    private final Set<NFAState> nfaStates;
    private final Map<Character, DFAState> trans = new HashMap<>();
    private boolean accept = false;
    private String tokenType = null;

    public DFAState(Set<NFAState> nfaStates) {
        this.nfaStates = new HashSet<>(nfaStates);
    }

    public void addTransition(char symbol, DFAState state) { trans.put(symbol, state); }

    public DFAState getTransition(char symbol) { return trans.get(symbol); }

    public void setAccept(boolean accept) { this.accept = accept; }

    public boolean isAccept() { return accept; }

    public void setTokenType(String type) { this.tokenType = type; }

    public String getTokenType() { return tokenType; }

    public Set<NFAState> getNFAStates() { return Collections.unmodifiableSet(nfaStates); }
}
