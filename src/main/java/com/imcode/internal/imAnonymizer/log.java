/*
 * Copyright (C) 2020. Jacob Sandin och imCode Partner AB - All Rights Reserved.
 * This file is part of the project dbanonymizer., and you may use, distribute and modify this code under the licence agreement terms you have with imCode Partner AB for the specific project.
 *
 */

package com.imcode.internal.imAnonymizer;

import com.imcode.internal.imAnonymizer.parser.Value;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class log {

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void debug(String message, Object... args) {
        if (Value.logLevel() == Value.logLevels.DEBUG) {
            message = message.replaceAll("\n","\n--");
            System.out.printf("-- " + getDateTime()+ " - " + message+"\r\n", args);
        }
    }

    public static void debug(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        debug(sStackTrace);
        try {
            pw.close();
            sw.close();
        } catch (IOException ioException) {
//            ioException.printStackTrace();
        }
    }

    public static void info(String message, Object... args) {
        System.out.printf("-- " + getDateTime()+ " - " +message+"\r\n", args);
    }
}
