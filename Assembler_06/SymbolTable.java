// Imports:
import java.util.HashMap;

/**
 * SymbolTable:
 * A symbol table that keeps a correspondence between symbolic labels and numeric addresses.
 */
public class SymbolTable {

    // Fields:
    HashMap<String, Integer> symbolTable = new HashMap<>();

    /**
     * Constructor
     * Creates a new symbol table initialized with all the predefined symbols and their pre-allocated RAM addresses,
     * according to section 6.2.3 of the book
     */
    public SymbolTable () {
        buildSymbolTable();
    }

    /**
     * Initializes the Symbol Table with all the predefined symbols and their pre-allocated RAM addresses
     */
    private void buildSymbolTable() {
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }

    /**
     * Adds the pair (symbol, address) to the table
     * @param symbol - the symbol to add
     * @param address - the address corresponding to the symbol
     */
    public void addEntry (String symbol, int address) {
        if (!symbolTable.containsKey(symbol)) {
            symbolTable.put(symbol, address);
        }
    }

    /**
     * Does the symbol table contain the given symbol?
     * @param symbol - a symbol to look for in the table
     * @return - True if the symbol is contained, False otherwise
     */
    public boolean contains (String symbol) {
        return symbolTable.containsKey(symbol);
    }

    /**
     * Returns the address associated with the symbol
     * @param symbol - a symbol to look for its address
     * @return - the address associated with the symbol. returns null if the symbol is not there
     */
    public int getAddress (String symbol) {
        return symbolTable.get(symbol);
    }

}
