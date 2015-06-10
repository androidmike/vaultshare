package com.vaultshare.play;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by mchang on 6/9/15.
 */
public class LogUtil {

    public static String getString(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
