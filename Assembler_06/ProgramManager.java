// Imports:
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ProgramManager:
 * The main file for the project (run-file)
 */
public class ProgramManager {

    // Constants:
    private static final int UNSUCCESSFUL = 1;

    /**
     * The MAIN function that run the whole project
     * @param args - arguments from command line
     */
    public static void main(String[] args)
    {
        // Gets the path from the command line and remove white spaces
        String path = args[0].trim();
        File inputFile = new File (path);

        // If file or directory doesn't exist
        if (!inputFile.exists()) {
            System.out.println("File or directory does not exist");
            System.exit(UNSUCCESSFUL);
        }

        // Extract the absolut path of the file, without the file name, if exists
        String absolutPath = inputFile.getAbsolutePath();
        String name = inputFile.getName();

        // Put all files names in a hash map, in order to handle a situation of many files
        HashMap<String, File> filesMap = new HashMap<>();
        String[] filesArr = inputFile.list();
        if (filesArr != null) { // if we've got a path to a directory with files in it
            absolutPath = absolutPath + "\\";
            for (String file : filesArr ) {
                if (file.endsWith(".asm")) { // we want only "asm" files
                    inputFile = new File(name + "\\" + file);
                    file = file.substring(0, file.lastIndexOf(".")); // remove the ".asm" from the files names
                    filesMap.put(file, inputFile);
                }
            }
        } else { // else - we've got a path to a single file
            String fileName = inputFile.getName();
            absolutPath = absolutPath.substring(0, absolutPath.indexOf(name));
            fileName = fileName.substring(0, fileName.lastIndexOf(".")); // remove the ".asm" from the file name
            filesMap.put(fileName, inputFile);
        }

        // For each file we have in that hash map
        for (Map.Entry<String, File> set : filesMap.entrySet()) {

            String fileName = set.getKey();

            // Create an output file in the same directory, named as the input file, ends with ".hack"
            String outputFilePath = absolutPath + fileName + ".hack";
            File outputFile = new File (outputFilePath);

            try {
                if (outputFile.exists()) { // If we already have a file with that name
                    if (!outputFile.delete()){ // Try to delete it
                        System.out.println("File named '" + fileName + ".hack' already exists, but cannot be deleted" +
                                           " and replaced. Close it and try again");
                        System.exit(UNSUCCESSFUL);
                    }
                }

                if (outputFile.createNewFile()) { // If we successfully created an output file

                    // Creates a HackAssembler and translate the file
                    HackAssembler hackAssembler = new HackAssembler(inputFile, outputFile);
                    hackAssembler.assemble();

                } else { // we couldn't create an output file
                    System.out.println("Cannot create an output file for '" + fileName + "'");
                    System.exit(UNSUCCESSFUL);
                }
            } catch (IOException e) {
                System.out.println("An I/O error occurred");
                System.exit(UNSUCCESSFUL);
            }
        }
    }
}


