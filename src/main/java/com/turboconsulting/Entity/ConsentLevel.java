package com.turboconsulting.Entity;

public enum ConsentLevel {
    NONE("NONE"),
    RESTRICTED("RESTRICTED"),
    UNRESTRICTED("UNRESTRICTED");

    private final String text;

    ConsentLevel(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }


    public static ConsentLevel fromString(String text) {
        for (ConsentLevel b : ConsentLevel.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
