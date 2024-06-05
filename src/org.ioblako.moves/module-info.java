module org.ioblako.moves
{
        provides org.ioblako.core.move with
org.ioblako.moves.addMlist,
org.ioblako.moves.append,
org.ioblako.moves.clearState,
org.ioblako.moves.dumpMyPID,
org.ioblako.moves.exec,
org.ioblako.moves.log,
org.ioblako.moves.loopMlist,
org.ioblako.moves.LogStateVariable,
org.ioblako.moves.LogTheMessage,
org.ioblako.moves.setState,
org.ioblako.moves.startDaemon,
org.ioblako.moves.sleep,
org.ioblako.moves.terminate,
org.ioblako.moves.zip;

        requires java.base;
        requires transitive java.compiler;
        requires transitive org.ioblako.core;
        exports org.ioblako.moves;
}
