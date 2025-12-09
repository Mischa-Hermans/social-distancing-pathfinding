package social_distancing.data;

import java.util.List;

/**
 * Holds the final paths for players A and B.
 * Each list entry corresponds to a time step, after both players have moved.
 */
public class Paths {

    public final List<Integer> pathA;
    public final List<Integer> pathB;

    public Paths(List<Integer> pathA, List<Integer> pathB) {
        this.pathA = pathA;
        this.pathB = pathB;
    }

    public void printToConsole() {
        // Path A
        for (int v : pathA) System.out.print((v + 1) + " ");
        System.out.println();

        // Path B
        for (int v : pathB) System.out.print((v + 1) + " ");
        System.out.println();
    }
}
