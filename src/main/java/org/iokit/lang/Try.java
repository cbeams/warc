package org.iokit.lang;

import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.concurrent.Callable;

public final class Try {

    private Try() {
    }

    public static <T> T toCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception ex) {
            throw unchecked(ex);
        }
    }

    public static void toRun(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            throw unchecked(ex);
        }
    }

    private static RuntimeException unchecked(Exception ex) {
        return ex instanceof IOException ?
            new UncheckedIOException((IOException) ex) :
            new UncheckedException(ex);
    }

    @FunctionalInterface
    public interface CheckedRunnable {

        void run() throws Exception;
    }
}
