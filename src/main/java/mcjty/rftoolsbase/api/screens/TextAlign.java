package mcjty.rftoolsbase.api.screens;

public enum TextAlign {
    ALIGN_LEFT,
    ALIGN_CENTER,
    ALIGN_RIGHT;

    public static TextAlign get(String alignment) {
        if ("Left".equals(alignment)) {
            return TextAlign.ALIGN_LEFT;
        } else {
            return "Right".equals(alignment) ? TextAlign.ALIGN_RIGHT : TextAlign.ALIGN_CENTER;
        }
    }
}
