package me.relex.recorder.tools;

public class CommonUtil {

    public static String convertMillis(long milliseconds) {

        milliseconds = milliseconds <= 0 ? 0 : milliseconds / 1000L;

        String hours = timeStrFormat(String.valueOf((milliseconds % (24 * 60 * 60L)) / (60 * 60L)));
        String minutes =
                timeStrFormat(String.valueOf(((milliseconds % (24 * 60 * 60L)) % (60 * 60L)) / 60));
        String second =
                timeStrFormat(String.valueOf(((milliseconds % (24 * 60 * 60L)) % (60 * 60L)) % 60));

        return hours + ":" + minutes + ":" + second;
    }

    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }
}
