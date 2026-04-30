import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Lexical analyser demo: regex -> NFA -> DFA -> simulate");

        String regex = "(a|b)*abb"; // example: strings ending with 'abb'
        System.out.println("Regex: " + regex);

        NFA nfa = RegexToNFA.fromRegex(regex);
        DFA dfa = NFAToDFAConverter.convert(nfa);

        String[] tests = {"abb", "aabb", "ababb", "ab", "aab"};
        for (String t : tests) {
            System.out.printf("%s -> %s%n", t, DFASimulator.matches(dfa, t));
        }

        // simple interactive lexer demo (uses AutomataBuilder)
        AutomataBuilder builder = new AutomataBuilder();
        DFA lexerDfa = builder.buildLexerDFA();
        DFALexer lexer = new DFALexer(lexerDfa);
        String input = readInput();
        List<Token> tokens = lexer.tokenize(input);
        printTokens(tokens);
    }

    private static String readInput() {
        System.out.println("Enter input to tokenize (single line):");
        Scanner sc = new Scanner(System.in);
        String line = sc.hasNextLine() ? sc.nextLine() : "";
        return line;
    }

    private static void printTokens(List<Token> tokens) {
        for (Token t : tokens) System.out.println(t);
    }
}
