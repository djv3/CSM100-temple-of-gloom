package student;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHelper {

    public static Logger getLogger(String className) throws IOException {
        String fullClassName = className + ".class.getName()";
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT]  %5$s %n");
        Logger logger = Logger.getLogger(fullClassName);
        FileHandler fh = new FileHandler("C:\\Users\\an78d\\az\\projects\\uol\\scm100sdp\\coursework\\CSM100-temple-of-gloom\\logs\\log.txt");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        return logger;
    }
}
