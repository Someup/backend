package project.backend.business.post.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;

public class JsonParser {

  public static JSONObject parseJsonFromText(String text) {
    try {
      text = extractJsonContent(text);
      return new JSONObject(text);
    } catch (JSONException e) {
      throw new CustomException(ErrorCode.INVALID_SUMMARY);
    }
  }

  private static String extractJsonContent(String text) {
    String jsonPattern = "(?s)```json\\s*(.*?)\\s*```";
    Pattern pattern = Pattern.compile(jsonPattern);
    Matcher matcher = pattern.matcher(text);
    if (!matcher.find()) {
      throw new CustomException(ErrorCode.INVALID_SUMMARY);
    }
    return matcher.group(1);
  }
}
