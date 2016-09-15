package com.yesteapea.utils;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Some missing String Utils not found any useful libraries.
 * This file is named <code>YStringUtils (Y = yesteapea)</code> to not have
 * a name conflict with the awesome <code>org.apache.commons.lang3.StringUtils</code>.
 */
public class YStringUtils {


  /** Gson object initialized with default params **/
  public static final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

  /** Gson object initialized with pretty printing set **/
  public static final Gson gsonPretty = new GsonBuilder().serializeSpecialFloatingPointValues().disableHtmlEscaping()
      .setPrettyPrinting().create();

  /** Json parser **/
  public static final JsonParser JSON_PARSER = new JsonParser();

  /** Convert any object to json**/
  public static String toJson(Object o) {
    return gson.toJson(o);
  }

  /** Converts any object to pretty json**/
  public static String toPrettyJson(Object o) {
    return gsonPretty.toJson(o);
  }

  /** Converts any object to json**/
  public static JsonObject toJsonObj(Object o) {
    return JSON_PARSER.parse(gson.toJson(o)).getAsJsonObject();
  }

  /** Converts Json string to corresponding POJO**/
  public static <T> T fromJson(String s, Class<T> classOfT) {
    return gson.fromJson(s, classOfT);
  }

  /** Converts Json string to corresponding List of POJOs**/
  public static <T> List<T> fromListJson(String inp) {
    return gson.fromJson(inp, new TypeToken<List<T>>() {
    }.getType());
  }

}

