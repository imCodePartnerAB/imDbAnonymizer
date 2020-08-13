/*
 * Copyright (C) 2020. Jacob Sandin och imCode Partner AB - All Rights Reserved.
 * This file is part of the project dbanonymizer., and you may use, distribute and modify this code under the licence agreement terms you have with imCode Partner AB for the specific project.
 *
 */

package com.imcode.internal.imAnonymizer.parser;

import com.imcode.internal.imAnonymizer.log;
import com.imcode.internal.imAnonymizer.parser.xml.Root;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

//This class might have to be a singleton
//TODO: DOes it have to be a singleton?
public class Value {
    //For tricking JAVA to have global variable as a singleton class
    private static Value _ref = new Value();
    private static String _xmlFileName = "";
    private static String[] _args;
    //private static xmlParser _xml = new xmlParser();
    public static String[] getArgs() {
        return _args;
    }

    public static Root getXmlRoot() {
        return _rootType;
    }
    private static Root _rootType;
//    private static String _jdbcURl="";
//    private static String _jdbcDriver="";
//    private static String _dbusername="";
//    private static String _dbpassword="";



    public static String getJdbcURl() {return _rootType.getProperties().getJdbcUrl();}
    public static String getJdbcDriver() {
        return _rootType.getProperties().getJdbcDriver();
    }
    public static String getDBpassword() {
        return _rootType.getProperties().getDbpassword();
    }
    public static String getDBusername() {
        return _rootType.getProperties().getDbusername();
    }


    public static void init(String[] args) {
        _args = args;
        commandLine cmd = new commandLine();
        _xmlFileName = cmd.getXMLFileName(args);
        if (!StringUtils.isBlank(_xmlFileName))
        {
            log.debug("Config to use set to xmlFile="+_xmlFileName);
            //_xml.init(_xmlFileName);
            initXML();
        }

    }
    private static void initXML() {
        File xmlFile = new File(_xmlFileName );

        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(Root.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            _rootType = (Root) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(xmlFile));

            log.debug("jdbcURL: "+_rootType.getProperties().getJdbcUrl());
            log.debug("jdbcDriver: "+_rootType.getProperties().getJdbcDriver());
            log.debug("dbUserName: "+_rootType.getProperties().getDbusername());
            log.debug("dbPassword: *******");
        }
        catch (JAXBException e)
        {
            //e.printStackTrace();
            log.debug(e);
        }

    }

    //TODO
    // Get the value for a property in the xml file
    public static String getXmlValue(String property) {

        // Iterate Statics
        for (int i = 0; i < Value.getXmlRoot().getResources().getStatics().getStatic().size(); i++) {
            String staticName = Value.getXmlRoot().getResources().getStatics().getStatic().get(i).getAlias();
            String staticValue = Value.getXmlRoot().getResources().getStatics().getStatic().get(i).getProcessedValue();

//            log.debug(staticName + "==" + property);
            if (staticName.equals(property)) {
                //log.debug(property + "==" + staticValue);
                return staticValue;
            }
        }

        //Iterate files
        for (int i = 0; i < Value.getXmlRoot().getResources().getFiles().getFile().size(); i++) {
            String fileAlias = Value.getXmlRoot().getResources().getFiles().getFile().get(i).getAlias();
            String fileName = Value.getXmlRoot().getResources().getFiles().getFile().get(i).getProcessedValue();

            //log.debug(fileAlias + "==" + property);
            if (fileAlias.equals(property)) {
                //log.debug(fileAlias + "==" + property);
                return fileName;
            }
        }

        return "";
    }

    //TODO
    //Replace all properties in string with values
    public static String replacePropWithValueInStr(String prepString) {
        return "";
    }


    public static enum logLevels {
        INFO,
        DEBUG
    }

    private static Value get() {
        return _ref;
    }
    //TODO: loglevel debug or info only so far?
    public static logLevels logLevel () {
        return logLevels.DEBUG;
    }

}
