package social_distancing.data;

import java.util.List;

/**
 * Stores the resulting paths of players A and B.
 * Each entry represents their positions after one full time step.
 */
public class Paths {

    public final List<Integer> pathA;
    public final List<Integer> pathB;

    public Paths(List<Integer> pathA, List<Integer> pathB) {
        this.pathA = pathA;
        this.pathB = pathB;
    }

    public void printToConsole() {
        for (int v : pathA) System.out.print((v + 1) + " ");
        System.out.println();

        for (int v : pathB) System.out.print((v + 1) + " ");
        System.out.println();
    }
}
