import java.util.*;

public class DFALexer {
    private final DFA dfa;
    private String input = "";
    private int pos = 0;

    public DFALexer(DFA dfa) { this.dfa = dfa; }

    public List<Token> tokenize(String input) {
        this.input = input;
        this.pos = 0;
        List<Token> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            skipWhitespace();
            if (isAtEnd()) break;
            Token t = nextToken();
            if (t != null) tokens.add(t);
            else {
                // unknown char, consume one
                advance();
            }
        }
        return tokens;
    }

    // Attempts to find the longest matching token starting at current pos
    private Token nextToken() {
        DFAState start = dfa.getStartState();
        DFAState cur = start;
        int idx = pos;
        int lastAcceptPos = -1;
        DFAState lastAcceptState = null;

        while (idx < input.length() && cur != null) {
            char c = input.charAt(idx);
            cur = dfa.transition(cur, c);
            if (cur != null && cur.isAccept()) {
                lastAcceptPos = idx;
                lastAcceptState = cur;
            }
            idx++;
        }

        if (lastAcceptPos >= pos) {
            String lexeme = input.substring(pos, lastAcceptPos + 1);
            pos = lastAcceptPos + 1;
            String type = lastAcceptState != null ? lastAcceptState.getTokenType() : "TOKEN";
            return new Token(type, lexeme);
        }
        return null;
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) advance();
    }

    private boolean isAtEnd() { return pos >= input.length(); }

    private char peek() { return input.charAt(pos); }

    private char advance() { return input.charAt(pos++); }

    private void retract() { if (pos > 0) pos--; }
}
