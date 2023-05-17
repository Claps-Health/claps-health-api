package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	private Utils() {
		throw new IllegalStateException("Utils class");
	}

	private static Gson Json;

	static {
		Json= new GsonBuilder().create();
	}

	public static boolean isJSONValid(String jsonInString) {
		try {
			Json.fromJson(jsonInString, Object.class);
			return true;
		} catch(com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	public static String toJson(Object obj) {
		return Json.toJson(obj).toString();
	}

	public static <T> T getClassObjFromJsonObject(Object json, Class<T> classOfT) {
		return Json.fromJson(Json.toJsonTree(json), classOfT);
	}

	public static <T> T getClassObjFromJsonObject(Object json, Type typeOfT) {
		return Json.fromJson(Json.toJsonTree(json), typeOfT);
	}

	public static <T> T getClassObjFromJsonString(String json_str, Class<T> classOfT) {
		return Json.fromJson(json_str, classOfT);
	}

	public static <T> T getClassObjFromJsonString(String json_str, Type typeOfT) {
		return Json.fromJson(json_str, typeOfT);
	}

	private final static Base64.Decoder decoder = Base64.getDecoder();
	private final static Base64.Encoder encoder = Base64.getEncoder();

	public static byte[] decodeBase64String(String SrcStr) {
		return decoder.decode(SrcStr);
	}

	public static byte[] decodeBase64String(byte[] SrcBytes) {
		return decoder.decode(SrcBytes);
	}

	public static String encodeBase64String(byte[] in) {
		if(in==null || in.length==0) return null;
		return encoder.encodeToString(in);
	}

	public static String restoreBase64String(String SrcStr) {
		return new String(decoder.decode(SrcStr), StandardCharsets.UTF_8);
	}

	public static String convertToBase64String(String in) {
		if(in==null || in.isEmpty()) return null;
		return encoder.encodeToString(in.getBytes(StandardCharsets.UTF_8));
	}

	public static Long addSecondsWithUTC(int seconds) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime().getTime()/1000;
	}

	public static Long addDaysWithUTC(int days) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, days * 24);
		return cal.getTime().getTime()/1000;
	}

}
