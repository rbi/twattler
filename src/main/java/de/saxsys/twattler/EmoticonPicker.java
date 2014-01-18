package de.saxsys.twattler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class EmoticonPicker {

  public static final String PATH_TO_EMOTICONS = "../../../resources/emoticons/";
  public static final String EMOTION_BIGGRIN = "😀";
  public static final String EMOTION_CONFUSED = "😕";
  public static final String EMOTION_EEK = "😮";
  public static final String EMOTION_MAD = "😠";
  public static final String EMOTION_SAD = "😟";
  public static final String EMOTION_SMILE = "😃";
  public static final String EMOTION_SURPRISE = "😱";
  private static final int NO_EMOTICON_FOUND = -1;
  private static final int ONE_PLUS_ONE = 2;
  public static Map<String, String> emoticonMap = new HashMap<>();
  static {
    emoticonMap.put(EMOTION_BIGGRIN, "icon_biggrin.gif");
    emoticonMap.put(EMOTION_CONFUSED, "icon_confused.gif");
    emoticonMap.put(EMOTION_EEK, "icon_eek.gif");
    emoticonMap.put(EMOTION_SAD, "icon_mad.gif");
    emoticonMap.put(EMOTION_MAD, "icon_sad.gif");
    emoticonMap.put(EMOTION_SMILE, "icon_smile.gif");
    emoticonMap.put(EMOTION_SURPRISE, "icon_surprised.gif");
  }

  public String findEmoticonPath(String str) {

    for (String key : emoticonMap.keySet()) {

      if (str.equals(key)) {
        return emoticonMap.get(key);
      }
    }

    return null;
  }

  public int findEmoticonTextStart(String text) {

    int result = NO_EMOTICON_FOUND;

    for (String key : emoticonMap.keySet()) {

      if (text.contains(key)) {
        int temp = text.indexOf(key);
        if (temp > 0 && temp < result) {
          result = temp;
        }
      }
    }
    return result;
  }

  // TODO Gehört hier nicht wirklich hin!!!
  public void checkText(TextFlow tf, String text) {

    Collection<Node> newChildren = new ArrayList<>();

    // Textteile prüfen wo ein Emoticon liegt
    int beginIndex = 0;
    int emoticonTextStart = NO_EMOTICON_FOUND;
    do {
      emoticonTextStart = findEmoticonTextStart(text);

      if (emoticonTextStart != NO_EMOTICON_FOUND) {

        newChildren.add(new Text(text.substring(beginIndex, emoticonTextStart)));
        String emoticon = text.substring(emoticonTextStart, emoticonTextStart + ONE_PLUS_ONE);
        newChildren.add(new ImageView("file:///" + findEmoticonPath(emoticon)));
        try {
          text = text.substring(emoticonTextStart + ONE_PLUS_ONE);
        } catch (IndexOutOfBoundsException e) {
          // Naja Emoticon war wohl das letzte im Text
          emoticonTextStart = NO_EMOTICON_FOUND;
        }
      }

    } while (emoticonTextStart != NO_EMOTICON_FOUND);

      if (emoticonTextStart != NO_EMOTICON_FOUND) {
    newChildren.add(new Text(text.substring(emoticonTextStart)));
      }

    tf.getChildren().addAll(newChildren);
  }
}
