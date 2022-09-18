package ai.faire.challenge.airport.model.i18n;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class Translator {

  private static MessageSource messageSource;
  private static final Translator translator = new Translator();

  private Translator() {
  }

  public Translator(MessageSource messageSource) {
    Translator.messageSource = messageSource;
  }

  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    return messageSource != null ? messageSource.getMessage(code, args, defaultMessage, locale) : defaultMessage;
  }

  public String[] getMessage(String code, Object[] args, String defaultCode, String defaultMessage, Locale locale) {
    String[] codeAndMessage = new String[2];
    if (defaultCode != null && !defaultCode.isEmpty()) {
      try {
        codeAndMessage[1] = messageSource.getMessage(code, args, locale);
        codeAndMessage[0] = code;
      } catch (Exception var8) {
        codeAndMessage[0] = defaultCode;
        codeAndMessage[1] = this.getMessage(defaultCode, null, defaultMessage, locale);
      }

      return codeAndMessage;
    } else {
      codeAndMessage[0] = code;
      codeAndMessage[1] = this.getMessage(code, args, defaultMessage, locale);
      return codeAndMessage;
    }
  }

  public static Translator getInstance() {
    return translator;
  }

}
