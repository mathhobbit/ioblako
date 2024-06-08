/*
mlistChange*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: testMlist.java,v 1.3 2010/02/08 04:52:23 nikitis Exp $
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

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Hashtable;
import java.util.Set;
import java.text.DateFormat;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.ioblako.core.schedule;
import org.ioblako.core.Time;
import org.ioblako.core.move;
import org.ioblako.core.Log;



/**
 * This utility allows to test xml-mlist. It verifies whether
 * all the moves in mlist can be loaded.
 *
 * @author Sergey Nikitin
 * @since  v1.0
 */

public class testMlist{

public static final String language   ="en";
public static final String country    ="US";
public static final String FALSE      ="false";
public static final String TRUE       ="true";



public static final String MOVE       ="move";
public static final String NAME       ="name";
public static final String SKIPFAIL   ="skipFail";
public static final String PARAMETER  ="parameter";
public static final String VALUE      ="value";

String Location=null;
static DateFormat dateFormatter=null;
static DateFormat timeFormatter=null;
static Locale currentLocale=null;
NodeList listOfMoves=null;
schedule myMlist=null;
String LogFile=null;
private ArrayList<Time> timeList=null;
Document doc=null;
private static String LocationOfSourceCode=null;

public static void main(String[] argv) throws Exception{
if(argv.length==0){
       System.out.println("Usage: java testMlist [mlist.xml]");
       System.exit(0);
 }

if(currentLocale == null)
    currentLocale = Locale.of(language,country);

if(dateFormatter == null)
    dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                                   currentLocale);

if(timeFormatter == null)
 timeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG,currentLocale);

if(!argv[0].toLowerCase().endsWith(".xml")){
   testMlist.testMst(argv[0]);
   System.exit(0);
}

testMlist tm = new testMlist(argv);
if(!tm.parseMoves(argv[0])){
 System.out.println("Can not parse \""+argv[0]+"\"");
 System.exit(1);
}

if(argv.length == 2)
    testMlist.printXML(new File(argv[0]),new File(argv[1]));
else{
//tm.testSchedule(); 
tm.testMoves();
}
}//main
public testMlist(File xmlFile, move calledFromMove){
Location = xmlFile.getPath();
if(!xmlFile.isFile()){
  Log.log("ERROR","testMlist: "+Location+" is not a valid file!",calledFromMove); 
}
}

public testMlist(String arg) throws Exception{


Location = arg;

if(!(new File(Location)).exists())
    throw new Exception("Mlist has to be a valid XML-file!");
}//testMlist
public testMlist(String[] argv) throws Exception{


Location = argv[0];

if(!(new File(Location)).exists()){
 System.out.println("Mlist has to be a valid XML-file!");
 System.exit(1);
}

for(int i=0; i<argv.length; i++){
    if(argv[i].equals("-s"))
    	LocationOfSourceCode=argv[i+1];
}
if(!(new File(LocationOfSourceCode)).isDirectory())
	throw new Exception("The valid directory for source code should follow the switch -s");

}//testMlist


public boolean parseMoves(String myLocation){


doc=null;

//parser the document

 try{

DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       doc = docBuilder.parse(new File(Location));
       doc.getDocumentElement().normalize();
  }
 catch(IOException e){
     System.out.println("ERROR:"+e.toString());
     return false;
 }
catch(NullPointerException e){
     System.out.println("ERROR:"+e.toString());
     return false;
}
        catch(javax.xml.parsers.ParserConfigurationException e)
        {
            System.out.println("The XML parser could not read your input. Check it " +
                    "for errors and try again. "+e.toString());
        }
        catch (SAXParseException err)
        {
            System.out.println ("** Parsing error" + ", line "
                 + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(err.getMessage());
        }
        catch (SAXException e)
        {
            System.out.println(e.getMessage());
        }
catch(Exception e){
     System.out.println("ERROR:"+e.toString());
     return false;
}


Element lst = doc.getDocumentElement();

listOfMoves = lst.getElementsByTagName(MOVE);

lst=null;


return true;
}//public void parseMoves()

/*
public void testSchedule(){
         myMlist = new schedule(Location);
         LogFile = myMlist.getLogFile();

System.out.println("Testing "+Location);
System.out.println("------------------");

if(LogFile == null)
    System.out.println("Mlist \""+Location+"\" does not have log-file.");


if(myMlist.isScheduleMissing()){
               System.out.println("Mlist \""+Location+"\" does not have schedule.");
	     return;
}


    timeList = myMlist.getDaySchedule(new Date());

    System.out.println("Every day schedule");
    System.out.println("------------------");
   DateFormat dFormatter=null;

for (Iterator<Time> en = timeList.iterator();en.hasNext();){
               Time tim = en.next();

               Locale cLocale = Locale.of(language,country);

          DateFormat tmFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT,cLocale);
     dFormatter = DateFormat.getDateInstance(DateFormat.SHORT,
                            cLocale);
              tmFormatter.setLenient(true);

              System.out.println("        Start = "+tmFormatter.format(tim.getStart()));
       System.out.println("        End = "+tmFormatter.format(tim.getEnd()));
}//for

HashMap<Date,ArrayList<Time>> ht = myMlist.getSpecialDays();
if( !ht.isEmpty() ){
	System.out.println("------------");
	System.out.println("Special days");
	System.out.println("------------");
        
	for(Iterator<Date> en = ht.keySet().iterator();en.hasNext();){
       		Date sDay=en.next();
      		 ArrayList<Time> trm=ht.get(sDay);
		System.out.println("Special schedule for " + dFormatter.format(sDay));
		System.out.println("--------------------------- ");
     		for(Iterator<Time> tn=trm.iterator();tn.hasNext();){
     			    Time tim=tn.next();
        			 // Locale cLocale = new Locale(language,country);

			  // DateFormat tmFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT,cLocale);


       			     timeFormatter.setLenient(true);

			    System.out.println("        Start = "+timeFormatter.format(tim.getStart()));
 		   System.out.println("        End = "+timeFormatter.format(tim.getEnd()));
     		}//for

		System.out.println("--------------------------- ");

	}//for
}//if


ArrayList<Date> holidays =  myMlist.getHolidays();

if( !ht.isEmpty() ){
System.out.println("Holidays");
System.out.println("--------------------------- ");

     for(Iterator<Date> en=holidays.iterator(); en.hasNext();)
	               System.out.println(dFormatter.format(en.next()));
     if(myMlist.isMondayHoliday)
	     System.out.println("\"Monday\" is a holiday.");
     if(myMlist.isTuesdayHoliday)
	     System.out.println("\"Tuesday\" is a holiday.");
     if(myMlist.isWednesdayHoliday)
	     System.out.println("\"Wednesday\" is a holiday.");
     if(myMlist.isThursdayHoliday)
	     System.out.println("\"Thursday\" is a holiday.");
     if(myMlist.isFridayHoliday)
	     System.out.println("\"Friday\" is a holiday.");
     if(myMlist.isSaturdayHoliday)
	     System.out.println("\"Saturday\" is a holiday.");
     if(myMlist.isSundayHoliday)
	     System.out.println("\"Sunday\" is a holiday.");
}//if
else{
 System.out.println("--------------------------- ");
     if(myMlist.isMondayHoliday)
	     System.out.println("\"Monday\" is a holiday.");
     if(myMlist.isTuesdayHoliday)
	     System.out.println("\"Tuesday\" is a holiday.");
     if(myMlist.isWednesdayHoliday)
	     System.out.println("\"Wednesday\" is a holiday.");
     if(myMlist.isThursdayHoliday)
	     System.out.println("\"Thursday\" is a holiday.");
     if(myMlist.isFridayHoliday)
	     System.out.println("\"Friday\" is a holiday.");
     if(myMlist.isSaturdayHoliday)
	     System.out.println("\"Saturday\" is a holiday.");
     if(myMlist.isSundayHoliday)
	     System.out.println("\"Sunday\" is a holiday.");
}

 System.out.println("--------------------------- ");

 System.out.println("************************************************************");
 System.out.println("************************************************************");
Date today = new Date();
System.out.println("Schedule for today "+dFormatter.format(today));
System.out.println("------------------------------- ");

ArrayList<Time> tS = myMlist.getDaySchedule(today);

if(tS == null){
	System.out.println("Today is holiday!!!");
	return;
}

for(Iterator<Time> en=tS.iterator();en.hasNext();){
	 Time tim=en.next();
	   Locale cLocale = new Locale(language,country);

	     DateFormat tmFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT,
			                                           cLocale);


	         tmFormatter.setLenient(true);
	         /*
		      Date current=null;
		      try{
			          current = tmFormatter.parse(tmFormatter.format(new Date()));
		      }
		      catch(Exception ex){}
		      

		         System.out.println("        Start = "+tmFormatter.format(tim.getStart()));
			 System.out.println("        End = "+tmFormatter.format(tim.getEnd()));
}//for


System.out.println("------------------------------- ");

}//testSchedule
*/

public static void testMst(String fl)throws Exception{

System.out.println("Testing "+fl);

     BufferedReader rd = new BufferedReader(new FileReader(fl));
     String buf = null, ky="", vl="",replace_key="",replace_value="";
     Hashtable<String,String> replace = null;
     Hashtable<String,String> prmtrs = new Hashtable<String,String>();
     Iterator<String> it_replace=null;
     String NewMove = "_Empty_Move";
     while((buf=rd.readLine())!=null){
           buf=buf.trim();
           if(buf.indexOf("#")!= -1)
                 buf = buf.substring(0,buf.indexOf("#"));
          if(!buf.isEmpty()){
              if(buf.indexOf("=")!=-1){
                      ky = buf.substring(0,buf.indexOf("="));
                      vl = buf.substring(buf.indexOf("=")+1).trim().replace("\"","");
             if(replace!=null){
                        it_replace=replace.keySet().iterator();
                                    while(it_replace.hasNext()){
                                           replace_key=it_replace.next();
                                           replace_value=replace.get(replace_key);
                                       if(vl.indexOf(replace_key)!= -1)
                                           vl=vl.replace(replace_key,replace_value);
                                    }//while 
              }//if
		              System.out.println("     "+"       "+ ky+"=\""+vl+"\"");
                                 if(prmtrs.contains(ky)){
                                      System.out.println("=====================================================");
                                      System.out.println("WARNING!!! Possible problem with "+ky);
                                      System.out.println("There is already a parameter that has the value:");
                                      System.out.println(prmtrs.get(ky));
                                      System.out.println("=====================================================");
                                  }
                                else
                                  prmtrs.put(ky,vl);
               }

              if(buf.indexOf("=")==-1){
              if(!NewMove.equals("_Empty_Move")){
                   if(NewMove.equals("org.ioblako.core.Log")){
                            if(!prmtrs.containsKey("file"))
                                System.out.println("parameters \"file\" is required for \"org.ioblako.core.Log\""); 
                            if(!prmtrs.containsKey("status"))
                                System.out.println("parameters \"status\" is required for \"org.ioblako.core.Log\""); 
                            
                   }
                   else
                         testNewMove(NewMove, prmtrs);

                }
              else{
                   replace = prmtrs;
                }
                 NewMove = buf;
                  System.out.println(NewMove); 
                 prmtrs = new Hashtable<String,String>();
              }
           }
       }
                         testNewMove(NewMove, prmtrs);
     rd.close();
     rd = null;

}//testMst

private static void  testNewMove(String NewMove, Hashtable<String,String>prmtrs) {
         try{
	      Class.forName(NewMove);
           }
	 catch(ClassNotFoundException ex){
			 System.out.println("Class "+NewMove+" is missing. Generating the stub for the class.");
			 //Need to create move stub in the given source directory
	          try{
	        	 if(NewMove.indexOf(".")==-1)
	        		  throw new Exception("Cnan not create a stub for class wothout package!");
	 			 String[] pth=NewMove.split("\\.");
	 			 System.out.println(NewMove);
				 String myNewMove=LocationOfSourceCode.trim();
				 for(String arv:pth)
					 myNewMove=myNewMove+File.separator+arv;
				 myNewMove=myNewMove+".java";
				 (new File(myNewMove)).getParentFile().mkdirs();
				 System.out.println(myNewMove);
                                 if((new File(myNewMove)).exists())
                                     throw new Exception("org.ioblako.util.testMlist: "+myNewMove+" already exists!");
				 PrintWriter out = new PrintWriter(new FileWriter(myNewMove));
				 out.println("package "+NewMove.substring(0,NewMove.lastIndexOf("."))+";");
				 out.println("");
				 out.println("");
				 out.println("import org.ioblako.core.move;");
				 out.println("import org.ioblako.core.Log;");
				 out.println("import org.ioblako.core.State;");
				 out.println("");
				 out.println("");				 
				 out.println("public class "+NewMove.substring(NewMove.lastIndexOf(".")+1)+" extends move{");
                                 out.println("public void run(){");
                            Iterator<String> it = prmtrs.keySet().iterator();
                                     String prmKey="", prmValue="";
                                     while(it.hasNext()){
                                                prmKey=it.next();
                                                prmValue=prmtrs.get(prmKey);
					              System.out.println("     "+"       "+ prmKey+"=\""+prmValue+"\"");
					              out.println("String "+prmKey+"=\""+prmValue+"\";");
					              out.println(" if(!parameters.containsKey(\""+prmKey+"\")){");
					              out.println("       Log.log(\"ERROR\",\""+NewMove+": Missing the required parameter: "+prmKey+ "\",(move)this);");
					              out.println("                     currentState.doneWithSuccess(false);");
					              out.println("                                return;");
					              out.println(" }");
                                            out.println(prmKey+"=parameters.get(\""+prmKey+"\");");
                                        }//while
				 out.println("//=========Your code goes after this line========================");
				 out.println("");
				 out.println("");
				 out.println("//=========The end of your code========================");
				 out.println("                     currentState.doneWithSuccess(true);");
				 out.println("}");
				 out.println("}");
				 out.close();
				 out=null;
                    
                   }
	          catch(Exception ioe){
	        	  System.out.println(ioe.toString());
	          }
             }// catch(ClassNotFoundException ex)

}


public void testMoves(){
for(int j=0; j <listOfMoves.getLength();j++){

         Node myMove=listOfMoves.item(j);
          NamedNodeMap nmap = myMove.getAttributes();
          Node name = nmap.getNamedItem("name");
          Node skipFail = nmap.getNamedItem("skipFail");
	  if(name == null){
                System.out.println("ERROR: One or more moves do not have names!!!");
		return;
	  }
          String moveName = name.getNodeValue();
         try{
	      Class.forName(moveName).getDeclaredConstructor().newInstance();
          System.out.println("move "+j+") "+ moveName);
	  if(skipFail!=null)
          System.out.println("            "+"skipFail ="+skipFail.getNodeValue());
	  else
		  System.out.println("            "+"skipFail = false");
	  NodeList kids = myMove.getChildNodes();
          ArrayList<String> pValues=new ArrayList<String>();
	  if(kids != null)
	       for (int i = 0; i < kids.getLength();i++ ){
	                   Node nd = kids.item(i);
	                   String lName = nd.getNodeName();

	            if(lName != null)
		          if(lName.equals("parameter")){

		                   nmap = nd.getAttributes();
	                    Node nameP = nmap.getNamedItem("name");
	                     Node valueP = nmap.getNamedItem("value");
		              System.out.println("     "+"       "+ nameP.getNodeValue()+"=\""+valueP.getNodeValue()+"\"");
                                 if(pValues.contains(valueP.getNodeValue())){
                                      System.out.println("=====================================================");
                                      System.out.println("WARNING!!! Possible problem with "+nameP.getNodeValue());
                                      System.out.println("There is already a parameter that has the value:");
                                      System.out.println(valueP.getNodeValue());
                                      System.out.println("=====================================================");
                                  }
                                else
                                     pValues.add(valueP.getNodeValue());


                     }//if(lName.equals(PARAMETER))



        	 }//for
                 System.out.println(" ------------------------------------------------------- ");
	 }
	 catch(Exception e){
		  if(LocationOfSourceCode!=null)
		 try{
				 Class.forName(moveName);			 
		 }
		 catch(ClassNotFoundException ex){
			 System.out.println("move "+j+") "+ moveName);
			 System.out.println("Class "+moveName+" is missing. Generating the stub for the class.");
			 //Need to create move stub in the given source directory
	          try{
	        	 if(moveName.indexOf(".")==-1)
	        		  throw new Exception("Cnan not create a stub for class wothout package!");
	 			 String[] pth=moveName.split("\\.");
	 			 System.out.println(moveName);
				 String myNewMove=LocationOfSourceCode.trim();
				 for(String arv:pth)
					 myNewMove=myNewMove+File.separator+arv;
				 myNewMove=myNewMove+".java";
				 (new File(myNewMove)).getParentFile().mkdirs();
				 System.out.println(myNewMove);
                                 if((new File(myNewMove)).exists())
                                     throw new Exception("org.ioblako.util.testMlist: "+myNewMove+" already exists!");
				 PrintWriter out = new PrintWriter(new FileWriter(myNewMove));
				 out.println("package "+moveName.substring(0,moveName.lastIndexOf("."))+";");
				 out.println("");
				 out.println("");
				 out.println("import org.ioblako.core.move;");
				 out.println("import org.ioblako.core.Log;");
				 out.println("import org.ioblako.core.State;");
				 out.println("");
				 out.println("");				 
				 out.println("public class "+moveName.substring(moveName.lastIndexOf(".")+1)+" extends move{");
                                 out.println("public void run(){");
				 //SERGEY stopped here on 07/08/2011
				  NodeList kids = myMove.getChildNodes();
				  if(kids != null)
				       for (int i = 0; i < kids.getLength();i++ ){
				                   Node nd = kids.item(i);
				                   String lName = nd.getNodeName();

				            if(lName != null)
					          if(lName.equals("parameter")){

					                   nmap = nd.getAttributes();
				                    Node nameP = nmap.getNamedItem("name");
				                     Node valueP = nmap.getNamedItem("value");
					              System.out.println("     "+"       "+ nameP.getNodeValue()+"=\""+valueP.getNodeValue()+"\"");
					              out.println("String "+nameP.getNodeValue()+"=\""+valueP.getNodeValue()+"\";");
					              out.println(" if(!parameters.containsKey(\""+nameP.getNodeValue()+"\")){");
					              out.println("       Log.log(\"ERROR\",\""+moveName+": Missing the required parameter: "+nameP.getNodeValue()+ "\",(move)this);");
					              out.println("                     currentState.doneWithSuccess(false);");
					              out.println("                                return;");
					              out.println(" }");
                                            out.println(nameP.getNodeValue()+"=parameters.get(\""+nameP.getNodeValue()+"\");");
			                     }//if(lName.equals(PARAMETER))
				       }
				 out.println("//=========Your code goes after this line========================");
				 out.println("");
				 out.println("");
				 out.println("//=========The end of your code========================");
				 out.println("                     currentState.doneWithSuccess(true);");
				 out.println("}");
				 out.println("}");
				 out.close();
				 out=null;
	          }
	          catch(Exception ioe){
	        	  System.out.println(ioe.toString());
	          }
	 		 
		 }
		 System.out.println("move \""+moveName+"\": ERROR: "+ e.toString());
                 System.out.println(" ------------------------------------------------------- ");
	 }
}//for
							     
}//testMoves
public static void printXML(File srcFile, File dstFile, String moveToBeDelited) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out,moveToBeDelited);
out.println("</mlist>");
out.flush();
out.close();
out=null;
 
}


//new code Wednesday, December  7, 2011 08:28:53 AM PST

public static void printXML(File srcFile, File dstFile, Set<String> movesToBeDelited,HashMap<String,HashMap<String,String>> movesToBeAdded) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out,movesToBeDelited);

if(movesToBeAdded!=null){
 Iterator<String> it = movesToBeAdded.keySet().iterator();
 Iterator<String> ParameterNames=null;
 String moveName=null;
 String parameterName=null; 
 while(it.hasNext()){
     moveName=it.next();
     out.println("<move name=\""+moveName.trim()+"\" skipFail=\"false\">");
       ParameterNames=movesToBeAdded.get(moveName).keySet().iterator();  
         while(ParameterNames.hasNext()){
                  parameterName=ParameterNames.next();
                 out.println("<parameter name=\""+parameterName+"\" value=\""+movesToBeAdded.get(moveName).get(parameterName)+"\"/>");       
          }
     out.println("</move>");
 }
  


}
out.println("</"+root.getTagName()+">");
out.flush();
out.close();
out=null;
}

//done with new code Wednesday, December  7, 2011 08:28:53 AM PST



public static void printXMLWithDelParameter(File srcFile, File dstFile, String parameterNameToBeDeleted) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNodeWithDelParameter(nl.item(i),out,parameterNameToBeDeleted);
out.println("</mlist>");
out.flush();
out.close();
out=null;

}



public static void printXML(File srcFile, File dstFile, HashMap<String,HashMap<String,String>> mlistChange) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out,mlistChange,null);
out.println("</mlist>");
out.flush();
out.close();
out=null;
 
}
public static void printXML(File srcFile, File dstFile) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out);
out.println("</mlist>");
out.flush();
out.close();
out=null;
}
public static void printXML(File srcFile, File dstFile,
                     String newMoveName,HashMap<String,String> newMoveParameters) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out);
if(newMoveName!=null){
if(newMoveParameters==null)
  out.println("<move name=\""+newMoveName+"\"/>");
else{
  out.println("<move name=\""+newMoveName+"\">");
for(Iterator<String> en = newMoveParameters.keySet().iterator();en.hasNext();){
      String buf = en.next();
out.println("<parameter name=\""+buf+"\" value=\""+newMoveParameters.get(buf)+"\"/>");
}
  out.println("</move>");
     
}
}
out.println("</mlist>");
out.flush();
out.close();
out=null;
}

public static void printXML(File srcFile, File dstFile, String moveToBeUpdate, String parameterName, String parameterValue) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNode(nl.item(i),out,moveToBeUpdate,parameterName,parameterValue);
out.println("</mlist>");
out.flush();
out.close();
out=null;
}

public static HashMap<String,HashMap<String,String>> getAllMoves(File srcFile) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
NodeList nl = root.getChildNodes();
HashMap<String,HashMap<String,String>> ret= new HashMap<String,HashMap<String,String>> ();
for(int i = 0; i < nl.getLength();i++){
    if(!nl.item(i).getNodeName().startsWith("#")){
       NamedNodeMap nmp =  nl.item(i).getAttributes();
        Node nmpi=nmp.item(0);
        String ndV=nmpi.getNodeValue();
        HashMap<String,String>ht=new HashMap<String,String>();
        NodeList kids = nl.item(i).getChildNodes();    
     if(kids.getLength()>0)
      for(int j=0;j<kids.getLength();j++)
      if(!kids.item(j).getNodeName().startsWith("#"))
            ht.put(kids.item(j).getAttributes().item(0).getNodeValue(),
                   kids.item(j).getAttributes().item(1).getNodeValue());
        ret.put(ndV,ht);
 }
}
return ret;
}


public static void printNode(Node input,PrintWriter out, HashMap<String,HashMap<String,String>> mlistChange, HashMap<String,String> parameterChange){
String printIt = null;
if(input.getNodeName()!=null){ 
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;
String foundParameterToChange=null;
NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
          String ndV=nmpi.getNodeValue();
       if(ndV!=null){
         if(parameterChange == null){
          printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+ndV+"\"";
          if(mlistChange.containsKey(ndV))
                   parameterChange=mlistChange.get(ndV);
          }
         else{
                if(foundParameterToChange!=null){
                       printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+parameterChange.get(foundParameterToChange)+"\"";
                        foundParameterToChange=null;
                    }
                else
                       printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+ndV+"\"";
                if(parameterChange.containsKey(nmpi.getNodeValue()))
                         foundParameterToChange=nmpi.getNodeValue();

            }
         }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out,mlistChange, parameterChange);
       out.println("</"+input.getNodeName()+">"); 
}
}
public static void printNode(Node input,PrintWriter out, String moveToBeDelited){
String printIt = null;
if(input.getNodeName()!=null){
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;

NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null){
          if(nmpi.getNodeValue().equals(moveToBeDelited))
                       return;
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
          }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out);
       out.println("</"+input.getNodeName()+">");
}
}

//new code (Wednesday, December  7, 2011 08:33:08 AM PST)
public static void printNode(Node input,PrintWriter out, Set<String> movesToBeDelited){
String printIt = null;
if(input.getNodeName()!=null){
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;

NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null){
          if(movesToBeDelited.contains(nmpi.getNodeValue()))
                       return;
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
          }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out);
       out.println("</"+input.getNodeName()+">");
}
}


public static void printNodeWithOneMoveUpdate(Node input,PrintWriter out, String moveToBeUpdate, HashMap<String,String> parameterNameValue){
String printIt = null;
if(input.getNodeName()!=null){
  if(input.getNodeName().startsWith("#"))
     return;
  if(!input.getNodeName().equals("move")){
         testMlist.printNode(input,out);
         return;
   }
  
  printIt = "<"+input.getNodeName()+" ";
}
else
return;
boolean foundMoveToUpdate=false;
NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null){
          if(nmpi.getNodeValue().equals(moveToBeUpdate) && nmpi.getNodeName().equals("name"))
                       foundMoveToUpdate=true;
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
          }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
if(!foundMoveToUpdate){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
 if(parameterNameValue!=null){
   Iterator<String> it = parameterNameValue.keySet().iterator();
   String parameterName=null;
   while(it.hasNext()){
       parameterName=it.next();
       out.println("<parameter name=\""+parameterName+"\" value = \""+parameterNameValue.get(parameterName)+"\"/>");
   }
  }
       out.println("</"+input.getNodeName()+">");
  }
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out);
if(foundMoveToUpdate && parameterNameValue!=null){
   Iterator<String> it = parameterNameValue.keySet().iterator();
    String parameterName=null;
   while(it.hasNext()){
       parameterName=it.next();
       out.println("<parameter name=\""+parameterName+"\" value = \""+parameterNameValue.get(parameterName)+"\"/>");
   }
}
       out.println("</"+input.getNodeName()+">");
}
}
public static void printXMLwithOneMoveUpdate(File srcFile, File dstFile, String moveToBeUpdate, HashMap<String,String> parameterNameValue) throws Exception{
DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
Document doc = docBuilder.parse(srcFile);
       doc.getDocumentElement().normalize();
Element root = doc.getDocumentElement();
DocumentType dt = doc.getDoctype();
PrintWriter out = new PrintWriter(dstFile);
out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
if(dt.getInternalSubset()!=null){
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\"");
out.println("["+dt.getInternalSubset()+"]>");
}
else
out.println("<!DOCTYPE "+dt.getName()+ " SYSTEM \""+dt.getSystemId()+"\">");
out.println("<"+root.getTagName()+">");
NodeList nl = root.getChildNodes();
for(int i = 0; i < nl.getLength();i++)
      testMlist.printNodeWithOneMoveUpdate(nl.item(i),out,moveToBeUpdate,parameterNameValue);
 
out.println("</"+root.getTagName()+">");
out.flush();
out.close();
out=null;
}


//end of new code (Wednesday, December  7, 2011 08:33:08 AM PST)


public static void printNode(Node input,PrintWriter out, String moveToBeUpdate, String parameterName, String parameterValue){
String printIt = null;
if(input.getNodeName()!=null){
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;
boolean foundMoveToUpdate=false;
NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null){
          if(nmpi.getNodeValue().equals(moveToBeUpdate))
                       foundMoveToUpdate=true;
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
          }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
if(!foundMoveToUpdate){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
   out.println("<parameter name=\""+parameterName+"\" value = \""+parameterValue+"\"/>");
       out.println("</"+input.getNodeName()+">");
  }
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out);
if(foundMoveToUpdate)
   out.println("<parameter name=\""+parameterName+"\" value = \""+parameterValue+"\"/>");
       out.println("</"+input.getNodeName()+">");
}
}
//new code
public static void printNodeWithDelParameter(Node input,PrintWriter out, String parameterToBeDeleted){
String printIt = null;
if(input.getNodeName()!=null){ 
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;

NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null){
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
           if(input.getNodeName().equals("parameter"))
             if(nmpi.getNodeName().equals("name"))
                if(nmpi.getNodeValue().equals(parameterToBeDeleted))
                           return;
         }
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNodeWithDelParameter(kids.item(i),out,parameterToBeDeleted);
       out.println("</"+input.getNodeName()+">"); 
}
}
//done with new
public static void printNode(Node input,PrintWriter out){
String printIt = null;
if(input.getNodeName()!=null){ 
  if(input.getNodeName().startsWith("#"))
     return;
  printIt = "<"+input.getNodeName()+" ";
}
else
return;

NamedNodeMap nmp = input.getAttributes();
   for(int j=0;j<nmp.getLength();j++){
         Node nmpi=nmp.item(j);
       if(nmpi.getNodeValue()!=null)
         printIt=printIt+" "+nmpi.getNodeName()+"="+"\""+nmpi.getNodeValue()+"\"";
       else
         printIt=printIt+" "+nmpi.getNodeName();
     }
   NodeList kids = input.getChildNodes();
if(kids.getLength() == 0){
   printIt=printIt+"/>";
   out.println(printIt);
}
else{
   printIt=printIt+">";
   out.println(printIt);
      for(int i=0;i<kids.getLength();i++)
         testMlist.printNode(kids.item(i),out);
       out.println("</"+input.getNodeName()+">"); 
}
}

}//class
