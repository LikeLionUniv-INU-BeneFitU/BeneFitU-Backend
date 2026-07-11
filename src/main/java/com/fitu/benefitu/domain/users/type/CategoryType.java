package com.fitu.benefitu.domain.users.type;

public enum CategoryType {
    ALL,
    CORPORATE,
    REGION,
    REQUIREMENTS,
    STATE;

    public static CategoryType fromString(String value) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.name().equalsIgnoreCase(value)) {
                return categoryType;
            }
        }
        return ALL;
    }
}
