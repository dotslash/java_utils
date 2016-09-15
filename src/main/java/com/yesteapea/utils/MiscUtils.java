package com.yesteapea.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Misc java utils.
 */
public class MiscUtils {
  private static final Logger LOG = Logger.getLogger(MiscUtils.class.getName());


  /**
   * Returns if a set has any commom elements with a collection
   */
  public static <T> boolean hasCommon(Collection<T> clxn, Set<T> set) {
    for (T t : clxn) {
      if (set.contains(t)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Converts an enumeration to list. Enumerations are v0 of java Iterators (Iterator extends Enumeration)
   */
  public static <T> List<T> toList(Enumeration<T> from) {
    ArrayList<T> ret = new ArrayList<T>();
    copyEnumerationToClxn(from, ret);
    return ret;
  }

  /**
   * Adds elements in an enumeration to the input collection.
   * Enumerations are v0 of java Iterators (Iterator extends Enumeration)
   */
  public static <T> void copyEnumerationToClxn(Enumeration<T> from, Collection<T> to) {
    while (from.hasMoreElements()) {
      to.add(from.nextElement());
    }
  }


  /**
   * The main value this function ads as compared to Iterables.getFirst is
   * if the iterable itself is null, it does not throw an exception
   *
   * @param iterable an iterable whose first value is to be returned
   * @param <T>      Type
   * @return The first element if exists, else null
   */
  public static <T> T getFirst(Iterable<T> iterable) {
    return getFirst(iterable, null);
  }

  /**
   * The main value this function ads as compared to Iterables.getFirst is
   * if the iterable itself is null, it does not throw an exception
   *
   * @param iterable an iterable whose first value is to be returned
   * @param <T>      Type
   * @return The first element if exists, else fallback
   */
  public static <T> T getFirst(Iterable<T> iterable, T fallback) {
    if (iterable == null) {
      return fallback;
    }
    //noinspection LoopStatementThatDoesntLoop
    for (T t : iterable) {
      return t;
    }
    return fallback;
  }

  /**
   * Returns first non null value if there some non null.
   * Else returns null.
   */
  @SafeVarargs
  public static <T> T firstNonNull(T... objs) {
    for (T obj : objs) {
      if (obj != null) {
        return obj;
      }
    }
    return null;
  }

  /**
   * Var args wrapper around guava's Lists.asList
   */
  @SafeVarargs
  public static <T> List<T> asList(T first, T... next) {
    return Lists.asList(first, next);
  }


  /**
   * Returns an iterator over the lines of a file from a filepath
   */
  public static LineIterator lineIterator(String filepath) throws IOException {
    return IOUtils.lineIterator(FileUtils.openInputStream(new File(filepath)), Charset.defaultCharset().name());
  }


  public static final double LATITUDE_MAX = 90.0;
  public static final double LONGITUDE_MAX = 180.0;

  /**
   * Calculate distance (in meters) between two points in (latitude, longitude).
   * <p>
   * Uses Haversine method to compute the distances.
   * If (lat, long) combination are invalid it returns a negative distance (invalid value)
   * </p>
   * Taken from : http://stackoverflow.com/a/27943
   */
  public static double distance(double lat1, double lng1, double lat2, double lng2) {
    if (Math.abs(lat1) == LATITUDE_MAX ||
        Math.abs(lat2) == LATITUDE_MAX ||
        Math.abs(lng1) == LONGITUDE_MAX ||
        Math.abs(lng2) == LONGITUDE_MAX) {
      return Double.MAX_VALUE;
    }

    final int R = 6371;// Radius of the earth

    Double dLat = Math.toRadians(lat2 - lat1);
    Double dLon = Math.toRadians(lng2 - lng1);
    double sinLatDist = Math.sin(dLat / 2);
    double cosLat1 = Math.cos(Math.toRadians(lat1));
    double cosLat2 = Math.cos(Math.toRadians(lat2));
    double sinLonDist = Math.sin(dLon / 2);

    Double a = (sinLatDist * sinLatDist) + (cosLat1 * cosLat2 * sinLonDist * sinLonDist);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000;// convert to meters
  }

  /**
   * Get current working directory
   */
  public static String getWorkDir() {
    String workDir = System.getProperty("user.dir");
    if (workDir.charAt(workDir.length() - 1) != '/') {
      workDir += "/";
    }
    return workDir;
  }

  /**
   * Get home directory
   */
  public static String getHomeDir() {
    return System.getProperty("user.home");
  }

  public static String expandHomeDir(String inp) {
    return inp.replaceAll("^~", getHomeDir());
  }


  public static Long safeParseLong(String number, long fallback) {
    try {
      return Long.parseLong(number);
    } catch (Exception e) {
      LOG.finest(ExceptionUtils.getStackTrace(e));
      return fallback;
    }
  }

  public static Integer safeParseInt(String number, int fallback) {
    try {
      return Integer.parseInt(number);
    } catch (Exception e) {
      LOG.finest(ExceptionUtils.getStackTrace(e));
      return fallback;
    }
  }

  public static Double safeParseDouble(String number, double fallback) {
    try {
      return Double.parseDouble(number);
    } catch (Exception e) {
      LOG.finest(ExceptionUtils.getStackTrace(e));
      return fallback;
    }
  }

  public static Float safeParseFloat(String number, float fallback) {
    try {
      return Float.parseFloat(number);
    } catch (Exception e) {
      LOG.finest(ExceptionUtils.getStackTrace(e));
      return fallback;
    }
  }

  /**
   * <code>DateTimeFormat</code> does not have a good toString method.
   * Once a date pattern is created, the pattern string is lost.
   * This object wraps around the pattern and DateTimeFormat
   * http://stackoverflow.com/questions/10490951/pattern-string-from-a-joda-time-datetimeformatter
   */
  public static class DfAndPattern {
    private final String pattern;
    /**
     * Use this object for any operations on formatter.
     * The most common operations {@link #print} and {@link #parseDateTime} are
     * added to wrapper object
     */
    public final DateTimeFormatter formatter;

    public DfAndPattern(String pattern) {
      this.pattern = pattern;
      formatter = DateTimeFormat.forPattern(pattern);
    }

    @Override
    public String toString() {
      return pattern;
    }

    /**Wraps joda DateTimeFormatter's print */
    public String print(DateTime dateTime) {
      return formatter.print(dateTime);
    }

    /**Wraps joda DateTimeFormatter's parseDateTime */
    public DateTime parseDateTime(String dateString) {
      return formatter.parseDateTime(dateString);
    }
  }

}
