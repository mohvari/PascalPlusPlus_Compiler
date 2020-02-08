public class Utils {
    public static String addEndToString(String string) {
        return "\"" + string.substring(1, string.length() - 1) + "\\00" + "\"";
    }
}
