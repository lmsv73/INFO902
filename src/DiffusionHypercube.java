import mpi.*;

public class DiffusionHypercube {

    public static void main(String args[]) throws Exception {
        // Version statique
        /*
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int last = size - 1;
        String bufferString[] = new String[1];
        bufferString[0] = "HELLO !";

        if (me == 0) {
            System.out.println("I'm <" + me + ">: send " + bufferString[0] + " to " + (me + 1) + " and " + last);
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me + 1, 99);
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, last, 99);
        }
        else {
            if (me <= size / 2) {
                final Status mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, me - 1, 99);
                System.out.println("I'm <" + me + ">: receive <" + bufferString[0] + "> from " + mps.source);
                if (me + 1 <= size / 2) {
                    MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me + 1, 99);
                }
            }
            else {
                final Status mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, (me + 1) % size, 99);
                System.out.println("I'm <" + me + ">: receive <" + bufferString[0] + "> from " + mps.source);
                if (me - 1 > size / 2) {
                    MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me - 1, 99);
                }
            }
        }
        MPI.Finalize();
        */

        final DiffusionHypercube diffusionHypercube = new DiffusionHypercube();
        if (args.length <= 3) {
            diffusionHypercube.start(args, 0);
        }
        else {
            int from = Integer.valueOf(args[3]);
            diffusionHypercube.start(args, from);
        }
    }

    private void start(final String args[], final int from) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int firstDef = 0;
        int lastDef = size / 2;
        int last = (size / 2 + me) % size;
        int next = (me + 1) % size;
        int prev = (me - 1 + size) % size;
        if (from < 0 || from >= size) {
            System.err.println("The given node must be in [0, " + (size - 1) + "]");
            MPI.Finalize();
            return;
        }
        String bufferString[] = new String[1];
        bufferString[0] = "hello";

        if (me == from) {
            System.out.println("<" + me + "> send to " + prev + " and " + next);
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, (me + 1) % size, 99);
            if (me - 1 < 0) {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, size - 1, 99);
            }
            else {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me - 1, 99);
            }
        }
        else {
            if ((me - from + size) % size < lastDef) {
                final Status mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, prev, 99);
                System.out.println("<" + me + ">: receive <" + bufferString[0] + "> from " + mps.source);
                if (next <= last) {
                    System.out.println("<" + me + ">: send <" + bufferString[0] + "> to " + next);
                    MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, next, 99);
                }
            }
            else {
                final Status mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, next, 99);
                System.out.println("<" + me + ">: receive <" + bufferString[0] + "> from " + mps.source);
                if (prev > last) {
                    System.out.println("<" + me + ">: send <" + bufferString[0] + "> to " + prev);
                    MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, prev, 99);
                }
            }
        }
        MPI.Finalize();
    }

}
