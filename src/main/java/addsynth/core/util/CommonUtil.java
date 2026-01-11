package addsynth.core.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import addsynth.core.util.constants.DevStage;
import addsynth.core.util.java.StringUtil;
import org.apache.logging.log4j.Logger;

public final class CommonUtil {

  public static final void displayModInfo(Logger log, String mod_name, String author, String version, DevStage dev_stage, String date){
    if(dev_stage.isDevelopment){
      log.info(StringUtil.build(mod_name, " by ", author, ", version ", version, dev_stage.label, ", built on ", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)), "."));
      log.warn("This is a development build. This is not intended for public use.");
    }
    else{
      log.info(StringUtil.build(mod_name, " by ", author, ", version ", version, ", built on ", date, "."));
    }
  }

}
