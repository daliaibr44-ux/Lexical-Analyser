import java.util.*;

public class NFA {
    private final NFAState start;
    private final Set<NFAState> acceptStates;
    private String tokenType = null;

    // New constructor per spec
    public NFA(NFAState start, Set<NFAState> acceptStates) {
        this.start = start;
        this.acceptStates = new HashSet<>(acceptStates);
        for (NFAState s : this.acceptStates) s.setAccept(true);
    }

    // Backwards-compatible helpers (Thompson-style)
    public static NFA singleChar(char c) {
        NFAState s = new NFAState();
        NFAState e = new NFAState();
        s.addTransition(c, e);
        return new NFA(s, Collections.singleton(e));
    }

    public static NFA concat(NFA a, NFA b) {
        for (NFAState as : a.acceptStates) {
            as.addEpsilonTransition(b.start);
            as.setAccept(false);
        }
        return new NFA(a.start, b.acceptStates);
    }

    public static NFA union(NFA a, NFA b) {
        NFAState s = new NFAState();
        NFAState e = new NFAState();
        s.addEpsilonTransition(a.start);
        s.addEpsilonTransition(b.start);
        for (NFAState as : a.acceptStates) { as.addEpsilonTransition(e); as.setAccept(false); }
        for (NFAState bs : b.acceptStates) { bs.addEpsilonTransition(e); bs.setAccept(false); }
        return new NFA(s, Collections.singleton(e));
    }

    public static NFA star(NFA a) {
        NFAState s = new NFAState();
        NFAState e = new NFAState();
        s.addEpsilonTransition(a.start);
        s.addEpsilonTransition(e);
        for (NFAState as : a.acceptStates) { as.addEpsilonTransition(a.start); as.addEpsilonTransition(e); as.setAccept(false); }
        return new NFA(s, Collections.singleton(e));
    }

    // Spec methods
    public void addTransition(NFAState from, char symbol, NFAState to) { from.addTransition(symbol, to); }

    public void addEpsilonTransition(NFAState from, NFAState to) { from.addEpsilonTransition(to); }

    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public NFAState getStartState() { return start; }

    public Set<NFAState> getAcceptStates() { return Collections.unmodifiableSet(acceptStates); }
}
