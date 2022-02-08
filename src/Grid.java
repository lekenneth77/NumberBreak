import java.util.ArrayList;
import java.util.Random;

public class Grid {
    private static final int NUM_OF_TILES = 16;

    private ArrayList<int[]> grid;
    private int resetCount;
    private int mainNumber;
    private ArrayList<Integer> selectedNum;
    private ArrayList<Integer> selectedPos;


    public Grid(int resetNum) {
        grid = new ArrayList<>();
        selectedNum = new ArrayList<>();
        selectedPos = new ArrayList<>();
        resetCount = resetNum;
        fillGrid(resetCount);
    }

    /**
     * Fills the grid at the start of the game and sets the mainNumber.
     * @param resetCount
     */
    private void fillGrid(int resetCount) {
        Random rng = new Random();
        for (int i = 0; i < NUM_OF_TILES; i++) {
            int[] tile = {rng.nextInt(9) + 1, resetCount};
            grid.add(tile);
        }
        setMainNumber();
    }

    public void setMainNumber() {
        Random rng = new Random();
        mainNumber = rng.nextInt(9) + 1;
    }

    /**
     * Prints the grid out.
     */
    public void printGrid() {
        for (int i = 0; i < NUM_OF_TILES; i++) {
            if (i != 0 && i % 4 == 0) {
                System.out.println();
            }
            System.out.print(grid.get(i)[0] + " ");
        }
        System.out.println();
    }

    /**
     * Get the mainNumber of the grid
     */
    public int getMainNumber() {
        return mainNumber;
    }

    /**
     * Changes the middle numbers of the grid
     */
    public void changeCenterGrid() {
        Random rng = new Random();
        grid.get(5)[0] = rng.nextInt(9) + 1;
        grid.get(6)[0] = rng.nextInt(9) + 1;
        grid.get(9)[0] = rng.nextInt(9) + 1;
        grid.get(10)[0] = rng.nextInt(9) + 1;
    }
    /**
     * Checks if the given position is valid.
     */
    public boolean checkPos(int pos) {
        if(pos < 1 || pos > 17) {
            System.out.println("That's not a position on this board!");
        } else if (selectedPos.isEmpty() && (pos != 6 && pos != 7 && pos != 10 && pos != 11)) {
            System.out.println("You must start in the center! Choose Again!");
            //checks if the first selection is in the center
            return false;
        }
        else if (selectedPos.contains(pos) || grid.get(pos - 1)[0] == 0) {
            System.out.println("That coin is empty!");
            return false;
        }
        selectedPos.add(pos);
        return true;
    }

    /**
     * Returns the number at the given position and turns it into zero.
     */
    public int select(int pos) {
        pos--;
        int value = grid.get(pos)[0];
        selectedNum.add(value);
        if (pos != 5 && pos != 6 && pos != 9 && pos != 10) {
            grid.get(pos)[1]--;
            grid.get(pos)[0] = 0;
        }
        return value;
    }

    public int numOfSelected() {
        return selectedNum.size();
    }

    public int numBroken() {
        int count = 0;
        for (int pos : selectedPos) {
            if (pos != 6 && pos != 7 && pos != 10 && pos != 11) {
                count++;
            }
        }
        return count;
    }

    /**
     * Clears selection of numbers.
     */
    public void resetSelection() {
        selectedNum.clear();
        selectedPos.clear();
    }

    public void resetGrid() {
        Random rng = new Random();
        for (int i = 0; i < 16; i++) {
            if (i != 5 && i != 6 && i != 9 && i != 10) {
                int[] tile = grid.get(i);
                if (tile[0] == 0) {
                    if (tile[1] == 0) {
                        tile[0] = rng.nextInt(9) + 1;
                        tile[1] = resetCount;
                    } else {
                        tile[1]--;
                    }
                }
            }
        }
    }

    /**
     * Checks if there is anymore options
     */
    public boolean checkIfAllZero() {
        for (int i = 0; i < 16; i++) {
            if (i != 5 && i != 6 && i != 9 && i != 10) {
                if (grid.get(i)[0] != 0) {
                    return false;
                }
            }
        }
        return true;
    }














}
