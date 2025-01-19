package io.ankush.kap_mini.model;


public enum FieldType {

    TEXT,
    NUMBER,
    DATE,
    DROPDOWN,
    CHECKBOX,
    RADIO,
    BOOLEAN,
    TEXT_AREA,
    TIME,
    DATE_TIME;

    public static boolean exists(String fieldTypeStr) {
        if (fieldTypeStr == null || fieldTypeStr.trim().isEmpty()) {
            return false;
        }
        try {
            FieldType.valueOf(fieldTypeStr.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static FieldType fromString(String fieldTypeStr) {
        if (fieldTypeStr == null || fieldTypeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Field type string cannot be null or empty");
        }
        try {
            return FieldType.valueOf(fieldTypeStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid field type: " + fieldTypeStr, e);
        }
    }
}
