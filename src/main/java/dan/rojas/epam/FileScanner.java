package dan.rojas.epam;


import dan.rojas.epam.file.FileScannerRecursiveAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

public class FileScanner {

  private final static Logger logger = LoggerFactory.getLogger(FileScanner.class);
  public static final String FILE_PATH = "C:\\Users\\Daniel_Rojas\\Projects";

  public static void main(String[] args) {
    final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    final long start = System.currentTimeMillis();
    final BigInteger size = forkJoinPool.invoke(new FileScannerRecursiveAction(new File(FILE_PATH), false));
    final long end = System.currentTimeMillis();
    logger.info("Total size: " + NumberFormat.getNumberInstance(Locale.US).format(size)
        + " bytes. (Time : " + (end - start) + "ms)");
  }
}
