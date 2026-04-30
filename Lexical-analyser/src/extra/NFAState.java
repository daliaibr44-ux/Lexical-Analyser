import java.util.*;

public class NFAState {
    private static int NEXT_ID = 0;
    private final int id;
    private final Map<Character, List<NFAState>> trans = new HashMap<>();
    private final List<NFAState> eps = new ArrayList<>();
    private boolean accept = false;
    private String tokenType = null;

    public NFAState() { this.id = NEXT_ID++; }

    public void addTransition(char symbol, NFAState state) {
        trans.computeIfAbsent(symbol, k -> new ArrayList<>()).add(state);
    }

    public void addEpsilonTransition(NFAState state) { eps.add(state); }

    public List<NFAState> getTransitions(char symbol) {
        return trans.getOrDefault(symbol, Collections.emptyList());
    }

    public List<NFAState> getEpsilonTransitions() { return Collections.unmodifiableList(eps); }

    public void setAccept(boolean accept) { this.accept = accept; }

    public boolean isAccept() { return accept; }

    public void setTokenType(String type) { this.tokenType = type; }

    public String getTokenType() { return tokenType; }
}
