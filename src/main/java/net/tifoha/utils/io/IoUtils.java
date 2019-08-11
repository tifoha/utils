package net.tifoha.utils.io;

import lombok.Getter;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.math.RoundingMode.HALF_UP;
import static java.nio.file.Files.isDirectory;

/**
 * @author Vitalii Sereda
 */
public class IoUtils {
    private static final BigDecimal THESHOLD = new BigDecimal(1000);

    public static String toHumanReadable(long bytes) {
        BigDecimal size = new BigDecimal(bytes);
        SizeUnit[] sizeUnits = SizeUnit.values();
        BigDecimal result = size;
        for (SizeUnit sizeUnit : sizeUnits) {
            result = size.divide(sizeUnit.multiplier);
            if (result.compareTo(THESHOLD) < 0) {
                return sizeUnit.toString(bytes);
            }
        }
        return SizeUnit.EB.toString(bytes);
    }

    @Getter
    public enum SizeUnit {
        B(1L, "Bytes"),
        KB(1_024L, "Kilobytes"),
        MB(1_048_576L, "Megabytes"),
        GB(1_073_741_824L, "Gigabytes"),
        TB(1_099_511_627_776L, "Terabytes"),
        PB(1_125_899_906_842_624L, "Petabytes"),
        EB(1_152_921_504_606_846_976L, "Exabytes");
        private final BigDecimal multiplier;
        private final String fullName;

        SizeUnit(long multiplier, String fullName) {
            this.multiplier = new BigDecimal(multiplier);
            this.fullName = fullName;
        }

        public BigDecimal to(long amount, SizeUnit sizeUnit) {
            return new BigDecimal(amount)
                    .multiply(multiplier)
                    .divide(sizeUnit.multiplier, 2, HALF_UP);
        }

        public String toString(long bytes) {
            return new BigDecimal(bytes).divide(multiplier, 2, HALF_UP) + name();
        }
    }

    public static void createParentDirectories(Path path) {
        path = path.normalize();
        Path parentDirectory = isDirectory(path) ? path : path.getParent();
        if (parentDirectory == null) {
            return;
        }
        if (!Files.exists(parentDirectory)) {
            try {
                Files.createDirectories(parentDirectory);
            } catch (IOException e) {
                throw new UncheckedIOException("Unable to create parent directories of " + path, e);
            }
        }
    }

    /**
     * Close {@link Closeable} resource.
     *
     * @param closeables
     *            {@link Closeable} array
     */
    public static void close(final AutoCloseable... closeables) {
        close(null, closeables);
    }

    /**
     * Close {@link Closeable} resource.
     *
     * @param logger
     *            {@link Logger} instance, can be <code>null</code> if logging not required
     * @param closeables
     *            {@link Closeable} array
     */
    public static void close(final Logger logger, final AutoCloseable... closeables) {
        if (closeables == null || closeables.length == 0)
            return;

        for (final AutoCloseable c : closeables)
            if (c != null)
                try {
                    c.close();
                } catch (final Exception e) {
                    if (logger != null && logger.isDebugEnabled())
                        logger.debug("Closing[" + c + "] fail", e);
                }
    }

    public static void main(String[] args) {
        System.out.println(toHumanReadable(312314123));
    }
}
