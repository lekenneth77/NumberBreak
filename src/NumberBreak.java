import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class NumberBreak {

    private static int numRounds;
    private static int winningPoints;
    private static int totalSeconds;

    private static Grid grid;


    private static int interval;
    private static Timer timer;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("****WELCOME TO NUMBER BREAK****");
        System.out.print("Would you like the instructions on how to play?: Input Y for Yes and N for No: ");
        if (sc.next().toUpperCase().charAt(0) == 'Y') {
            displayInstructions();
        }
        playGame();
    }

    private static void playGame() {
        boolean continuePlaying = true;
        Scanner sc = new Scanner(System.in);
        while (continuePlaying) {
            setup(sc);
            System.out.println("\n!!! GAME START !!!");
            int currentScore = 0;
            int roundSelectAmt = 0;
            int chain = 1;
            boolean victory = false;
            System.out.println("\nYou have a total of " + totalSeconds + " seconds for each round.");
            for (int currentRound = 1; currentRound <= numRounds; currentRound++) {
                System.out.println();
                startRound(currentRound);
                System.out.println("This is your current score: " + currentScore + " / " + winningPoints);
                System.out.println("You currently have a Chain of " + chain + "x using a Coin Count of: "
                        + roundSelectAmt);
                currentRound = playRound(currentRound);
                if (currentRound <= numRounds) {
                    if (roundSelectAmt == grid.numOfSelected()) {
                        chain++;
                    } else if (chain == 1) {
                        roundSelectAmt = grid.numOfSelected();
                    } else {
                        chain = 1;
                    }
                    currentScore += (grid.numBroken() * chain);
                    if (currentScore >= winningPoints) {
                        currentRound = numRounds;
                        victory = true;
                    }
                    if (!victory) {
                        grid.resetSelection();
                        grid.resetGrid();
                        grid.setMainNumber();
                    }
                }
            }
            continuePlaying = postGame(victory);
        }
    }

    /**
     * Sets up the grid for the current game.
     * @param sc
     */
    private static void setup(Scanner sc) {
        boolean diffCheck = true;
        while (diffCheck) {
            System.out.println("The difficulty options are: EASY MEDIUM HARD CUSTOM");
            System.out.print("Enter the difficulty you would like: ");
            String diff = sc.next().toUpperCase();
            if (diff.equals("EASY")) {
                numRounds = 10;
                winningPoints = 15;
                totalSeconds = 120;
                grid = new Grid(3);
                diffCheck = checkDiff(diff, 3, sc);
            } else if (diff.equals("MEDIUM")) {
                numRounds = 20;
                winningPoints = 40;
                totalSeconds = 60;
                grid = new Grid(4);
                diffCheck = checkDiff(diff, 4, sc);

            } else if (diff.equals("HARD")) {
                numRounds = 15;
                winningPoints = 50;
                totalSeconds = 45;
                grid = new Grid(4);
                diffCheck = checkDiff(diff, 4, sc);

            } else if (diff.equals("CUSTOM")) {
                System.out.print("Enter the number of rounds you would like to play: ");
                numRounds = Math.abs(sc.nextInt());
                System.out.print("Enter how many points you must achieve to win: ");
                winningPoints = Math.abs(sc.nextInt());
                System.out.print("Enter how many seconds would you like in each round: ");
                totalSeconds = Math.abs(sc.nextInt());
                System.out.print("Enter how many rounds it would take for the coins to reappear: ");
                int resetCount = sc.nextInt();
                grid = new Grid(Math.abs(resetCount));
                diffCheck = checkDiff(diff, resetCount, sc);
            } else {
                System.out.println("That's not a valid difficulty!");
            }
        }
    }

    private static boolean checkDiff(String diff,int resetCount, Scanner sc) {
        if (!diff.equals("CUSTOM")) {
            System.out.println(diff + " Difficulty:\n" + numRounds + " Rounds\n" + winningPoints +
                    " Points Required\n" + totalSeconds + " Seconds per Round\nNumbers will reappear after " + resetCount + " Turns Have Passed");
        }
        System.out.print("Are you sure about this difficulty? Y for Yes, N for No: ");
        if (sc.next().toUpperCase().charAt(0) == 'Y') {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Prints out the start of each round.
     */
    private static void startRound(int currentRound) {
        System.out.println("**** ROUND " + currentRound + " ****");
        System.out.println("The main number is: " + grid.getMainNumber());
        if (currentRound % 3 == 0) {
            grid.changeCenterGrid();
        }
        System.out.println("This is what the current grid looks like - ");
        grid.printGrid();
        showTimer();
    }

    /**
     * Plays the round out.
     */
    private static int playRound(int currentRound) {
        Scanner sc = new Scanner(System.in);
        int sum = 0;
        boolean continueGame = true;
        while(continueGame) {
            if (interval == 0) {
                timer.cancel();
                System.out.println("You've run out of time!");
                return numRounds + 1;
            } else {
                System.out.print("Enter the position of the coin you would like to add to your current sum: ");
                int pos = sc.nextInt();
                if (grid.checkPos(pos)) {
                    sum += grid.select(pos);
                    System.out.println("This is your current sum: " + sum);
                    if (sum % grid.getMainNumber() == 0) {
                        timer.cancel();
                        return currentRound;
                    } else if (grid.checkIfAllZero()) {
                        timer.cancel();
                        System.out.println("You've ran out of options!");
                        return numRounds + 1;
                    }
                }
            }

        }
        return 0;
    }

    private static boolean postGame(boolean victory) {
        Scanner sc = new Scanner(System.in);
        if (victory) {
            System.out.println("Congratulations! You won the game!");
        } else {
            System.out.println("Sucks! You lost!");
        }
        System.out.println();
        System.out.print("Would you like to play another game of Sphere Break? Enter Y for Yes and N for No: ");
        if (sc.next().toUpperCase().charAt(0) != 'Y') {
            System.out.println("Thanks for playing!");
            return false;
        }
        return true;
    }

    private static void showTimer() {
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = totalSeconds;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                setInterval();
            }
        }, delay, period);

    }

    private static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }

    private static void displayInstructions() {
        System.out.println("The main purpose of this game is to add up numbers to create a sum that can divide" +
                " evenly by the given main number. \nThe grid is set up in a 4x4 format. You will select coins (numbers)" +
                " by providing the position of the coin when asked for it. \nThe positions will be laid out like this: ");
        System.out.println("1 2 3 4\n5 6 7 8\n9 10 11 12\n13 14 15 16");

        System.out.println("At the start of every round, the main number will be changed, and you will have a time limit" +
                " to find a valid sum. You will then be asked to start selecting.");
        System.out.println("When selecting coins, you must always at the start of that round choose a coin from the center. (Positions 6 7 10 11)");
        System.out.println("This will be your \"free space\" as these coins will not disappear after a round when selected.");
        System.out.println("You will however not get points for selecting those coins.");
        System.out.println("You will obtain a point for each coin outside of the center you use in your sum.");
        System.out.println("These coins will disappear when you use them for a round. They will reappear with a new value, you can set" +
                " how long it takes for the coins to reappear at the start of the game.");
        System.out.println("If you choose the same amount of coins to create a valid sum in a row, you will get a " +
                "chain bonus.");
        System.out.println("This will multiply your score based on how long your chain has lasted.");
        System.out.println("Once you reach the required amount of points you will win the game.");
        System.out.println("If you end up with no more valid coins to be selected, that will immediately end the game.");
        System.out.print("\nWould you like to view an example round of the game? Input Y for Yes and N for No: ");
        Scanner sc = new Scanner(System.in);
        if (sc.next().toUpperCase().charAt(0) == 'Y') {
            viewExample();
        }
        System.out.println("That's all! Good luck!\n");

    }

    private static void viewExample() {
        System.out.println("Basic Playing Example: ");
        System.out.println("6 9 10 3\n2 9 4 5\n1 4 8 7\n5 5 3 4");
        System.out.println("Let's say the main number of this round is 4.");
        System.out.println("To proceed to the next round, you must add up numbers that is divisible by 4, for example" +
                " 4, 8, 12, 16, and so on.");
        System.out.println("Let's say I choose the number at position 6, which is the number 9, so my current sum is 9. For my next number, \n" +
                "I'll choose the number at position 4, which is number 3. Now my current sum is 12. 12 is divisible by 4 so" +
                " this round is done. ");
        System.out.println("For that round, I would only get 1 point, because I only chose one number that was on the outer rim of the grid:" +
                "\n9 was in the center, 3 was on the outside. Remember you only get points from the numbers on the outer rim.");
        System.out.println("Now after this round, the grid will look like this: ");
        System.out.println("6 9 10 0\n2 9 4 5\n1 4 8 7\n5 5 3 4");
        System.out.println("Notice how the 3 has turned into a zero, but the 9 did not. We used both of the numbers, so why didn't" +
                " they both disappear?\nRemember that center numbers do not disappear when used, only outer rim numbers do. ");



    }






}
