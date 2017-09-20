package com.example.uberv;

import com.sun.istack.internal.Nullable;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO add null checks for message and args
public abstract class Logger {

    private static LogLevel sLogLevel = LogLevel.NONE;

    public static void init(LogLevel logLevel) {
        sLogLevel = logLevel;
    }

    // Facade methods
    public static void d(String message, @Nullable String... args) {
        tryLogMessage(LogLevel.DEBUG, message, args);
    }

    public static void out(String message, @Nullable String... args) {
        System.out.println(String.format(message, args));
    }

    public static void e(String message, @Nullable String... args) {
        tryLogMessage(System.err, LogLevel.ERROR, message, args);
    }

    public static void e(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        tryLogMessage(System.err, LogLevel.ERROR, sw.toString(), null);
    }

    public static void e(Throwable throwable, String message, @Nullable String... args) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String errorMessage = String.format("%s\n%s", String.format(message, args), sw.toString());
        tryLogMessage(System.err, LogLevel.ERROR, errorMessage, null);
    }

    public static void wtf(String message, @Nullable String... args) {
        tryLogMessage(System.err, LogLevel.WTF, message, args);
    }

    /**
     * Logs passed {@code message} amd {@code args} at level {@link LogLevel#INFO}
     */
    public static void i(String message, @Nullable String... args) {
        tryLogMessage(LogLevel.INFO, message, args);
    }

    private static void tryLogMessage(LogLevel logLevel, String message, @Nullable String... args) {
        tryLogMessage(System.out, logLevel, message, args);
    }

    private static void tryLogMessage(PrintStream printStream, LogLevel logLevel, String message, @Nullable String... args) {
        if (sLogLevel.level >= logLevel.level) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement traceElement = stackTraceElements[stackTraceElements.length - 1];
            String callInfoString = String.format("%s.%s:%d", traceElement.getClassName(), traceElement.getMethodName(), traceElement.getLineNumber());
            long threadId = Thread.currentThread().getId();
            // <date> | <log_level_name | <log_tag>: <log_message>
            String result = String.format("%s %04d/%s/%s: %s",
                    getTimestamp(),
                    threadId,
                    logLevel.name,
                    callInfoString,
                    String.format(message, args));
            printStream.println(result);
        }
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM dd hh:mm:ss"));
    }

    public static enum LogLevel {
        DEBUG(5, "DEBUG"), INFO(4, "INFO "), ERROR(3, "ERROR"), WTF(2, "WTF  "), OUTPUT(1, "OUT"), NONE(0, "NONE ");

        private final int level;
        private final String name;

        LogLevel(int level, String name) {
            this.level = level;
            this.name = name;
        }
    }
}
