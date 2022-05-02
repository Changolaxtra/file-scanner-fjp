package dan.rojas.epam.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

@Slf4j
public class FileScannerRecursiveAction extends RecursiveTask<BigInteger> {

  private final File file;
  private final boolean printPath;

  public FileScannerRecursiveAction(final File file, final boolean printPath) {
    this.file = file;
    this.printPath = printPath;
  }

  @Override
  protected BigInteger compute() {
    if (Objects.nonNull(file)) {
      if (file.isFile()) {
        print(file, false);
        return BigInteger.valueOf(file.length());
      }
      if (file.isDirectory()) {
        print(file, true);
        final List<FileScannerRecursiveAction> subTasks =
            Optional.ofNullable(file.listFiles())
                .map(Arrays::asList)
                .orElse(new ArrayList<>())
                .stream()
                .map(listedFile -> new FileScannerRecursiveAction(listedFile, printPath))
                .collect(Collectors.toList());

        subTasks.forEach(ForkJoinTask::fork);

        return subTasks.stream()
            .map(FileScannerRecursiveAction::join)
            .reduce(BigInteger.ZERO, BigInteger::add);
      }
    }
    return BigInteger.ZERO;
  }

  private void print(final File file, final boolean isDirectory) {
    if (printPath) {
      if (isDirectory) {
        log.debug("File " + file.getName() + " is directory.");
      } else {
        log.debug("File " + file.getName() + " is single file.");
        log.info(file.getAbsolutePath());
      }
    }
  }

}
