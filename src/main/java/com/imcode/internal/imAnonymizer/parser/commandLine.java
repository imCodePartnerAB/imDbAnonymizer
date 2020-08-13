/*
 * Copyright (C) 2020. Jacob Sandin och imCode Partner AB - All Rights Reserved.
 * This file is part of the project dbanonymizer., and you may use, distribute and modify this code under the licence agreement terms you have with imCode Partner AB for the specific project.
 *
 */

package com.imcode.internal.imAnonymizer.parser;

import com.imcode.internal.imAnonymizer.log;
import org.apache.commons.cli.*;

/*
    Class to handle commandline parameters, and to notice missing such.
 */

//TODO: This class should be able to handle most of the functionality that the xmlfile supplies.
public class commandLine {
    Options options = new Options();
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();


    public commandLine() {
        //Add the --xmlfile parameter for connandline handling
        Option xmlFile = Option.builder().longOpt("xmlfile")
                .argName("file" )
                .required()
                .hasArg()
                .desc("Use this xmlfile to run." )
                .build();
        options.addOption( xmlFile );
    }

    //Return the currently supplied xmlfile from the commandline parameters
    public String getXMLFileName(String[] args) {
        try {
            CommandLine cmd = parser.parse(options, args);
            return cmd.getOptionValue("xmlfile");
        } catch (ParseException exp) {
            log.debug("Parsing failed.  Reason: " + exp.getMessage());
            formatter.printHelp("imAnonymizer", options);
        }
        return "";
    }

}
