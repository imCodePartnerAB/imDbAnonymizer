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
            System.setOut(new PrintStream(System.out,true,"Cp850"));
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
                    String xmlValue=Value.getXmlValue(fieldValue);
                    if (_rs.wasNull()
                        || (Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).ValidateChange(_rs.getString(fieldName))
                        && !_rs.getString(fieldName).contains(xmlValue))) {
                        _rs.updateNString(fieldName, xmlValue);
                        updateStr += "`" + fieldName + "`=" + xmlValue+ ", ";
                        _doUpdate=true;
                    }
                }
                if (_doUpdate) {
                    updateStr = updateStr.replaceAll(", $", " ");
                    updateStr += " WHERE  `" + tblIdField + "`=" + _rs.getString(tblIdField) + ";";
                    log.debug("Done update: " + updateStr);
                    _rs.updateRow();
                }
            }
            _rs.close();
            log.debug("\n\n");
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug(e);
        }
    }

    // Iterate through table rows, and output SQL statements
    private static void generateSQL(int tableId) {
        //_db = new db();
        boolean _ret = false;
        try {
            String tblName = Value.getXmlRoot().getTables().getTable().get(tableId).getName();
            String tblIdField = Value.getXmlRoot().getTables().getTable().get(tableId).getIdfield();
            ResultSet _rs = _db.GetRS("SELECT * FROM " + tblName);// + " LIMIT 0,10");

            while (_rs.next()) {
                boolean _doUpdate = false;
                String updateStr = "UPDATE " + tblName + " SET ";
                for (int i = 0; i < Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().size(); i++) {
                    String fieldName = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getName();
                    String fieldValue = Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).getValue();
                    String xmlValue=Value.getXmlValue(fieldValue);

                    //log.debug(_rs.getString(fieldName)+" " + fieldName);
                    if ((Value.getXmlRoot().getTables().getTable().get(tableId).getFields().getField().get(i).ValidateChange(_rs.getString(fieldName))
                            && !(_rs.getString(fieldName)+"").contains(xmlValue))) {
                        //log.debug( _rs.getString(fieldName)+" ?=? "+xmlValue );
                        updateStr += "`" + fieldName + "`=" + xmlValue + ", ";
                        _doUpdate=true;
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