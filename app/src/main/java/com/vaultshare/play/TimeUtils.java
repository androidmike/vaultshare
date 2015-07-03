package com.vaultshare.play;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mchang on 6/8/15.
 */
public class TimeUtils {

    private static final int               SECOND_MILLIS    = 1000;
    private static final int               MINUTE_MILLIS    = 60 * SECOND_MILLIS;
    private static final int               HOUR_MILLIS      = 60 * MINUTE_MILLIS;
    private static final int               DAY_MILLIS       = 24 * HOUR_MILLIS;
    public static final  DateTimeFormatter TIMESTAMP_FORMAT = ISODateTimeFormat.dateTime();

    public static String getCurrentTimestamp() {
        // Default time zone is the device time zone
        return TIMESTAMP_FORMAT.print(new DateTime(DateTimeZone.getDefault()));
    }

    public static boolean isWithinSeconds(String string, int secs) {
        DateTime date = getTime(string);
        return !(date.isBefore(DateTime.now().minusSeconds(secs)));
    }

    public static int getBeginningTimeMs() {
        return 0;
    }

    public static String dateToString(Date date) {
        DateTime dt = new DateTime(date);
        return TIMESTAMP_FORMAT.print(dt);
    }

    public static String getString(DateTime date) {
        return TIMESTAMP_FORMAT.print(date);
    }

    public static DateTime parseTimestamp(String timestamp) {
        return TIMESTAMP_FORMAT.parseDateTime(timestamp);
    }

    public static String getTimeAgo(String time) {
        if (getTimeAgo(parseTimestamp(time).getMillis()) != null) {
            return getTimeAgo(parseTimestamp(time).getMillis());
        }
        return "";
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new DateTime().getMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "A minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "An hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static DateTime getTime(String lastUpdateDate) {
        return TIMESTAMP_FORMAT.parseDateTime(lastUpdateDate);

    }

    public static int getSecsInDay() {
        return 86400;
    }

    public static Date stringToDate(String date) {
        return TimeUtils.getTime(date).toDate();
    }

    public static String toString(DateTime time) {
        return time.toString(TIMESTAMP_FORMAT);
    }

    public static long getTimeSince(String time) {
        DateTime startTime = getTime(time);
        return new DateTime().getMillis() - startTime.getMillis();
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);

        if (days > 0) {
            sb.append(days);
            sb.append(" Days ");
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append(" Hr ");
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(" Min ");
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append(" Sec");
        }
        return (sb.toString());
    }
}
