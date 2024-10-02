package project.backend.business.post.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;

public class JsonParser {

  public static Map<String, String> convertString2Json(String text) {
    try {
      text = stripJsonMarkdown(text);
      JSONObject jsonObject = new JSONObject(text);

      String title = jsonObject.getString("title");
      String content = jsonObject.getString("content");

      Map<String, String> result = new HashMap<>();
      result.put("title", title);
      result.put("content", content);
      return result;
    } catch (JSONException e) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
  }

  private static String stripJsonMarkdown(String text) {
    String jsonPattern = "(?s)```json\\s*(.*?)\\s*```";
    Pattern pattern = Pattern.compile(jsonPattern);
    Matcher matcher = pattern.matcher(text);
    if (!matcher.find()) {
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
    return matcher.group(1);
  }
}
