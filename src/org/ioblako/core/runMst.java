/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: runMst.java,v 1.5 2010/02/08 04:12:04 nikitis Exp $
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
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;
import java.text.DateFormat;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


/**
 * This class executes sequentially moves from a given test file woth the extension ".mst".
 * 
 *
 * @author Sergey Nikitin
 * @since 1.0
 */


public class runMst extends move implements Runnable{

public static final String language   ="en";
public static final String country    ="US";
public static final String FALSE      ="false";
public static final String TRUE       ="true";



public static final String SKIPFAIL   ="skipFail";


private State myState=null;
//private static String mailHost=null;
private boolean shouldItLoop=false;
private boolean sendMail=false;
private int sleepTimeout=10000;

//Hashtable<String,move> activeMoves=new Hashtable<String,move>();
ArrayList<move> activeMoves=new ArrayList<move>();

String Location=null;
Date myDay=null;

static DateFormat dateFormatter=null;
static DateFormat timeFormatter=null; 
static Locale currentLocale=null;

String LogFile=null;
String LogStatus=null;


public  synchronized void sendMail( boolean yesNo){
      sendMail=yesNo;
      notifyAll();
}
public  synchronized long getSleepTimeout(){
      notifyAll();
   return sleepTimeout;
}
public synchronized void setSleepTimeout(int timeout){
   sleepTimeout = timeout;
        notifyAll();
}

public synchronized boolean doesItLoop(){
        notifyAll();
      return shouldItLoop;
}
public synchronized void shouldItLoop(boolean yesNo){
        shouldItLoop = yesNo;
        notifyAll();
}

public static void main(String[] argv) throws Exception{

  if(argv.length==0)
                    Usage(); 
   

if(currentLocale == null){
 //   Locale.Builder localeBuilder 
 //           = new Builder(); 
    //currentLocale = (new Builder().build()).of(language,country);
      currentLocale=Locale.of(language,country);
}

if(dateFormatter == null)
    dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                                   currentLocale);
if(timeFormatter == null)
    timeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG, currentLocale);


 Hashtable<String,String> startHashtable=new Hashtable<String,String>();
for (int i=0;i<argv.length ; i++)
                 switch(argv[i]){
                   case "-m":
                   break;
                   case "-l":
                   break;
                   case "-t":
                   break;
                   case "-f":
                   break;
                   default:
                    if(argv[i].indexOf("=")!=-1)
                      startHashtable.put(argv[i].substring(0,argv[i].indexOf("=")),argv[i].substring(argv[i].indexOf("=")+1).trim().replace("\"",""));
                   break;
                   }
 
    boolean loopFlag=false;
    boolean switchMail=false;
    int sleepTime=10000;
      for (int i=0;i<argv.length ; i++)
             switch(argv[i]){
                case "-m":
                       //  mailHost=argv[++i];
                       //  switchMail = true;
            System.out.println("This version does not support electronic mail messages.");
                break;
                case "-l":
                         loopFlag=true;
                break;
                case "-t":
                    try{
                     sleepTime=Integer.parseInt(argv[++i]);
                     }
                    catch(Exception e){
                                System.out.println(e.toString());
                                Usage(); 
                        }
                break;
                case "-f":
                     i++;
                     if(i >= argv.length)
                       Usage();
                     if(!(new File(argv[i])).exists())
                       Usage();
                     if((new File(argv[i])).isDirectory())
                       Usage();
                                       startHashtable.put("mlist",argv[i]);
               break;
               default:
                 break;
        }//switch
     
          if(!startHashtable.containsKey("mlist"))
                  Usage();
                                       runMst mn = new runMst(startHashtable);
                                                mn.shouldItLoop(loopFlag);
                                                mn.setSleepTimeout (sleepTime);
                                                //mn.sendMail(switchMail);
                                                mn.sendMail(false);
                                                mn.setState(null); 
                                                mn.setName("main");
                                                mn.start();
                                                mn=null;  

}

private static void Usage(){
                             System.out.println("Usage: java core.runMst parameter_name=parameter_value [mlist.mst]");
                             System.out.println("Usage: java core.runMst -l -t 20000 -f path_2_mlist");
                             System.out.println("-l   Repeat given [mlist.mst] in the loop");
                             System.out.println("-t   Define sleep time interval (in milliseconds) between two invocations of [mlist.mst] in the loop");  
                             System.out.println("-f   Path to [mlist.mst]");  
                             System.exit(0);   
}
/**
 * Constructs execution environment for an mlist which is specified
 * in the file
 *
 * @param hashtable - that should have at least the location of an mlist in a file system.
 * @author Sergey Nikitin
 * @since 2.0
 */
public runMst(Hashtable<String,String> startPrmtrs) throws Exception{
this.myState=new State();
this.myInstance=this;
this.Location = startPrmtrs.get("mlist");
this.parameters=startPrmtrs;
//this.setParameters(startPrmtrs);
// myDay=new Date();


if(Location == null){

  throw new Exception(" Location of a mlist has to be defined in order to start software integrator - ioblako!!!");

}
if(!(new File(Location)).exists()){
  
  throw new Exception("Mlist has to be a valid file in order to start software integrator - ioblako!!!");

}
if((new File(Location)).isDirectory()){
  
  throw new Exception("Mlist has to be a valid file in order to start software integrator - ioblako!!!");

}

String Log_File = null, Log_Status = "DEBUG,ERROR,INFO";
/*
if(Location.toLowerCase().endsWith(".mst")){
  Log_File = Location.substring(0,Location.lastIndexOf("."))+".log";
  myInstance.setLogFile(Log_File);
 }
else{
  Log_File = Location+".log";
  myInstance.setLogFile(Log_File);
}
*/
  myInstance.setLogStatus(Log_Status);
  myMlist = new schedule(Location);


     BufferedReader rd = new BufferedReader(new FileReader(new File(Location)));
     String buf = null;
     Hashtable<String,String> prmtrs = new Hashtable<String,String>();
     Hashtable<String,String> replace = null;
     String NewMove = "_Empty_Move";
     while((buf=rd.readLine())!=null){
           buf=buf.trim();
           if(buf.indexOf("#")!= -1)
                 buf = buf.substring(0,buf.indexOf("#"));
          if(!buf.isEmpty()){
              if(buf.indexOf("=")!=-1)
                      prmtrs.put(buf.substring(0,buf.indexOf("=")).trim(),buf.substring(buf.indexOf("=")+1).trim().replace("\"","").trim());
              else{
              if(!NewMove.equals("_Empty_Move")){
                   if(NewMove.equals("org.ioblako.core.Log")){
                               addNewMove(NewMove,replace, prmtrs);
                            if(prmtrs.containsKey("file")){
                                           Log_File = prmtrs.get("file");
                                           myInstance.setLogFile(Log_File);
                            }
                            if(prmtrs.containsKey("status")){
                                           Log_Status = prmtrs.get("status");
                                           myInstance.setLogStatus(Log_Status);
                            }
                   }
                   else
                         addNewMove(NewMove,replace, prmtrs);

                }
               else{
                         replace = prmtrs;
                              String startPrmtrs_Key="";
                              Iterator<String> it_prmtrs = startPrmtrs.keySet().iterator();
                              while(it_prmtrs.hasNext()){
                                  startPrmtrs_Key=it_prmtrs.next();
                                  replace.put(startPrmtrs_Key,startPrmtrs.get(startPrmtrs_Key));
                              }
                         it_prmtrs=null;
                         startPrmtrs_Key=null;
                  }
                 NewMove = buf.trim();
                 prmtrs = new Hashtable<String,String>();
              } 
           }
       }
                         addNewMove(NewMove,replace, prmtrs);
     rd.close();
     rd = null;
}


/**
 * Constructs execution environment for an mlist which is specified
 * in the file
 *
 * @param mlist_location - the location of an mlist in a file system.
 * @author Sergey Nikitin
 * @since 1.0
 */

public runMst(File mlist_location) throws Exception{
this.myState=new State();
this.myInstance=this;
this.Location = mlist_location.getPath();

Hashtable<String,String> ht = new Hashtable<String,String>();
ht.put("mlist",Location);
this.parameters=ht;
 myDay=new Date();


if(Location == null){

  throw new Exception(" Location of a mlist has to be defined in order to start software integrator - ioblako!!!");

}
if(!(new File(Location)).exists()){
  
  throw new Exception("Mlist has to be a valid file in order to start software integrator - ioblako!!!");

}
if((new File(Location)).isDirectory()){
  
  throw new Exception("Mlist has to be a valid file in order to start software integrator - ioblako!!!");

}

String Log_File = null, Log_Status = "DEBUG,ERROR,INFO";
/*
if(Location.toLowerCase().endsWith(".mst")){
  Log_File = Location.substring(0,Location.lastIndexOf("."))+".log";
  myInstance.setLogFile(Log_File);
 }
else{
  Log_File = Location+".log";
  myInstance.setLogFile(Log_File);
}
*/
  myInstance.setLogStatus(Log_Status);
  myMlist = new schedule(Location);


     BufferedReader rd = new BufferedReader(new FileReader(mlist_location));
     String buf = null;
     Hashtable<String,String> prmtrs = new Hashtable<String,String>();
     Hashtable<String,String> replace = null;
     String NewMove = "_Empty_Move";
     while((buf=rd.readLine())!=null){
           buf=buf.trim();
           if(buf.indexOf("#")!= -1)
                 buf = buf.substring(0,buf.indexOf("#"));
          if(!buf.isEmpty()){
              if(buf.indexOf("=")!=-1)
                      prmtrs.put(buf.substring(0,buf.indexOf("=")).trim(),buf.substring(buf.indexOf("=")+1).trim().replace("\"","").trim());
              else{
              if(!NewMove.equals("_Empty_Move")){
                   if(NewMove.equals("org.ioblako.core.Log")){
                               addNewMove(NewMove,replace, prmtrs);
                            if(prmtrs.containsKey("file")){
                                           Log_File = prmtrs.get("file");
                                           myInstance.setLogFile(Log_File);
                            }
                            if(prmtrs.containsKey("status")){
                                           Log_Status = prmtrs.get("status");
                                           myInstance.setLogStatus(Log_Status);
                            }
                   }
                   else
                         addNewMove(NewMove,replace, prmtrs);

                }
               else{
                         replace = prmtrs;
                  }
                 NewMove = buf.trim();
                 prmtrs = new Hashtable<String,String>();
              } 
           }
       }
                         addNewMove(NewMove,replace, prmtrs);
     rd.close();
     rd = null;
}

private void addNewMove(String NewMove,Hashtable<String,String> replace,  Hashtable<String,String>prmtrs) throws Exception{

                    if(replace != null && prmtrs != null)
                          if(!replace.isEmpty() && !prmtrs.isEmpty()){
                            
                              Iterator<String> it_replace=null; 
                              Iterator<String> it_prmtrs = prmtrs.keySet().iterator();
                              String prmtrs_key="", prmtrs_value="", replace_key="", replace_value=""; 
                              while(it_prmtrs.hasNext()){
                                   prmtrs_key=it_prmtrs.next();
                                   prmtrs_value=prmtrs.get(prmtrs_key);
                                it_replace=replace.keySet().iterator();

                                    while(it_replace.hasNext()){
                                           replace_key=it_replace.next();
                                           replace_value=replace.get(replace_key);
                                       if(prmtrs_value.indexOf(replace_key)!= -1){
                                           prmtrs_value=prmtrs_value.replace(replace_key,replace_value);
                                           prmtrs.put(prmtrs_key,prmtrs_value);
                                        }
                                    }//while 
                               }//while
                               it_replace = null;
                               it_prmtrs = null;
                           }
                     if(NewMove.equals("org.ioblako.core.Log"))
                                        return; 
                    move  nMove = (move)Class.forName(NewMove).getDeclaredConstructor().newInstance();
                   if(prmtrs.containsKey(SKIPFAIL))
                        nMove.setContinueOnFail(prmtrs.get(SKIPFAIL).equalsIgnoreCase("true"));
                   else
                       nMove.setContinueOnFail(false);
                    nMove.setParameters(prmtrs);
                    nMove.setLogFile(myInstance.getLogFile());
                    nMove.setLogStatus(myInstance.getLogStatus());
                    nMove.setState(myState);
                    nMove.setName(NewMove);
                   if(myThreadGroup != null)
                          nMove.setThreadGroup(myThreadGroup);
                  nMove.setThread(thisThread);
                    nMove.setParent(myInstance);
                    activeMoves.add(nMove);
}




/**
 * Runs the current mlist.
 *
 * @since 1.0
 */

public void run(){
boolean parentIsRunning = true;
if (thisThread != Thread.currentThread())
 return;
if(myInstance == null)
 return;
move prnt = myInstance.getParent();
if(prnt != null)
      parentIsRunning=prnt.isAlive();

    Log.log("DEBUG","Software integrator with mlist "+Location+" is active!",myInstance);



timeFormatter.setLenient(true);
Date current = null;
long currentLong = 0;

       Log.log("DEBUG","running mlist without time schedule.",myInstance);
       while(parentIsRunning && this.isAlive()){
 try{
  amIsuspended();
 }
 catch(java.lang.InterruptedException intE){
                Log.log("DEBUG",intE.toString(),myInstance);

}
          runMoves();
     if(shouldItLoop)
        try{
          sleep(sleepTimeout);
       if(prnt != null)
         parentIsRunning=prnt.isAlive();
         }
        catch(java.lang.InterruptedException eI){
                Log.log("DEBUG",eI.toString(),myInstance);
         }
     else
        break;
        }//while
if(currentState!=null){
	//communicating to the future moves about my state
try{
 if(myState.isItSuccess())
      currentState.doneWithSuccess(true);
  else
      currentState.doneWithSuccess(false);
}
catch(InterruptedException iex){
 Log.log("DEBUG",iex.toString(),myInstance);   
      currentState.doneWithSuccess(false);
}
  currentState.put(Location,myState);
}

 Log.log("DEBUG","The software integrator with mlist "+Location+" has finished its job list!",myInstance);   
 //listOfMoves=null;
 //timeList=null;
 //current=null;
 //activeMoves=null;
 //prnt=null;
 //System.gc();
return;   
}//public void run()




/**
 * Executes sequentially moves
 * @author Sergey Nikitin
 * @since 1.0
 */

private void runMoves(){
	move myMove=null;
                   for(int i=0;(i <activeMoves.size()) && this.isAlive();i++){
                        
                           myMove = activeMoves.get(i);

                            String moveName=myMove.getName(); 
                      Log.log("DEBUG","Starting move with name \""+moveName+"\"`",myInstance); 
                     if(myMove.continueOnFail())
                      Log.log("DEBUG",moveName+" has skipFail = \"true\"`",myInstance); 
                     else
                      Log.log("DEBUG",moveName+" has skipFail = \"false\"`",myInstance); 

                     try{
                        amIsuspended();
                        if(myState == null)
                             return;
                        myState.SetIsItDone(false);
                        myMove.setThread(thisThread);
                        myMove.run();

                         if(myState == null || myInstance == null)
                             return;
                          if(myState.isItSuccess()){
                                 Log.log("INFO","Move "+moveName+" was completed with success",myInstance);
                            if(sendMail){
                            //    mailThread sMail = new mailThread(mailHost,myMove.getMlistNode(),"true",myInstance);
                             //   sMail.start();
                             //   sMail=null;
                             Log.log("DEBUG","Move "+moveName+": electronic mail is not supported.",myInstance);
                              }
                          } 
                        else{
                              Log.log("ERROR","Move "+moveName+" failed",myInstance);
                          if(sendMail){
                              //  mailThread sMail = new mailThread(mailHost,myMove.getMlistNode(),"false",myInstance);
                              //  sMail.start();
                              //  sMail=null;
                             Log.log("DEBUG","Move "+moveName+": electronic mail is not supported.",myInstance);
                            }
                            if(!myMove.continueOnFail()){   
                          myMove = null;
                         }
                       }
                     }//try
                     catch(Exception ex){
                              Log.log("ERROR","exception in the move "+moveName+": "+ex.toString(),myInstance);
                            if(!myMove.continueOnFail())   
                                   break;
                       }//catch   
                    }//for            
}






}//end of class


