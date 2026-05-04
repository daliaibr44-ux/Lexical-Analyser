
public class DFALexer {

    private DFA dfa;

    public DFALexer(DFA dfa) {
        this.dfa = dfa;
    }

    public void scan(String input) {//scan from user and analysis.
        int i = 0;
        int line = 1;
        int column = 1;

        System.out.println("Lexeme\tToken\tPosition");//Table title.

        while (i < input.length()) {//if not reached in thee end of the text coomplete scan

            if (Character.isWhitespace(input.charAt(i))) {//if we have space or new line
                if (input.charAt(i) == '\n') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }
                i++;
                continue;
            }

            int start = i;//start new token
            int startColumn = column;

            DFAState current = dfa.getStartState();//start state of dfa
            DFAState lastAccept = null;//this is save the last state of dfa because long maatch

            int lastAcceptIndex = -1;//last state accept
            int lastAcceptCol = column;

            while (i < input.length()) {//scan into dfa
                char Character = input.charAt(i);

                DFAState next = dfa.transition(current, Character);

                if (next == null) {//if not has transition for this character
                    break;
                }

                current = next;//state new
                i++;
                column++;

                if (current.isAccept()) {//if this state is accept may be correct token,and will save
                    lastAccept = current;
                    lastAcceptIndex = i;
                    lastAcceptCol = column;
                }
            }

            if (lastAccept != null) {
                String lexeme = input.substring(start, lastAcceptIndex);//cut the part from the text

                System.out.println(
                        "'" + lexeme + "'\t" +
                        lastAccept.getTokenType() +
                        "\tLine " + line + ", column " + startColumn
                );

                i = lastAcceptIndex;
                column = lastAcceptCol;

            } else {
                System.out.println(
                        "Lexing is Error at in'" + input.charAt(start) +
                        "' Line " + line + ", column " + startColumn
                );
                return;
            }
        }

        System.out.println("Lexing is Completed :)");
    }
}