/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: Time.java,v 1.1.1.1 2003/01/23 04:54:34 nikitis Exp $
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

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.StringTokenizer;


/**
 * This is a storage class for time string of mlist. It can return start and end time
 * for a process.
 *
 * @author Sergey Nikitin
 * @since 1.0
 */


public class Time{

public static final String language="en";
public static final String country ="US";


public Date Start=null;
public Date End=null;
public static String TimeZone="PST";

private Locale currentLocale = Locale.of(language,country);

// should have LONG in the date format to extract the system TimeZone
private DateFormat timeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG, currentLocale);

/**
 * Creates an instance of the class Time.
 * @param startTime-time to start process
 * @param endTime-the moment of time after which the process can not be executed.
 * @exception Exception if the arguments are not correctly defined.
 */

public static Time timeFactory(String startTime, String endTime) throws Exception{

  Time instance = new Time(startTime, endTime, null);

 return instance;

}

/**
 * Creates an instance of the class Time for a particular time zone.
 * @param startTime-time to start process
 * @param endTime-the moment of time after which the process can not be executed.
 * @exception Exception if the arguments are not correctly defined.
 */
 
public Time(String startTime, String endTime, String timeZone) throws Exception{

String today = null;
StringTokenizer tk = new StringTokenizer(timeFormatter.format(new Date()));

 while(tk.hasMoreTokens())
	 Time.TimeZone = tk.nextToken();


if(startTime == null || endTime == null)
   return;

 
		  DateFormat justDate = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
                  timeFormatter.setLenient(true);

               try{ 
		  today = justDate.format(new Date());
                  Start = timeFormatter.parse(today+" "+startTime+" "+TimeZone);
                }
               catch(ParseException pe){
                       throw new Exception("Wrong time format! "+
                                                                      pe.toString());
                   }     
               try{ 
                  End = timeFormatter.parse(today+" "+endTime+" "+TimeZone);
                }
               catch(ParseException pe){
                       throw new Exception("Wrong time format! "+
                                                                      pe.toString());
                   }     

}

/**
* Returns the date which is starting moment stored in the instance of the class Time.
* @return the moment of time after which the process can be executed.
* @since 1.0
*/

public Date  getStart(){
 
 return Start;

}
/**
* Sets the date which is the starting moment stored in the instance of the class Time.
* @since 1.0
*/
public void  setStart(Date sTart){
 this.Start=sTart;
}

/**
* Returns the date which is the terminal moment stored in the instance of the class Time.
* After this moment the process can not be executed.
* @return the moment after which the process can not be executed.
* @since 1.0
*/
public Date  getEnd(){
 return End;
}

/**
 * Sets a terminal date for the process. After this date the process can not be executed.
 * @param the terminal date for the process.
 * @since 1.0
 */
public void  setEnd(Date eNd){
 this.End=eNd;
}


/**
* Returns the time zone used by the instance of the class Time.
* @return time zone in use
* @since 1.0
*/
   public String getTimeZone(){
              return TimeZone;
     }
}
