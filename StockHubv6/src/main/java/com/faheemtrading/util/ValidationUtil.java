package com.faheemtrading.util;

public final class ValidationUtil {
    private ValidationUtil() {}

    public static boolean notEmpty(String... fields) {
        for (String f : fields) if (f == null || f.isBlank()) return false;
        return true;
    }
    public static boolean isEmail(String email) {
        return email != null && email.matches(".+@.+\\..+");
    }
}
