import java.io.*;

public class FileManager {
    static String defFileName;

    public FileManager(String fileName) throws IOException {
        defFileName = fileName;
        File file = new File(fileName);
        file.createNewFile();
    }


    public static void add(String addString,  String fileName) throws IOException {
        FileWriter fr = new FileWriter(fileName,true);
        fr.write (addString + "\n");
        fr.close();
    }
    public static void add(String addString) throws IOException {add(addString, defFileName);}

    public static String getLine(String fileName, int lineNum) throws IOException {
        String out = "";
        FileReader rf = new FileReader(fileName);
        BufferedReader br = new BufferedReader(rf);
        for (int lineNumber = 0; lineNumber < 10; lineNumber++) {
            if (lineNumber == lineNum) {
                out = br.readLine();
            } else
                br.readLine();
        }
        br.close();
        return out;
    }
    public static String getLine(int lineNum) throws IOException {return getLine(defFileName, lineNum);}
    public static String  getLine(String fileName) throws IOException {
        return (getLine(fileName, 0));
    }

    public static String getAll(String fileName) throws IOException {
        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        StringBuilder out = new StringBuilder();
        while ((st = br.readLine()) != null) {
            out.append(st).append('\n');
        }
        br.close();
        return(out.toString());
    }
    public static String getAll() throws IOException {
        return getAll(defFileName);
    }

    public static void delete(int startLine, int numLines, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        //String buffer to store contents of the file
        StringBuilder sb = new StringBuilder();

        //Keep track of the line number
        int linenumber = 1;
        String line;

        while ((line = br.readLine()) != null) {
            //Store each valid line in the string buffer
            if (linenumber < startLine || linenumber >= startLine + numLines)
                sb.append(line).append("\n");
            linenumber++;
        }
        if (startLine + numLines > linenumber)
            System.err.println("End of file reached.");
        br.close();

        FileWriter fw = new FileWriter(fileName);
        //Write entire string buffer into the file
        fw.write(sb.toString());
        fw.close();
    }
    public static void delete(int startLine, int numLines) throws IOException {
        delete(startLine, numLines, defFileName);
    }
    public static void delete(int line, String fileName) throws IOException {
        delete(line, 1, fileName);
    }
    public static void delete(int line) throws IOException {
        delete(line, 1, defFileName);
    }


}
