import mpi.*;

public class DiffusionAnneau {

    public static void main(String args[]) throws Exception {
        final DiffusionAnneau diffusionAnneau = new DiffusionAnneau();
        if (args.length <= 3) {
            diffusionAnneau.start(args, 0);
        }
        else {
            diffusionAnneau.start(args, Integer.valueOf(args[3]));
        }
    }

    private void start(final String args[], final int from) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (from < 0 || from >= size) {
            System.err.println("The given node must be in [0, " + (size - 1) + "]");
            MPI.Finalize();
            return;
        }
        String bufferString[] = new String[1];
        bufferString[0] = "HELLO";

        if (me == from) {
            System.out.println("<" + me + ">: send <" + bufferString[0] + ">");
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, (me + 1) % size, 99);
        }
        else {
            Status mps;
            if (me == 0) {
                mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, size - 1, 99);
            }
            else {
                mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, me - 1, 99);
            }
            System.out.println("<" + me + ">: receive <" + bufferString[0] + "> from " + mps.source);
            if ((me + 1) % size != from) {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, (me + 1) % size, 99);
            }
        }
        MPI.Finalize();
    }

    /*
    private void start(final String args[]) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        String bufferString[] = new String[1];
        bufferString[0] = "HELLO !";

        if (me == 0) {
            System.out.println("I'm <" + me + ">: send " + bufferString[0]);
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me + 1, 99);
        }
        else {
            final Status mps = MPI.COMM_WORLD.Recv( bufferString, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 99 );
            System.out.println("I'm <" + me + ">: receive " + bufferString[0]);
            if (me + 1 < size) {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me + 1, 99);
            }
        }
        MPI.Finalize();
    }
    */

}
