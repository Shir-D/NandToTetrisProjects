// Imports:
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 *  Parser Class:
 *  Encapsulates access to the input code. Reads an assembly program
 *  by reading each instruction line-by-line, parses the current command,
 *  and provides convenient access to the commands components (fields
 *  and symbols). In addition, removes all white space and comments.
 */
public class Parser {

    // Constants:
    public static final String A_INSTRUCTION = "A_Instruction";
    public static final String L_INSTRUCTION = "L_Instruction";
    private static final String C_INSTRUCTION = "C_Instruction";
    private static final String EMPTY_STR = "";

    // Fields:
    private final BufferedReader bufferedReader;
    private String currentLine = null;
    private String nextLine;

    /**
     * Constructor
     * Creates a Parser object and opens the source text file
     * @throws IOException (If an I/O error occurs while reading the file)
     */
    public Parser (File inputFile) throws IOException {

        this.bufferedReader = new BufferedReader(new FileReader(inputFile));
        this.nextLine = getNextLine();
    }

    /**
     * Gets next line we want to parse
     * @return the next line
     * @throws IOException (If an I/O error occurs while reading the file)
     */
    private String getNextLine() throws IOException {

        while (true) {
            // Read the next line
            String line = bufferedReader.readLine();
            // If null - we reached the EOF, return null
            if (line == null) {
                return null;
            } else {
                // If it's not a white space line or a comment - return the line.
                // else - continue to the next line
                if (!whiteSpaceOrCommentLine(line)) {
                    return trimLine(line);
                }
            }
        }
    }

    /**
     * Check if a given line is a white-spaces-only line or a comment line
     * @param line - line to check
     * @return true if it's a white-spaces-only line or a comment line, false otherwise
     */
    private boolean whiteSpaceOrCommentLine(String line) {
        line = line.trim();
        return ((line.length() == 0) || (line.startsWith("//")));
    }

    /**
     * Removes unnecessary white spaces and in-line comments from a given String
     * @param line - line to trim
     * @return trimmed line
     */
    private String trimLine (String line) {

        // Remove unnecessary white spaces
        line = line.replaceAll("\\s+", "");

        // Remove in-line comments, if exist
        if (line.contains("//")) {
            StringBuilder cLine = new StringBuilder(line);
            cLine.delete(line.indexOf("//"), line.length());
            line = cLine.toString();
        }
        return line;
    }

    /**
     * Checks if we're finished
     * @return True if there are more instructions, False otherwise
     */
    public boolean hasMoreLines() {
        return (nextLine != null);
    }

    /**
     * Reads the next instruction from the input and makes it the current line
     * Should be called only if hasMoreLines() is true
     * @throws IOException (If an I/O error occurs while reading the file in "getNextLine" method)
     */
    public void advance() throws IOException {
        currentLine = this.nextLine;
        nextLine = this.getNextLine();
    }

    /**
     * Closes the input file when we're finished
     * @throws IOException (If an I/O error occurs while closing the file)
     */
    public void closeInputFile () throws IOException {
        bufferedReader.close();
    }

    /**
     * Gets the instruction type
     * @return  A_INSTRUCTION: for @Xxx where Xxx is either a symbol or a decimal number
     *          L_INSTRUCTION: for (Xxx) where Xxx is a symbol
     *          C_INSTRUCTION: for dest=comp;jump
     */
    public String instructionType () {
        if (currentLine.startsWith("@")) {
            return A_INSTRUCTION;
        } else if (currentLine.startsWith("(")) {
            return L_INSTRUCTION;
        }
        return C_INSTRUCTION;
    }

    /**
     * Should be called only when instructionType() is "A_Instruction" or "L_Instruction"
     * @return the symbol or decimal Xxx of the current instruction @Xxx or (Xxx) as a String
     */
    public String symbol() {
        if (currentLine.startsWith("@")) {
            // If it starts with a "@" - it's a number (an explicit address) OR a variable name (or a use of
            // already-declared label) --> return the number \ variable as is, without the "@"
            return currentLine.substring(1);

        // Else, it's a label declaration of the form: (LABEL) --> return the label name
        } else if (currentLine.startsWith("(")) {
            return currentLine.substring(1, currentLine.length()-1);
        }
        return EMPTY_STR;
    }

    /**
     * Should be called only when instructionType() is "C_Instruction"
     * @return the dest mnemonic in the current C instruction
     */
    public String dest() {
        if (currentLine.contains("=")) {
            return currentLine.substring(0, currentLine.indexOf("="));
        }
        // Else - there's no dest
        return EMPTY_STR;
    }

    /**
     * Should be called only when instructionType() is "C_Instruction"
     * @return the comp mnemonic in the current C instruction
     */
    public String comp() {
        if (currentLine.contains("=")) {
            if (currentLine.contains(";")) {
                return currentLine.substring(currentLine.indexOf("=")+1, currentLine.indexOf(";"));
            }
            return currentLine.substring(currentLine.indexOf("=")+1);
        } else if (currentLine.contains(";")) {
            return currentLine.substring(0, currentLine.indexOf(";"));
        }
        return EMPTY_STR;
    }

    /**
     * Should be called only when instructionType() is "C_Instruction"
     * @return the jump mnemonic in the current C instruction
     */
    public String jump() {
        if (currentLine.contains(";")) {
            return currentLine.substring(currentLine.indexOf(";")+1);
        }
        // Else - there's no jump condition
        return EMPTY_STR;
    }
}
