package domain.display;

public class DisplayFactory {

    public enum DisplayType {
        FILE,
        CONSOLE
    }

    public static Display getDisplay(DisplayType type, String... args) {
        switch (type) {
            case FILE:
                if (args.length > 0) {
                    return new FileDisplay(args[0], args.length > 1 && args[1].equals("overwrite"));
                } else {
                    throw new IllegalArgumentException("File path is required for FileDisplay");
                }
            case CONSOLE:
                return new ConsoleDisplay();
            default:
                throw new IllegalArgumentException("Unknown display type");
        }
    }
}
