package services;

import java.util.UUID;

/**
 * Produces unique, stable identifiers for tasks. A UUID is used instead of a
 * counter so identifiers remain unique across restarts and after CSV migration.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    public static String next() {
        return "T-" + UUID.randomUUID().toString();
    }
}
