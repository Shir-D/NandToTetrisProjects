// Imports:
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.toBinaryString;

/**
 * HackAssembler Class:
 * Parses the instruction in a given file(s), translates it to a binary code that holds the same
 * instructions' meaning, pads the instruction, so they all be 16-bit length, and then writes it
 * to the output file(s)
 */
public class HackAssembler {

    // Constants:
    private static final int DATA_MEMORY_START_ADDRESS = 16;

    // Fields:
    BufferedWriter outputFileBuffer;
    Parser firstPassParser;
    Parser secondPassParser;
    Parser parser;
    SymbolTable symbolTable;
    Code code;

    /**
     * Constructor
     * @param inputFile - contains the symbolic code that need to be translated
     * @param outputFile - the target file in which the translation to binary code will be written
     * @throws IOException (If an I/O error occurs while creating FileWriter)
     */
    public HackAssembler (File inputFile, File outputFile) throws IOException {

        // Create buffer to output file
        FileWriter fileWriter = new FileWriter (outputFile);
        this.outputFileBuffer = new BufferedWriter (fileWriter);

        // Create three Parsers - two for the two first passes and another one for the "real" parsing
        this.firstPassParser = new Parser(inputFile);
        this.secondPassParser = new Parser(inputFile);
        this.parser = new Parser(inputFile);

        // Create a SymbolTable and a Code
        this.symbolTable = new SymbolTable();
        this.code = new Code();
    }

    /**
     * Fulfill the HackAssembler mission: (1) parse and translate the symbolic code written in the input file
     *                                    (2) write the translated code to the output file
     * @throws IOException (If an I/O error occurs while reading the file in "advance" method)
     */
    public void assemble() throws IOException {

        // Preparations:
        firstPass();
        secondPass();

        // Real translation from symbolic to binary:
        while (parser.hasMoreLines()) {

            // Advance to the next line
            parser.advance();

            // Parse the current instruction and translate it to binary code
            String currentInstruction = parse();

            // Write it to the output file
            if (currentInstruction != null) {
                writeToOutputFile (currentInstruction);
            }
        }

        // Close files
        parser.closeInputFile();
        outputFileBuffer.close();
    }

    /**
     * First pass on the input file symbolic code, to insert all labels and their addresses to the Symbol Table
     * @throws IOException (If an I/O error occurs while reading the file in "advance" method)
     */
    private void firstPass () throws IOException {
        int linesCounter = -1;
        while (firstPassParser.hasMoreLines()) {

            // Advance to the next line
            firstPassParser.advance();

            // If we reached a label declaration, insert it to the Symbol Table
            if (firstPassParser.instructionType().equals(Parser.L_INSTRUCTION)) {
                symbolTable.addEntry(firstPassParser.symbol(), linesCounter+1);
            } else { // Lines counter skips on lines that are label declaration (They aren't going to appear in
                     // the final translated file
                linesCounter++;
            }
        }
    }

    /**
     * First pass on the input file symbolic code, to insert all variables and their addresses to the Symbol Table
     * @throws IOException (If an I/O error occurs while reading the file in "advance" method)
     */
    private void secondPass () throws IOException {

        int varAddressCounter = DATA_MEMORY_START_ADDRESS;

        while (secondPassParser.hasMoreLines()) {

            // Advance to the next line
            secondPassParser.advance();

            // If we reached a variable declaration, insert it to the Symbol Table
            if (secondPassParser.instructionType().equals(Parser.A_INSTRUCTION)) {
                String currentVar = secondPassParser.symbol();

                // the variable isn't in the table AND it's not a number --> only in this case we should insert it
                if (!symbolTable.contains(currentVar) && !Character.isDigit(currentVar.charAt(0))) {
                    symbolTable.addEntry(currentVar, varAddressCounter);
                    varAddressCounter++;
                }
            }
        }
    }

    /**
     * Parse the current instruction and translate it to a 16-bit-long binary code
     * (ignores lines that are label declaration)
     * @return the parsed and translated instruction
     */
    private String parse () {

        String instructionType = parser.instructionType();
        String instruction = null;

        if (instructionType.startsWith("A")) {
            instruction = parser.symbol();
            instruction = aInstructionToBinaryCode(instruction);

        } else if (instructionType.startsWith("C")) {
            String dest = parser.dest();
            String comp = parser.comp();
            String jump = parser.jump();
            instruction = cInstructionToBinaryCode(dest, comp, jump);

        }
        return instruction;
    }

    /**
     * Translate a c-instruction from symbolic to a 16-bit-long binary code
     * @param dest - dest bits
     * @param comp - comp bits
     * @param jump - jump bits
     * @return translated C-instruction
     */
    private String cInstructionToBinaryCode(String dest, String comp, String jump) {
        dest = Code.dest(dest);
        comp = Code.comp(comp);
        jump = Code.jump(jump);
        return (comp + dest + jump);
    }

    /**
     * Translate an A-instruction from symbolic to a 16-bit-long binary code
     * @param instruction - A-instruction to translate to binary
     * @return translated A-instruction
     */
    private String aInstructionToBinaryCode(String instruction) {

        // If first char of the instruction is not a digit - we want the variable / label address
        char firstChar = instruction.charAt(0);
        if (!Character.isDigit(firstChar)) {
            instruction = String.valueOf(symbolTable.getAddress(instruction));
        }
        instruction = toBinaryString(Integer.parseInt(instruction));
        return padWithZeros(instruction);
    }

    /**
     * Pads a given string to a 16-digits length by adding zeroes at its left side
     * @param instruction - string to pad
     * @return padded string
     */
    private String padWithZeros (String instruction) {
        int wordWidthInBits = 16;
        if (instruction.length() >= wordWidthInBits) { // No need to pad
            return instruction;
        }

        // Create a left-tale of zeroes
        StringBuilder newInst = new StringBuilder();
        while (newInst.length() < wordWidthInBits - instruction.length()) {
            newInst.append('0');
        }
        // Glue the tale to the given instruction
        newInst.append(instruction);
        return newInst.toString();
    }

    /**
     * Writes the binary instruction to the output file buffer
     * @param instruction - binary instruction to write
     * @throws IOException (If an I/O error occurs while writing to the file)
     */
    private void writeToOutputFile(String instruction) throws IOException {
        outputFileBuffer.write(instruction);
        outputFileBuffer.newLine();
    }
}