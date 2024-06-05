/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: move.java,v 1.6 2005/05/18 06:12:16 nikitis Exp $
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


import java.util.Hashtable;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
//import org.w3c.dom.Node;

/**
 *Abstract class move provides a prototype for the basic 
 *building blocks of GNU6 environment.
 *
 * @author Sergey Nikitin
 * @since 1.0
 */

public abstract class move implements Runnable {

schedule myMlist=null; //1st_build
HashMap<String,String> HashMapLogStatus=null;
protected Hashtable<String,String> parameters=null;
protected State  currentState=null;
private String LogFile=null;
private String LogStatus=null;
protected move myInstance=null;
protected ThreadGroup myThreadGroup=null;
protected Thread thisThread = null;
protected String myName=null;
//private volatile boolean amIactive=true;
//private volatile boolean amIdaemon=false;
private AtomicBoolean amIactive=new AtomicBoolean(true);
private AtomicBoolean amIdaemon=new AtomicBoolean(false);
private move parent=null;
//private volatile boolean lock=false;
//private volatile boolean amIsuspended=false;
private  AtomicBoolean lock=new AtomicBoolean(false);
private  AtomicBoolean _amIsuspended=new AtomicBoolean(false);
private boolean continueOnFail=false;
//private Node MlistNode=null;

public move(){
 this.myInstance=this;
}




private synchronized void requestLock(){
  while(lock.get())
   try{
     wait();
    }
   catch(java.lang.InterruptedException e){}
  lock.set(true);
  notifyAll();
  return;
}

private synchronized void releaseLock(){
  if(lock.get())
      lock.set(false);
  notifyAll();
  return;
}
public synchronized void amIsuspended() throws java.lang.InterruptedException{
    while(_amIsuspended.get())
         wait();
    notifyAll();
      return;
}

public boolean isAlive(){
requestLock();
    boolean ret = amIactive.get();
releaseLock();
return ret;
}
public void setAlive(boolean st){
requestLock();
    this.amIactive.set(st);
releaseLock();
}
/*
public void setMlistNode(Node newNode){
 this.MlistNode=newNode;
}
public Node getMlistNode(){
 return this.MlistNode;
}
*/
public void suspend() {
      requestLock(); 
       _amIsuspended.set(true);
      releaseLock();
       return;
}

public void resume(){
       requestLock(); 
        _amIsuspended.set(false);
       releaseLock();
         return;
}

public boolean isItSuspended(){
       requestLock(); 
        boolean ret = _amIsuspended.get();
       releaseLock();
         return ret;
}

public void setParent(move Parent){
   requestLock();
   this.parent = Parent;
   releaseLock();
}
public move getParent(){
   requestLock();
   move ret = this.parent;
   releaseLock();
   return ret;
}
public void setDaemon(boolean on){
  requestLock();
  this.amIdaemon.set(on);
  releaseLock();
}
public void setContinueOnFail(boolean on){
  this.continueOnFail=on;
}
public boolean continueOnFail(){
  return this.continueOnFail;
}

public String getName(){
 requestLock();
 String ret = myName;
 releaseLock();
 return ret;
}
/**
 * Sets the state of the current project.
 *
 * @param st State of the current project
 * @see org.ioblako.core.State
 * @since 1.1
 */
public void setState(State st){
     requestLock();
       this.currentState=st;
       if(st != null)
	st.SetIsItDone(false);
  releaseLock();
       return;
}
/**
 * Returns the state of the current project.
 * @return <CODE>State</CODE> - state of the current project.
 * @see org.ioblako.core.State
 */
public State getState(){
  requestLock();
     State ret= this.currentState;
   releaseLock();
   return ret;
}

/**
 * Sets parameters for the current move.
 */


public void setParameters(Hashtable<String,String> ht){
 requestLock();
     if(this != null)
         this.parameters=ht;
 releaseLock();
} 


/**
 * Returns parameters of the current move
 */

public Hashtable<String,String> getParameters(){
requestLock(); 
        Hashtable<String,String> ret = parameters;
releaseLock();
 return ret;

}

/**
 * Sets the log-file which will be used by this move
 */

public void setLogFile(String LogF){
requestLock();
  this.LogFile=LogF;
releaseLock();
}

/**
 * Sets the log-file status which will be used by this move
 */
public void setLogStatus(String Name){
if(Name == null)
   return;
releaseLock();
  this.LogStatus=Name;
if(Name != null){
    LogStatus = Name;
if(HashMapLogStatus == null){
    HashMapLogStatus = new HashMap<String,String>();
if(Name.indexOf(",")!=-1){
   StringTokenizer tk = new StringTokenizer(Name,",");
    while(tk.hasMoreTokens())
          HashMapLogStatus.put(tk.nextToken().trim(),"");
  }
else
 HashMapLogStatus.put(Name,"");
}

}
releaseLock();
}


/**
 * Returns the log-file for the current move
 */
public String getLogFile(){
requestLock();
 String ret = this.LogFile;
releaseLock();
return ret;
}
/**
 * Returns the log-file status for the current move
 */
public String getLogStatus(){
requestLock();
 String ret = this.LogStatus;
releaseLock();
return ret;
}
/**
 * Prepares move for the future garbage collection
 */
public void stop(){
requestLock();
myMlist=null; //1st_build comment it for the 1st build
myInstance= null;
  amIactive.set(false);
  parameters=null;
  LogFile=null;
  thisThread=null;
  myThreadGroup=null;
  myName=null;
  LogStatus=null;
if(this.currentState!=null)
  this.currentState.doneWithSuccess(false);
releaseLock();
  return;
}

/**
 * Assigns a name to the move
 */
public void setName(String newName){
requestLock();
       this.myName = newName;
releaseLock();
        return;
}

/**
 * Assigns a ThreadGroup to the move
 */

public void setThreadGroup(ThreadGroup newGroup){
requestLock();
       this.myThreadGroup = newGroup;
releaseLock();
        return;
}
/**
 * Assigns a Thread to the move
 */

public void setThread(Thread newThread){
requestLock();
       this.thisThread = newThread;
releaseLock();
        return;
}



/**
 * This method is the same as the one from the Thread class 
 */

public void sleep(int time) throws java.lang.InterruptedException{
if(thisThread == Thread.currentThread())
    Thread.sleep(time);
}
/**
 * This method is the same as the one from the Thread class 
 */

public void join() throws java.lang.InterruptedException{
 if(thisThread != null)
    thisThread.join();
  return;
}

public void join(long millis) throws java.lang.InterruptedException{
 if(thisThread != null)
    thisThread.join(millis);
  return;
}

/**
 * Starts execution of the current move.
 *
 * @since 1.0
 */


public void start() throws Exception{
requestLock();
if(thisThread == Thread.currentThread()){
releaseLock();
if(currentState != null)
    currentState.SetIsItDone(false);
this.run();
return;
}
if(myInstance == null){
releaseLock();
    throw new Exception("move.start(): ERROR: Can not start a thread without an instance. ");
}

 if(myThreadGroup != null){
         if(myName == null)
             thisThread = new Thread(myThreadGroup,myInstance,"move");
         else
             thisThread = new Thread(myThreadGroup,myInstance,myName);

     }//if
      else{
         if(myName == null)
             thisThread = new Thread(myInstance,"move");
         else
             thisThread = new Thread(myInstance,myName);
        }//else
 

   if(amIdaemon.get())
      thisThread.setDaemon(amIdaemon.get());
 releaseLock();
 thisThread.start();
  return;
}

/**
* Sets the instance of the move.
*/

public void setInstance(move newInstance){
requestLock();
   myInstance = newInstance;
 releaseLock();
   return; 
}

/**
* Returns count of all active neighbour threads in the group that 
*contains the current move. 
*/

public int activeCount(){
int ret=0;
requestLock();
 if(myThreadGroup != null)
      ret = myThreadGroup.activeCount();
 else
   if(thisThread != null)
       ret = (thisThread.getThreadGroup()).activeCount();    
releaseLock();    
return ret;
}//activeCount

/**
* This method is used to stop all threads from the current thread group.
*/

public synchronized void stopAll(){
ThreadGroup tg = myThreadGroup;

if (tg == null&& thisThread == null){
if(myInstance != null)
myInstance.stop();
notifyAll();
 return;
}
else
 if(thisThread != null && tg == null)
          tg=thisThread.getThreadGroup();
if(tg == null){
if(myInstance != null)
myInstance.stop();
notifyAll();
 return;
}

Thread[] tlist = new Thread[tg.activeGroupCount()];

int num=tg.enumerate(tlist,true);
move lc=null;
String main;
for(int i=0;i < num;i++){
    lc = (move)(Object)tlist[i];
main = lc.getName();
if(!main.equals("main"))
   lc.stop();
}
if(myInstance != null)
myInstance.stop();
notifyAll();
return;
}//stopAll

/**
* This method is used to iterrupt all threads from the current thread group.
*/

public void interruptAll(){
ThreadGroup tg = myThreadGroup;

if (tg == null&& thisThread == null)
 return;
else
 if(thisThread != null && tg == null)
          tg=thisThread.getThreadGroup();
if(tg == null)
 return;

Thread[] tlist = new Thread[tg.activeGroupCount()];

int num=tg.enumerate(tlist,true);

for(int i=0;i < num;i++)
   if(!tlist[i].isInterrupted())
       tlist[i].interrupt();

return;
}//interruptAll

public void interrupt(){
requestLock();
if(thisThread != null)
 if(!thisThread.isInterrupted())
 thisThread.interrupt();
releaseLock();
}



public Thread[] enumerateThreads(){
requestLock();
ThreadGroup tg = myThreadGroup;

if (tg == null&& thisThread == null){
 releaseLock();
 return null;
}
else
 if(thisThread != null && tg == null)
          tg=thisThread.getThreadGroup();
if(tg == null){
 releaseLock();
 return null;
}


int num = tg.activeCount();

Thread[] tlist = new Thread[num];

tg.enumerate(tlist);

releaseLock();
return tlist;
}//enumerate

public ThreadGroup[] enumerateThreadGroups(){
requestLock();

ThreadGroup tg = myThreadGroup;

if (tg == null&& thisThread == null){
releaseLock();
 return null;
}
else
 if(thisThread != null && tg == null)
          tg=thisThread.getThreadGroup();
if(tg == null){
releaseLock();
 return null;
}


int num = tg.activeGroupCount();

ThreadGroup[] glist = new ThreadGroup[num];

tg.enumerate(glist);
releaseLock();
return glist;
}//enumerate



}
