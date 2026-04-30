import java.util.*;

public class NFAToDFAConverter {

    public static DFA convert(NFA nfa) {
        Map<Set<NFAState>, Integer> stateIds = new HashMap<>();
        List<Set<NFAState>> states = new ArrayList<>();
        Queue<Set<NFAState>> q = new ArrayDeque<>();

        Set<NFAState> start = epsilonClosure(Collections.singleton(nfa.getStartState()));
        states.add(start);
        stateIds.put(start, 0);
        q.add(start);

        DFA dfa = new DFA();

        while (!q.isEmpty()) {
            Set<NFAState> cur = q.poll();
            int curId = stateIds.get(cur);
            Map<Character, Set<NFAState>> moves = new HashMap<>();

            for (NFAState s : cur) {
                for (NFAState t : s.getTransitions('\0')) {
                    // no-op: placeholder for symbol-less listing
                }
                // iterate over possible transitions by inspecting all keys is not available;
                // instead, infer by checking ASCII range for common symbols (simple approach)
                for (char c = 32; c < 127; c++) {
                    for (NFAState t : s.getTransitions(c)) {
                        moves.computeIfAbsent(c, k -> new HashSet<>()).add(t);
                    }
                }
            }

            for (Map.Entry<Character, Set<NFAState>> mv : moves.entrySet()) {
                char c = mv.getKey();
                Set<NFAState> target = epsilonClosure(mv.getValue());
                Integer id = stateIds.get(target);
                if (id == null) {
                    id = states.size();
                    states.add(target);
                    stateIds.put(target, id);
                    q.add(target);
                }
                dfa.addTransition(curId, c, id);
            }
        }

        // mark accept states
        for (int i = 0; i < states.size(); i++) {
            for (NFAState s : states.get(i)) {
                if (s.isAccept()) dfa.addAccept(i);
            }
        }

        dfa.setStart(0);
        return dfa;
    }

    private static Set<NFAState> epsilonClosure(Collection<NFAState> set) {
        Set<NFAState> res = new HashSet<>();
        Deque<NFAState> stack = new ArrayDeque<>(set);
        while (!stack.isEmpty()) {
            NFAState s = stack.pop();
            if (!res.add(s)) continue;
            for (NFAState t : s.getEpsilonTransitions()) stack.push(t);
        }
        return res;
    }
}
