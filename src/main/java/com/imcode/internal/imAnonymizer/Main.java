/*
 * Copyright (C) 2020. Jacob Sandin och imCode Partner AB - All Rights Reserved.
 * This file is part of the project dbanonymizer., and you may use, distribute and modify this code under the licence agreement terms you have with imCode Partner AB for the specific project.
 *
 */

package com.imcode.internal.imAnonymizer;

import com.imcode.internal.imAnonymizer.parser.Value;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;

public class Main {
    private static db _db = null;

    public static void main(String args[]) {
        log.debug("Starting");
        try {
            //TODO Dont know why it cant do utf-8 maby reading files wrong??
            System.setOut(new PrintStream(System.out,true,"utf-8"));
            //System.setOut(new PrintStream(System.out,true,"Cp850"));
        } catch (UnsupportedEncodingException e) {
            log.debug(e);
        }

        //Initialize the commandline args, such as xmlfile to use.
        Value.init(args);

        //TODO: First we create a working functionallity here in Main, then we move it and make it confirm to the goals.

        _db = new db();
        // Iterate the tables configured in the current xmlfile.
        for (int i = 0; i < Value.getXmlRoot().getTables().getTable().size(); i++) {
            log.debug("list table name: " + Value.getXmlRoot().getTables().getTable().get(i).getName());
            //TODO: Make a property in XML that decides wheter to output SQL or to update direcly
            //Update tables direcly
            //iterateTable(i);

            //Generate SQL to console.
            generateSQL(i);
        }
        _db.close();
    }

    // Iterate through table rows, and update them using ResultSet
    private static void iterateTable(int tableId) {
        boolean _ret = false;
        try {
            String tblName = Value.getXmlRoot().getTables().getTable().get(tableId).getName();
            String tblIdField = Value.getXmlRoot().getTables().getTable().get(tableId).getIdfield();
            ResultSet _rs = _db.GetRS("SELECT * FROM " + tblName);
            while (_rs.next()) {
                String updateStr = "UPDATE " + tblName + " SET ";
                boolean _doUpdate = false;
                for (int i = 0; i < Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().size(); i++) {
                    String fieldName = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getName();
                    String fieldValue = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getValue();
                    String xmlValue = Value.getXmlValue(fieldValue);
                    
                    // Ensure that the xmlValue is in UTF-8 format
                    byte[] utf8Bytes = xmlValue.getBytes("UTF-8");
                    String utf8Value = new String(utf8Bytes, "UTF-8");
                    
                    if (_rs.wasNull()
                            || (Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).ValidateChange(_rs.getString(fieldName))
                                    && !_rs.getString(fieldName).contains(utf8Value))) {
                        _rs.updateNString(fieldName, utf8Value);
                        updateStr += "`" + fieldName + "`=" + utf8Value + ", ";
                        _doUpdate = true;
                    }
                }
                if (_doUpdate) {
                    updateStr = updateStr.replaceAll(", $", " ");
                    updateStr += " WHERE `" + tblIdField + "`=" + _rs.getString(tblIdField) + ";";
                    log.debug("Done update: " + updateStr);
                    _rs.updateRow();
                }
            }
            _rs.close();
            log.debug("\n\n");
        } catch (Exception e) {
            // e.printStackTrace();
            log.debug(e);
        }
    }

    // Iterate through table rows, and output SQL statements
    private static void generateSQL(int tableId) {
        boolean _ret = false;
        try {
            String tblName = Value.getXmlRoot().getTables().getTable().get(tableId).getName();
            String tblIdField = Value.getXmlRoot().getTables().getTable().get(tableId).getIdfield();
            ResultSet _rs = _db.GetRS("SELECT * FROM " + tblName);
    
            while (_rs.next()) {
                boolean _doUpdate = false;
                String updateStr = "UPDATE " + tblName + " SET ";
                for (int i = 0; i < Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().size(); i++) {
                    String fieldName = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getName();
                    String fieldValue = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getValue();
                    String xmlValue = Value.getXmlValue(fieldValue);
    
                    // Ensure that the xmlValue is in UTF-8 format
                    byte[] utf8Bytes = xmlValue.getBytes("UTF-8");
                    String utf8Value = new String(utf8Bytes, "UTF-8");
    
                    if ((Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).ValidateChange(_rs.getString(fieldName))
                            && !(_rs.getString(fieldName) + "").contains(utf8Value))) {
                        updateStr += "`" + fieldName + "`=" + utf8Value + ", ";
                        _doUpdate = true;
                    }
                }
                if (_doUpdate) {
                    updateStr = updateStr.replaceAll(", $", " ");
                    updateStr += " WHERE  `" + tblIdField + "`=" + _rs.getString(tblIdField) + ";";
                    System.out.println(updateStr);
                    updateStr = "";
                }
            }
            _rs.close();
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug(e);
        }
    }
    
}
