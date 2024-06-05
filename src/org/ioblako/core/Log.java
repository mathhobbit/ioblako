/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: Log.java,v 1.3 2005/05/18 06:12:16 nikitis Exp $
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.text.DateFormat;

/**
 *
 * This is the class for writing debug and error messages to a given log file.
 * It prvides unified way for logging activity accross GNU6 environment.
 *
 * @author Sergey Nikitin  sergey@indexoffice.com, nikitin@asu.edu
 * @version 1.0
 */

public class Log {


public Log(){}
	/**
	 *
	 * Writes a message together with its status into a log-file
	 *
	 * @param Status status of a message, e.g., LOG:DEBUG
	 * @param Message message to be written into a log-file
	 */

	public static void log(String Status, String Message, String lfFile) {
		if (lfFile == null)
			return;
		DateFormat fmt = null;
		Date now = null;
		FileOutputStream fstr = null;
		try {
			fmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
			now = new Date();
			fstr = new FileOutputStream(lfFile, true);
			fstr.write((fmt.format(now) + ": " + Status + ": " + Message + "\n").getBytes());
			fstr.flush();
		} catch (Exception mre) {
			System.out.println("core.Log: Error when writing to Log File! " + mre.toString());
		} finally {
			if (fstr != null)
				try {
					fstr.close();
				} catch (IOException ioex) {
					System.out.println("core.Log: Error when writing to Log File! " + ioex.toString());
				}
			fstr = null;
			now = null;
			fmt = null;
		}

	}//public void log(String Status, String Message)

	/**
	 * Performs logging from a particular move. Move might have
	 * a particular move-specific file for logging.
	 *
	 * @param Status something like LOG:DEBUG or LOG:ERROR
	 * @param Message message to be written 
	 * @param mv move in which this logging is performed
	 *
	 */

	public static void log(String Status, String Message, move mv) {
		if (mv == null)
			return;
		String bdy = null;
		DateFormat fmt = null;
		Date now = null;
		FileOutputStream fstr = null;
		if (mv.HashMapLogStatus == null)
			bdy = Status + ": " + Message;
		else if (mv.HashMapLogStatus.containsKey(Status))
			bdy = Status + ": " + Message;
		if (mv.getLogFile() == null)
			return;
		if (bdy == null)
			return;

		try {
			fmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
			now = new Date();
			fstr = new FileOutputStream(mv.getLogFile(), true);
			fstr.write((fmt.format(now) + ": " + bdy + "\n").getBytes());
			fstr.flush();
		} catch (Exception mre) {
			System.out.println("core.Log: Error when writing to Log File! " + mre.toString());
			bdy = null;
		} finally {
			if (fstr != null)
				try {
					fstr.close();
				} catch (IOException ioex) {
					System.out.println("core.Log: Error when writing to Log File! " + ioex.toString());
				}
			fstr = null;
			bdy = null;
			now = null;
			fmt = null;
		}
	}//public void log(String Status, String Message)

	public static void log(String Status, String Message, Exception e, move mv) {
		if (mv == null)
			return;
		String bdy = null;
		DateFormat fmt = null;
		Date now = null;
		PrintStream ps = null;
		
		if (mv.HashMapLogStatus == null)
			bdy = Status + ": " + Message;
		else if (mv.HashMapLogStatus.containsKey(Status))
			bdy = Status + ": " + Message;
		if (mv.getLogFile() == null)
			return;
		if (bdy == null)
			return;

		try {
			fmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
			now = new Date();
			ps = new PrintStream(new FileOutputStream(mv.getLogFile(), true));
			ps.print(fmt.format(now) + ": " + bdy + "\n");
			e.printStackTrace(ps);
			ps.flush();
		} catch (Exception mre) {
			System.out.println("core.Log: Error when writing to Log File! " + mre.toString());
			bdy = null;
		} finally {
			if (ps != null) {
				ps.close();
				ps = null;
			}
			bdy = null;
			now = null;
			fmt = null;
		}
	}
}//public class Log
