package com.smartlibrary.util;

public final class RoleUtils {

    private RoleUtils() {
    }

    public static boolean isAdmin(String role) {
        return "admin".equals(role);
    }

    public static boolean isLibrarian(String role) {
        return "librarian".equals(role);
    }

    public static boolean canManageLibrary(String role) {
        return isAdmin(role) || isLibrarian(role);
    }

    public static int getBorrowDays(String role) {
        return "faculty".equals(role) ? 30 : 14;
    }

    public static int getRenewDays(String role) {
        return "faculty".equals(role) ? 15 : 7;
    }
}
