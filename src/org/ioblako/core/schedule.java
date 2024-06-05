/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: schedule.java,v 1.7 2010/02/08 04:12:04 nikitis Exp $
*
*This program is free software; you can redistribute it and/or
*modify it under the terms of the GNU General Public License
*as published by the Free Software Foundation; either version 2
*of the License, or (at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program; if not, write to the Free Software
*Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package org.ioblako.core;

import java.util.Locale;
//import java.util.Vector;
import java.util.ArrayList;
//import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;


/**
 * This class handles the part of an mlist that contains a schedule.
 *
 *
 * @author Sergey Nikitin
 * @since 1.0
 * </CODE>
 */

public class schedule{

public boolean isMondayHoliday=false;
public boolean isTuesdayHoliday=false;
public boolean isWednesdayHoliday=false;
public boolean isThursdayHoliday=false;
public boolean isFridayHoliday=false;
public boolean isSaturdayHoliday=false;
public boolean isSundayHoliday=false;



public String Location=null;

public String LogFile=null;
public String LogStatus=null;

//private Log log = null;

//Vector<Date> holidays=null;
ArrayList<Date> holidays=null;

DateFormat dateFormatter=null;

//ArrayList<move>  Mlst = null;


/*
 * local information - language, country
 */

static public final String language="en";
static public final String country="US";






static public final String SCHEDULE         ="schedule";
static public final String MLIST            ="mlist";
static public final String EVERYDAY         ="everyday";
static public final String DATE             ="date";
static public final String LOG              ="Log";
static public final String FILE             ="file";
static public final String STATUS           ="status";

//done with DTD constants
//
private boolean  scheduleIsMissing=false;

/**
 * Returns true if the schedule is not defined in mlist.
 *
 * @return true if the schedule is not defined
 * @since 1.0
 */

public boolean isScheduleMissing(){
  return scheduleIsMissing;
}

/**
 * Creates an insatnce of class schedule corresponding to
 * the schedule defined in the specified mlist.
 * @param  location of an mlist
 */
public schedule(String mlistLocation){


Locale currentLocale = Locale.of(language,country);

dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                                   currentLocale);





  this.Location=mlistLocation;
 

//if(mlistLocation.toLowerCase().endsWith(".xml")) //Sergey
//  parseMlist();

//parseMst();  //Sergey

}//public mlist
private boolean MWasParsed=false;
private void parseM(){
if(!MWasParsed){
MWasParsed=true;
//if((this.Location).toLowerCase().endsWith(".xml")) //Sergey
//  parseMlist();
//else
parseMst();  //Sergey

}
}
private boolean parseMst(){
scheduleIsMissing=true;
try{
BufferedReader rd = new BufferedReader(new FileReader(Location));

String ln = null;
String NewMove = "_Empty_Move";
HashMap<String,String> prmtrs = new HashMap<String,String>();

while((ln = rd.readLine())!=null){
if(ln.indexOf("#")!=-1)
     ln=ln.trim().substring(0,ln.indexOf("#"));
  
  if(!ln.isEmpty()){
            if(ln.indexOf("=")!=-1)
                      prmtrs.put(ln.substring(0,ln.indexOf("=")),ln.substring(ln.indexOf("=")+1));
              if(ln.indexOf("=")==-1){
                if(NewMove.equals("_Empty_Move")){
                 NewMove = ln;
                 prmtrs = new HashMap<String,String>();
               }
              else{
                   if(NewMove.equals("org.ioblako.core.Log")){
                            if(prmtrs.containsKey("file"))
                                           LogFile = prmtrs.get("file");;
                            
                            if(prmtrs.containsKey("status"))
                                           LogStatus = prmtrs.get("status");
                            
                   }
            //here one needs to parse schedule, at the moment it is missing
            // and available only for xml mlists. 
                }//else
              }


   }

}//while

  if(rd !=null){
     rd.close();
     rd=null;
  }

}
catch(IOException e){
     System.out.println("ERROR: "+e.toString());
     return false;
}
return true;

}//parseMst

public String getLogFile(){
parseM();
  return LogFile;
}

public String getLogStatus(){
parseM();
  return LogStatus;
}
}//end of the class
