package ali.barkbot.model;

public enum CameFrom {
    Yandex,
    Avito,
    Mailing,
    Other;

    public static CameFrom fromString(String value) {
        try {
            return CameFrom.valueOf(value);
        } catch (IllegalArgumentException exception) {
            return Other;
        }
    }
}
