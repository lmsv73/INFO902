import mpi.*;

public class DiffusionCentralisee {

    public static void main(String args[]) throws Exception {
        final DiffusionCentralisee diffusionCentralisee = new DiffusionCentralisee();
        if (args.length <= 3) {
            diffusionCentralisee.start(args, 0);
        }
        else {
            int from = Integer.valueOf(args[3]);
            diffusionCentralisee.start(args, from);
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

        if (me == from) {
            bufferString[0] = "coucou";
            System.out.println("I'm <" + me + ">: send " + bufferString[0]);
            for (int i = 0; i < size; i++) {
                if (i != from) {
                    MPI.COMM_WORLD.Send(bufferString, 0, 1, MPI.OBJECT, i, 99);
                }
            }
        } else {
            final Status mps = MPI.COMM_WORLD.Recv(bufferString, 0, 1, MPI.OBJECT, from, 99);
            System.out.println("I'm <" + me + ">: receive " + bufferString[0]);
        }
        MPI.Finalize();
    }

}
