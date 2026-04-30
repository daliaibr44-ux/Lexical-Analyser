public class DFASimulator {
    public static boolean matches(DFA dfa, String input) {
        int state = dfa.getStart();
        for (char c : input.toCharArray()) {
            Integer ns = dfa.move(state, c);
            if (ns == null) return false;
            state = ns;
        }
        return dfa.isAccept(state);
    }
}
