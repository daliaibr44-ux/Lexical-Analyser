import java.util.*;

public class RegexToNFA {

    // Convert a regex to an NFA using Thompson's construction.
    // Supports: literal chars, concatenation, union '|', Kleene star '*', and parentheses.
    public static NFA fromRegex(String regex) {
        String withConcat = addConcat(regex);
        String postfix = toPostfix(withConcat);
        Stack<NFA> st = new Stack<>();

        for (char c : postfix.toCharArray()) {
            if (c == '.') {
                NFA b = st.pop();
                NFA a = st.pop();
                st.push(NFA.concat(a, b));
            } else if (c == '|') {
                NFA b = st.pop();
                NFA a = st.pop();
                st.push(NFA.union(a, b));
            } else if (c == '*') {
                NFA a = st.pop();
                st.push(NFA.star(a));
            } else {
                st.push(NFA.singleChar(c));
            }
        }

        if (st.isEmpty()) throw new IllegalArgumentException("Invalid regex");
        return st.pop();
    }

    private static String addConcat(String s) {
        StringBuilder out = new StringBuilder();
        String ops = "|";
        for (int i = 0; i < s.length(); i++) {
            char c1 = s.charAt(i);
            out.append(c1);
            if (i + 1 < s.length()) {
                char c2 = s.charAt(i + 1);
                if (c1 != '(' && c2 != ')' && c2 != '|' && c1 != '|') {
                    out.append('.');
                }
            }
        }
        return out.toString();
    }

    // Shunting-yard to postfix
    private static String toPostfix(String s) {
        StringBuilder out = new StringBuilder();
        Stack<Character> st = new Stack<>();
        Map<Character, Integer> prec = new HashMap<>();
        prec.put('|', 1);
        prec.put('.', 2);
        prec.put('*', 3);

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                st.push(c);
            } else if (c == ')') {
                while (!st.isEmpty() && st.peek() != '(') out.append(st.pop());
                if (!st.isEmpty() && st.peek() == '(') st.pop();
            } else if (prec.containsKey(c)) {
                while (!st.isEmpty() && prec.containsKey(st.peek()) && prec.get(st.peek()) >= prec.get(c)) {
                    out.append(st.pop());
                }
                st.push(c);
            } else {
                out.append(c);
            }
        }
        while (!st.isEmpty()) out.append(st.pop());
        return out.toString();
    }
}
