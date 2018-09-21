import mpi.*;

public class DiffusionHypercube {

    public static void main(String args[]) throws Exception {
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
    }

    private void start(final String args[], final int from) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int last = size - 1;
        if (from < 0 || from >= size) {
            System.err.println("The given node must be in [0, " + (size - 1) + "]");
            MPI.Finalize();
            return;
        }
        String bufferString[] = new String[1];
        bufferString[0] = "HELLO !";

        if (me == from) {
            System.out.println("I'm <" + me + ">: send " + bufferString[0] + " to " + (me + 1) + " and " + last);
            MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, (me + 1) % size, 99);
            if (me - 1 < 0) {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, last, 99);
            }
            else {
                MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, me - 1, 99);
            }
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
    }

}
