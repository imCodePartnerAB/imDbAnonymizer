/*
 * Copyright (C) 2020. Jacob Sandin och imCode Partner AB - All Rights Reserved.
 * This file is part of the project dbanonymizer., and you may use, distribute and modify this code under the licence agreement terms you have with imCode Partner AB for the specific project.
 *
 */

package com.imcode.internal.imAnonymizer;

import com.imcode.internal.imAnonymizer.parser.Value;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class db {

    private java.util.ArrayList<Statement> _stmt = new java.util.ArrayList<Statement>();
    private Connection _conn;
    private ResultSet _rs;

    public db() {
        try {
            connect();
            log.debug("Connection: " + _conn);
        } catch (Exception e) {
            log.debug(e);
        }
    }

    private void connect() throws ClassNotFoundException, SQLException {
        String _url = Value.getJdbcURl();
        Class.forName(Value.getJdbcDriver());
        _conn = DriverManager.getConnection(_url, Value.getDBusername(), Value.getDBpassword());
    }

    protected void close() {
//        Log.debug("Closing database");
        try {
            if (_rs != null) {
                try {
//                    Log.debug("Closing ResultSet");
                    _rs.close();
                } catch (Exception e) {
                    log.debug(e);
                }
                _rs = null;
            }

//            Log.debug("Closing SQL Statements (" + _stmt.size() + ")");
            for (Statement st : _stmt) {
                if (st != null) {
                    try {

                        st.close();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        log.debug(e);
                    }
                    st = null;
                }
            }
            if (_conn != null) {
                try {
//                    Log.debug("Closing SQL connection");
                    _conn.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.debug(e);
                }
                _conn = null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug(e);
        }

    }

    private Statement getStatment() throws SQLException, ClassNotFoundException {
        if (_conn.isClosed()) {
            connect();
        }

        Statement _st = _conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        for (Statement st : _stmt) {
            if (st != null) {
                st = _conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                _st = st;
            } else if (st.isClosed()) {
                _st = st;
            }
        }
        if (_st == null) {
            _st = _conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            _stmt.add(_st);
        }


        _stmt.add(_st);
        return _st;
    }

    public ResultSet GetRS(String sqlstatement) {
        try {

            Statement _st = getStatment();

            if (_st.execute(sqlstatement)) {
                return _st.getResultSet();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.debug(e);
        }
        return _rs;
    }

    public int InsertDb(String sqlstatement) {
        int _autoIncKeyFromFunc = -1;
        try {
            Statement _st = getStatment();

            _st.executeUpdate(sqlstatement);

            ResultSet rs = _st.executeQuery("SELECT LAST_INSERT_ID()");

            if (rs.next()) {
                _autoIncKeyFromFunc = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new Error("Insert failed",e);
            //e.printStackTrace();

        }
        return _autoIncKeyFromFunc;
    }

    public static String escapeString(String str) {
        String _str = str;
        _str = processString(_str);
        _str = _str.replaceAll("\\\\r", "\\r");
        _str = _str.replaceAll("\\\\n", "\\n");
        _str = _str.replaceAll("\"", "\\\"");
        _str = _str.replaceAll("_", "\\_");
        _str = _str.replaceAll("%", "\\%");

        return _str;
    }

    private static String processString(String s) {
        if (s == null) {
            return "''";
        }
        StringBuilder buffer = new StringBuilder();
        //buffer.append('\'');
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);
            if (ch == '\'' || ch == '\\') {
                buffer.append('\\');
            }
            buffer.append(ch);
        }
        //buffer.append('\'');
        return buffer.toString();
    }
}
