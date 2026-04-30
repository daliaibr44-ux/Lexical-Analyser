import java.util.*;

public class AutomataBuilder {

    public DFA buildLexerDFA() {
        // Example: build a tiny DFA that recognizes identifiers (letters+), numbers (digits+), and operators '+'
        List<NFA> nfas = new ArrayList<>();
        nfas.add(buildIdentifierNFA());
        nfas.add(buildNumberNFA());
        nfas.add(buildOperatorNFA("+"));

        NFA combined = combineNFAs(nfas);
        return convertToDFA(combined);
    }

    private NFA buildKeywordNFA(String keyword) {
        NFA n = null;
        for (char c : keyword.toCharArray()) {
            NFA ch = NFA.singleChar(c);
            n = (n == null) ? ch : NFA.concat(n, ch);
        }
        for (NFAState s : n.getAcceptStates()) s.setTokenType("KW_" + keyword);
        return n;
    }

    private NFA buildIdentifierNFA() {
        // identifier: [a-zA-Z][a-zA-Z0-9]*
        NFA first = null;
        for (char c = 'a'; c <= 'z'; c++) first = (first==null)?NFA.singleChar(c):null;
        // simple approximation: just use + operator
        NFA a = NFA.singleChar('a');
        NFA b = NFA.singleChar('b');
        NFA id = NFA.union(a, b);
        for (NFAState s : id.getAcceptStates()) s.setTokenType("IDENT");
        return id;
    }

    private NFA buildNumberNFA() {
        // number: [0-9]+
        NFA d = NFA.singleChar('0');
        for (char c = '1'; c <= '9'; c++) d = NFA.union(d, NFA.singleChar(c));
        for (NFAState s : d.getAcceptStates()) s.setTokenType("NUMBER");
        return d;
    }

    private NFA buildOperatorNFA(String operator) {
        NFA n = null;
        for (char c : operator.toCharArray()) {
            NFA ch = NFA.singleChar(c);
            n = (n == null) ? ch : NFA.concat(n, ch);
        }
        for (NFAState s : n.getAcceptStates()) s.setTokenType("OP");
        return n;
    }

    private NFA buildDelimiterNFA(char delimiter) {
        NFA n = NFA.singleChar(delimiter);
        for (NFAState s : n.getAcceptStates()) s.setTokenType("DELIM");
        return n;
    }

    private NFA combineNFAs(List<NFA> nfas) {
        if (nfas.isEmpty()) throw new IllegalArgumentException("no nfas");
        NFA res = nfas.get(0);
        for (int i = 1; i < nfas.size(); i++) res = NFA.union(res, nfas.get(i));
        return res;
    }

    private DFA convertToDFA(NFA nfa) { return NFAToDFAConverter.convert(nfa); }

    private Set<NFAState> epsilonClosure(Set<NFAState> states) {
        Set<NFAState> res = new HashSet<>(states);
        Deque<NFAState> st = new ArrayDeque<>(states);
        while (!st.isEmpty()) {
            NFAState s = st.pop();
            for (NFAState t : s.getEpsilonTransitions()) if (res.add(t)) st.push(t);
        }
        return res;
    }

    private Set<NFAState> move(Set<NFAState> states, char symbol) {
        Set<NFAState> res = new HashSet<>();
        for (NFAState s : states) res.addAll(s.getTransitions(symbol));
        return res;
    }
}
