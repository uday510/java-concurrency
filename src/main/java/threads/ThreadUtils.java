package threads;

import java.util.logging.Logger;

public class ThreadUtils {
    private static final Logger LOGGER = Logger.getLogger(ThreadUtils.class.getName());

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void printThreadState(Thread thread) {
        if (thread == null) {
            LOGGER.warning(ANSI_RED + "⚠️ Null thread reference passed!" + ANSI_RESET);
            return;
        }

        String threadDetails = String.format(
                ANSI_BLUE + "========================================\n" + ANSI_RESET +
                        ANSI_YELLOW + "🔥 Thread Details (ID: %d) 🔥\n" + ANSI_RESET +
                        ANSI_BLUE + "========================================\n" + ANSI_RESET +
                        ANSI_GREEN + "📛 Name       : " + ANSI_RESET + "%s\n" +
                        ANSI_GREEN + "⚡ Priority    : " + ANSI_RESET + "%d\n" +
                        ANSI_GREEN + "🔄 State      : " + ANSI_RESET + "%s\n" +
                        ANSI_GREEN + "👥 Group      : " + ANSI_RESET + "%s\n" +
                        ANSI_GREEN + "💡 Alive      : " + ANSI_RESET + "%s\n" +
                        ANSI_GREEN + "👻 Daemon     : " + ANSI_RESET + "%s\n" +
                        ANSI_GREEN + "🚨 Interrupted: " + ANSI_RESET + "%s\n" +
                        ANSI_BLUE + "========================================\n" + ANSI_RESET,
                thread.getId(),
                thread.getName(),
                thread.getPriority(),
                thread.getState(),
                thread.getThreadGroup(),
                thread.isAlive() ? ANSI_GREEN + "✅ Yes" + ANSI_RESET : ANSI_RED + "❌ No" + ANSI_RESET,
                thread.isDaemon() ? ANSI_GREEN + "✅ Yes" + ANSI_RESET : ANSI_RED + "❌ No" + ANSI_RESET,
                thread.isInterrupted() ? ANSI_RED + "⚠️ Yes" + ANSI_RESET : ANSI_GREEN + "✅ No" + ANSI_RESET
        );

        LOGGER.info(threadDetails);
    }
}