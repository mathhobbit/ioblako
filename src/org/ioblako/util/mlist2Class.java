/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: mlist2Class.java,v 1.4 2010/02/08 04:52:23 nikitis Exp $
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

package org.ioblako.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import java.text.DateFormat;


import org.ioblako.core.move;
import org.ioblako.core.Log;
import org.ioblako.core.State;
import org.ioblako.core.schedule;
import org.ioblako.core.Time;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.w3c.dom.Document;


/**
 * This utility converts mlist into a class with the static main method.
 *
 * @author Sergey Nikitin
 * @since v1.1
 */

public class mlist2Class{

NodeList listOfMoves=null;

public static final String MOVE       ="move";
public static final String NAME       ="name";
public static final String SKIPFAIL   ="skipFail";
public static final String PARAMETER  ="parameter";
public static final String VALUE      ="value";

private static String LogFile=null;

	public static void main(String[] argv) throws Exception{
                   if(argv.length < 2){
                       System.out.println("Usage: mlist2Class "+ 
			       "mlist_location name_of_your_new_java_class");
		       System.exit(0);
		   }//if

		   File mlist = new File(argv[0]);
		   if(!mlist.exists()){
                         System.out.println("mlist: \""+
			     argv[0]+"\" does not exist!");
			 System.exit(0);
		   }//if

	          mlist2Class m2c = new  mlist2Class();

                  schedule myMlist = new schedule(argv[0]);
                  LogFile = myMlist.getLogFile();
		  m2c.parseMoves(argv[0]);
            if(myMlist.isScheduleMissing())
		  m2c.createClassWithoutSchedule(argv[1]);
	    else
		  m2c.createClassWithSchedule(argv[1]);
	}//main
/**
 * Extracts moves from an mlist defined by its location.
 * @param myLocation - mlist location
 *
 * @author Sergey NJikitin
 * @since 1.0 
 */
public void parseMoves(String myLocation) throws Exception{

         Document doc=null;
		     
		  //parser the document
 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       doc = docBuilder.parse(new File(myLocation));

		  
		 
		                                                        
		  Element lst = doc.getDocumentElement();
		  
		  listOfMoves = lst.getElementsByTagName(MOVE);
		 
		     lst=null;
		     doc=null;
		  
   }//public void parseMoves(){


public void createClassWithoutSchedule(String className) throws Exception{
PrintWriter os = new PrintWriter(new FileWriter(className+".java"));

         os.println("import org.ioblako.core.move;");
	 os.println("import org.ioblako.core.Log;");
         os.println("import org.ioblako.core.State;");
         os.println("import org.ioblako.util.mlist2Class;");

	os.println("import java.util.StringTokenizer;");
        os.println("import java.util.Hashtable;");

        os.println("public class "+className+"{");
	os.println("private static final String[] listOfmoves={");
	        Node myMove=null;
		   for(int i=0; i <listOfMoves.getLength();i++){
		                myMove = listOfMoves.item(i);
		                NamedNodeMap nmap = myMove.getAttributes();
		                Node name = nmap.getNamedItem(NAME);
		                Node skipFail = nmap.getNamedItem("skipFail");


                   String moveName=name.getNodeValue();
                   String continueOnFail="false";
                      if(skipFail != null)
                           continueOnFail=skipFail.getNodeValue();
		      moveName=moveName.trim()+","+continueOnFail.trim();
		              NodeList kids = myMove.getChildNodes();

			       for (int j = 0; j < kids.getLength();j++ ){

			              Node nd = kids.item(j);
			              String lName = nd.getNodeName();

			        	    if(lName != null)
				              if(lName.equals(PARAMETER)){
								                      
				                       nmap = nd.getAttributes();
			                               Node nameP = nmap.getNamedItem(NAME);
			                               Node valueP = nmap.getNamedItem(VALUE);                         

					        moveName=moveName+","+nameP.getNodeValue()+"="+valueP.getNodeValue(); 

					        }//if(lName.equals(PARAMETER))     



			     }//for 
	             if(i<listOfMoves.getLength()-1)
			 os.println("\""+moveName+"\",");
		     else
			 os.println("\""+moveName+"\"");
			       
		   }//for

	os.println("};");
	os.println("private static State myState=new State();");
	os.println(" ");
	os.println("public static void main(String[] argv) throws Exception{");
	if(LogFile != null)
	 os.println("Log.setLogFile(\""+LogFile+"\");");
	os.println("       for(int i = 0; i < listOfmoves.length;i++)");
	os.println("         mlist2Class.runMove(listOfmoves[i],myState);");
	os.println("    }");
	os.println("}");

   os.close();
   os=null;
	return;
}
public void createClassWithSchedule(String className) throws Exception{

PrintWriter os = new PrintWriter(new FileWriter(className+".java"));
os.println("import org.ioblako.core.move;");
os.println("import org.ioblako.core.Log;");
os.println("import org.ioblako.core.State;");
os.println("import org.ioblako.core.schedule;");
os.println("import org.ioblako.core.Time;");
os.println("import org.ioblako.util.mlist2Class;");

os.println("import java.util.StringTokenizer;");
os.println("import java.util.Hashtable;");
os.println("import java.util.Vector;");
os.println("import java.util.Date;");
os.println("import java.util.Locale;");

os.println("import java.text.DateFormat;");

        os.println("public class "+className+"{");
	os.println("private static final String[] listOfmoves={");
	        Node myMove=null;
		   for(int i=0; i <listOfMoves.getLength();i++){
		                myMove = listOfMoves.item(i);
		                NamedNodeMap nmap = myMove.getAttributes();
		                Node name = nmap.getNamedItem(NAME);
		                Node skipFail = nmap.getNamedItem("skipFail");


                   String moveName=name.getNodeValue();
                   String continueOnFail="false";
                      if(skipFail != null)
                           continueOnFail=skipFail.getNodeValue();
		      moveName=moveName.trim()+","+continueOnFail.trim();
		              NodeList kids = myMove.getChildNodes();

			       for (int j = 0; j < kids.getLength();j++ ){

			              Node nd = kids.item(j);
			              String lName = nd.getNodeName();

			        	    if(lName != null)
				              if(lName.equals(PARAMETER)){
								                      
				                       nmap = nd.getAttributes();
			                               Node nameP = nmap.getNamedItem(NAME);
			                               Node valueP = nmap.getNamedItem(VALUE);                         

					        moveName=moveName+","+nameP.getNodeValue()+"="+valueP.getNodeValue(); 

					        }//if(lName.equals(PARAMETER))     



			     }//for 
	             if(i<listOfMoves.getLength()-1)
			 os.println("\""+moveName+"\",");
		     else
			 os.println("\""+moveName+"\"");
			       
		   }//for

	os.println("};");
	os.println("private static State myState=new State();");
	os.println(" ");

	os.println("public static void main(String[] argv) throws Exception{");
        os.println("   if(argv.length <1){");
        os.println("                  System.out.println(\"An xml-file that defines schedule should be a command line argument.\");");
       	os.println("			                      return; ");
        os.println("    }");
        os.println(" mlist2Class.runMovesWithSchedule(argv[0],myState,listOfmoves);");
	os.println("}");
	os.println("}");
      //SERGEY


os.close();
	return;
}

public static void runMovesWithSchedule(String Location, State myState, String[] listOfmoves) throws Exception{
/*
     schedule myMlist = new schedule(Location);
     ArrayList<Time> timeList = myMlist.getDaySchedule(new Date());
               //Log.setLogFile(myMlist.getLogFile());
               Locale currentLocale = new Locale("en","US");
               DateFormat timeFormatter=DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG, currentLocale);
               timeFormatter.setLenient(true);
               Date current=null;
               long currentLong = 0;

																                if(timeList==null){
           Log.log("DEBUG","Today is a holiday!!!",LogFile);
           Log.log("DEBUG","The manager with mlist "+Location+" has finished its job list!",LogFile);
                       return;
     }//if
 while(timeList.size()>0){

															                         try{
          currentLong = System.currentTimeMillis();
        }
     catch(Exception ex){}

       Time tim=timeList.get(0);
       long timEnd = (tim.getEnd()).getTime();
       long timStart =(tim.getStart()).getTime();

           if((currentLong >timEnd)&& timeList.indexOf(tim)!= -1){
                          timeList.remove(timeList.indexOf(tim));
                          Date cur = new Date();

                      cur.setTime(currentLong);
          Log.log("DEBUG"," current time \""+timeFormatter.format(cur)+"\" is after time end="+ timeFormatter.format(tim.getEnd()),LogFile);

            }
           else{
               if((currentLong>timStart) && currentLong<timEnd){

                for(int i = 0; i < listOfmoves.length;i++)
                      mlist2Class.runMove(listOfmoves[i],myState);

               if(timeList.indexOf(tim)!=-1)
                       timeList.remove(timeList.indexOf(tim));
               }//if(current.compareTo(tim.getStart())>0 && current.compareTo(tim.getEnd())<0)
          }//else

  }//while    

*/
}//runMoveWithSchedule



public static void runMove(String mv, State myState) throws Exception{
             StringTokenizer tk = new StringTokenizer(mv,",");
             move newMove=null;
             String buf = null;
             String skipOnFail =  "";
             Hashtable<String,String> ht = new Hashtable<String,String>();
              if(tk.hasMoreTokens())
                         newMove = (move)Class.forName(tk.nextToken()).getDeclaredConstructor().newInstance();



                   if(tk.hasMoreTokens())
                      skipOnFail = tk.nextToken();

                while(tk.hasMoreTokens()){
                   buf = tk.nextToken();
                   ht.put(buf.substring(0,buf.indexOf("=")),
                   buf.substring(buf.indexOf("=")+"=".length()));
                 }//while

            newMove.setParameters(ht);
            newMove.setState(myState);
            newMove.start();

            if(!myState.isItSuccess() && skipOnFail.equals("false"))
                    throw new Exception("Move \""+mv+"\" failed.");


           newMove=null;
           tk = null;
           buf = null;
           skipOnFail = null;
           ht = null;
}//runMove


}//class
