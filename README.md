# ioblako
A platform for rapid automation (even without human intervention). 
Ioblako was built for Linux, openBSD, FreeBSD and for majority of commercial operating systems. The core functionality was produced about 24 years ago.
Though it is only a tiny glimpse of a vast industry tested set of tools, I think, this snippet of code would be enough to show with examples how to build a system that can be self-managed, self-improved (learning stuff) 
and self-sustained with minimal or no human intervention.

## Compiling

One needs the following

- [openjdk](https://github.com/openjdk/jdk)

First, you need to install jdk (or build from scratch), it needs to be version higher than 11.
Then set the JAVA_HOME environment variable. For example if your jdk path is 

/usr/local/jdk 

then do 

export JAVA_HOME=/usr/local/jdk

Change directory to ioblako/build/bin and

sh ./imm core

sh ./imm mv

sh ./link


It will compile and build ioblako for you.
Different Unix systems might have different shells.
If the script "imm" misbehaves in your shell then you either tweak it appropriately or
use (the right system, Linux, just kidding). On most Unix systems you can install bash and execute imm with bash

bash ./imm core<br/>
bash ./imm mv<br/>
bash ./link

If you need to clean compiled classes and rebuild everything, then please make the script executable clean_class.sh,

chmod u+x clean_class.sh

and

bash ./clean_m

will clean binary leftovers from ioblako/build/modules sub-directories.

bash ./clean_l

will wipe out the content of ioblako/build/jmods and ioblako/build/lib

## Testing

The tests are in ioblako/projects/test. You need to add ioblako/build/ioblako/bin to your path or
simply change to the directory ioblako/projects/test and do, for example,

../../build/ioblako/bin/runMst -f \`pwd\`/test.mst

It will produce Test.log. Examine the file Test.log in order to see that everything is working fine.

Running

../../build/ioblako/bin/runMst

yields the following output

##

Usage: java core.runMst parameter_name=parameter_value [mlist.mst]

Usage: java core.runMst -l -t 20000 -f path_2_mlist

-l   Repeat given [mlist.mst] in the loop

-t   Define sleep time interval (in milliseconds) between two invocations of [mlist.mst] in the loop

-f   Path to [mlist.mst]









