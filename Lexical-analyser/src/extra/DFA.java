import java.util.*;

public class DFA {
    private final Map<Integer, Map<Character, Integer>> trans = new HashMap<>();
    private final Set<Integer> accept = new HashSet<>();
    private int start = -1;

    // mapping between DFAState objects and internal ids
    private final Map<DFAState, Integer> stateIds = new IdentityHashMap<>();
    private final List<DFAState> idToState = new ArrayList<>();

    public void setStart(int s) { this.start = s; }
    public int getStart() { return start; }

    public void addTransition(int from, char c, int to) {
        trans.computeIfAbsent(from, k -> new HashMap<>()).put(c, to);
    }

    public Integer move(int state, char c) {
        Map<Character, Integer> m = trans.get(state);
        if (m == null) return null;
        return m.get(c);
    }

    public void addAccept(int s) { accept.add(s); }

    public boolean isAccept(int s) { return accept.contains(s); }

    public Set<Integer> allStates() { return trans.keySet(); }

    // Spec constructor: build internal int-based DFA from a DFAState graph
    public DFA(DFAState startState) {
        if (startState == null) return;
        Queue<DFAState> q = new ArrayDeque<>();
        q.add(startState);
        addState(startState);
        while (!q.isEmpty()) {
            DFAState s = q.poll();
            int sid = stateIds.get(s);
            for (char c = 32; c < 127; c++) {
                DFAState t = s.getTransition(c);
                if (t != null) {
                    if (!stateIds.containsKey(t)) { addState(t); q.add(t); }
                    addTransition(sid, c, stateIds.get(t));
                }
            }
            if (s.isAccept()) addAccept(sid);
        }
        this.start = stateIds.get(startState);
    }

    public DFA() { }

    public DFAState getStartState() {
        return start >= 0 && start < idToState.size() ? idToState.get(start) : null;
    }

    public DFAState transition(DFAState state, char input) {
        Integer id = stateIds.get(state);
        if (id == null) return null;
        Integer nid = move(id, input);
        return nid == null ? null : idToState.get(nid);
    }

    public void addState(DFAState state) {
        if (stateIds.containsKey(state)) return;
        int id = idToState.size();
        stateIds.put(state, id);
        idToState.add(state);
    }

    public Set<DFAState> getStates() { return Collections.unmodifiableSet(stateIds.keySet()); }
}
