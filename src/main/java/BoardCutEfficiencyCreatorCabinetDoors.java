import java.util.*;

public class BoardCutEfficiencyCreatorCabinetDoors {
    private static Map<Double, Integer> cutList = new HashMap<Double, Integer>();
    private static List<Double> allCuts = new ArrayList<Double>();
    private static final double newBoardLength = 12d * 8d;
    private static final double sawBladeWidthInches = 1d/16d;
    private static final int numBoardsTotal = 500;
    private int numTrials = 1000000;
    private List<Double> optimalCutOrder;
    private double optimalLengthRemaining = 0;
    private double optimalNumBoardsUsed = 0;

    static {
        cutList.put(28.25d, 5);
        cutList.put(29.25d, 16);
        cutList.put(5.75d, 1);
        cutList.put(7.75, 2);

        for(Double key: cutList.keySet()) {
            for(int i = 0; i < cutList.get(key); i++) {
               allCuts.add(key);
            }
        }
    }

    public static void main(String[] args) {
        new BoardCutEfficiencyCreatorCabinetDoors().determineCuts();
    }

    private void determineCuts() {
        for(int i = 0; i < numTrials; i++) {
            determineOptimalCutOrderForTrial();
        }
        printOptimalOrder();
    }

    private void determineOptimalCutOrderForTrial() {
        int numBoardsUsed = 0;

        // Get some boards from lowes
        List<Double> boards = getBoards();

        // Randomize the all cuts list
        randomizeList(allCuts);

        // For each cut that we need
        for(Double cut: allCuts) {

            // Make sure the board is longenough
            double currentBoardLength = boards.get(0);

            if (cut.doubleValue() > currentBoardLength) {
                // Throw the rest of the board in the trash
                boards.remove(0);
                currentBoardLength = boards.get(0);
                if (boards.size() == 0){
                    throw new RuntimeException("You ran out of boards");
                }
            }
            // If the next board in line is a fresh board, and we have at least one more cut to make,
            // we need to count this board as a board that will be cut.
            if(currentBoardLength == newBoardLength) {
                numBoardsUsed++;
            }
            // Make the cut
            boards.set(0, boards.get(0) - cut - sawBladeWidthInches);
        }

        double remaining = 0;
        // Calculate wood remaining that wasn't trashed
        for(int i = 0; i < boards.size();            i++) {
            remaining += boards.get(i);
        }

        if (remaining > optimalLengthRemaining) {
            optimalLengthRemaining = remaining;
            optimalCutOrder = new ArrayList<Double>(allCuts);
            optimalNumBoardsUsed = numBoardsUsed;
        }

    }

    private void printOptimalOrder() {
        Double[] order = optimalCutOrder.toArray(new Double[optimalCutOrder.size()]);
        System.out.println("======================================================");
        System.out.println("Order = " + Arrays.toString(order));
        System.out.println("You used this many boards: " + optimalNumBoardsUsed);

    }

    private void randomizeList(List<Double> in) {
        Collections.shuffle(in);
    }

    public List<Double> getBoards() {
        List<Double> boards = new ArrayList<Double>();
        for(int i = 0 ; i < numBoardsTotal; i++) {
            boards.add(newBoardLength);
        }
        return boards;
    }

}
