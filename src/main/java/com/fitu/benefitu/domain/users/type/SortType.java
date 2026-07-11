package com.fitu.benefitu.domain.users.type;

public enum SortType {
    DEFAULT,
    AMOUNT_HIGH,
    DEADLINE_IMMINENT;

    static SortType fromString(String sortType) {
        for (SortType type : values()) {
            if (type.name().equalsIgnoreCase(sortType)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
