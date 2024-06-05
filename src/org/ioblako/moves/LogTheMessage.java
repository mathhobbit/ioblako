package org.ioblako.moves;


import org.ioblako.core.move;
import org.ioblako.core.Log;
import org.ioblako.core.State;


public class LogTheMessage extends move{
public void run(){
String messagel="Hi there!!!";
 if(!parameters.containsKey("messagel")){
       Log.log("ERROR","org.ioblako.moves.LogTheMessage: Missing the required parameter: messagel",(move)this);
                     currentState.doneWithSuccess(false);
                                return;
 }
messagel=parameters.get("messagel");
//=========Your code goes after this line========================


       Log.log("DEBUG",messagel,(move)this);
       Log.log("INFO",messagel,(move)this);
       Log.log("MYLOG",messagel,(move)this);
       Log.log("TESTLOG",messagel,(move)this);
//=========The end of your code========================
                     currentState.doneWithSuccess(true);
}
}
