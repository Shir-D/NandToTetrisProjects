// Imports:
import java.util.HashMap;

/**
 * Code Class:
 * Translates Hack assembly language mnemonics (Parts if a C-Instruction) into binary code
 */
public class Code {

    // Fields:
    static HashMap<String, String> destMnemonics = new HashMap<>();
    static HashMap<String, String> compMnemonics = new HashMap<>();
    static HashMap<String, String> jumpMnemonics = new HashMap<>();

    /**
     * Constructor
     * Builds the 3 hash-maps which are used to translate the mnemonics from symbols to binary code
     */
    public Code () {
        buildDestMnemonics();
        buildCompMnemonics();
        buildJumpMnemonics();
    }

    /**
     * Builds the dest mnemonics table
     */
    private void buildDestMnemonics() {
        destMnemonics.put("no dest", "000");
        destMnemonics.put("M", "001");
        destMnemonics.put("D", "010");
        destMnemonics.put("DM", "011");
        destMnemonics.put("MD", "011");
        destMnemonics.put("A", "100");
        destMnemonics.put("AM", "101");
        destMnemonics.put("MA", "101");
        destMnemonics.put("AD", "110");
        destMnemonics.put("DA", "110");
        destMnemonics.put("ADM", "111");
        destMnemonics.put("AMD", "111");
        destMnemonics.put("DAM", "111");
        destMnemonics.put("DMA", "111");
        destMnemonics.put("MAD", "111");
        destMnemonics.put("MDA", "111");
    }

    /**
     * Builds the comp mnemonics table
     */
    private void buildCompMnemonics() {

        // Standard C-Commands
        // a=0
        compMnemonics.put("0", "1110101010");
        compMnemonics.put("1", "1110111111");
        compMnemonics.put("-1", "1110111010");
        compMnemonics.put("D", "1110001100");
        compMnemonics.put("A", "1110110000");
        compMnemonics.put("!D", "1110001101");
        compMnemonics.put("!A", "1110110001");
        compMnemonics.put("-D", "1110001111");
        compMnemonics.put("-A", "1110110011");
        compMnemonics.put("D+1", "1110011111");
        compMnemonics.put("A+1", "1110110111");
        compMnemonics.put("D-1", "1110001110");
        compMnemonics.put("A-1", "1110110010");
        compMnemonics.put("D+A", "1110000010");
        compMnemonics.put("D-A", "1110010011");
        compMnemonics.put("A-D", "1110000111");
        compMnemonics.put("D&A", "1110000000");
        compMnemonics.put("D|A", "1110010101");

        // a=1
        compMnemonics.put("M", "1111110000");
        compMnemonics.put("!M", "1111110001");
        compMnemonics.put("-M", "1111110011");
        compMnemonics.put("M+1", "1111110111");
        compMnemonics.put("M-1", "1111110010");
        compMnemonics.put("D+M", "1111000010");
        compMnemonics.put("D-M", "1111010011");
        compMnemonics.put("M-D", "1111000111");
        compMnemonics.put("D&M", "1111000000");
        compMnemonics.put("D|M", "1111010101");

        // Extended C-Commands
        compMnemonics.put("A<<", "1010100000");
        compMnemonics.put("D<<", "1010110000");
        compMnemonics.put("M<<", "1011100000");
        compMnemonics.put("A>>", "1010000000");
        compMnemonics.put("D>>", "1010010000");
        compMnemonics.put("M>>", "1011000000");
    }

    /**
     * Builds the jump mnemonics table
     */
    private void buildJumpMnemonics() {
        jumpMnemonics.put("no jump", "000");
        jumpMnemonics.put("JGT", "001");
        jumpMnemonics.put("JEQ", "010");
        jumpMnemonics.put("JGE", "011");
        jumpMnemonics.put("JLT", "100");
        jumpMnemonics.put("JNE", "101");
        jumpMnemonics.put("JLE", "110");
        jumpMnemonics.put("JMP", "111");
    }

    /**
     * Translates Hack assembly language mnemonics of DEST into binary code
     * @param mnemonic - a dest mnemonic String
     * @return a 3-bit long binary code (String) of the given mnemonic
     */
    public static String dest (String mnemonic) {
        if (mnemonic.isEmpty()) { // no dest
            return destMnemonics.get("no dest");
        }
        return destMnemonics.get(mnemonic);
    }

    /**
     * Translates Hack assembly language mnemonics of COMP into binary code
     * @param mnemonic - a comp mnemonic String
     * @return a 3-bit long binary code (String) of the given mnemonic
     */
    public static String comp (String mnemonic) {
        return compMnemonics.get(mnemonic);
    }

    /**
     * Translates Hack assembly language mnemonics of JUMP into binary code
     * @param mnemonic - a jump mnemonic String
     * @return a 3-bit long binary code (String) of the given mnemonic
     */
    public static String jump (String mnemonic) {
        if (mnemonic.isEmpty()) { // no jump condition
            return jumpMnemonics.get("no jump");
        }
        return jumpMnemonics.get(mnemonic);
    }
}
