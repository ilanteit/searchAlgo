public class Board {
    //private String[][] board;
    private String[][] board;
    private int N;

    public Board(String[][] board, int N) {
        this.board = board;
        this.N = N;
    }
    public int getN(){
        return this.N;

    }




    public boolean isValid(int row, int col) {
        return (row >= 0) && (row < N) && (col >= 0) && (col < N) && (!board[row][col].equals("X"));
    }

    //returns me the cost of a movment from block A to block B
    public int getCost(int current_row, int current_col, int newRow, int newCol) {
        String currBlock = board[current_row][current_col];
        String newBlock = board[newRow][newCol];


        int cost = 0;
        if (newBlock.equals("H")) {
            cost = 5;
            if ((newRow - current_row) != 0 && (newCol - current_col) != 0) {
                // System.out.println("i am inside");
                cost = 10;
            }
        } else if (newBlock.equals("R")) {
            cost = 3;
        } else if (newBlock.equals("D")) {
            cost = 1;
        } else if (newBlock.equals("G")) {
            cost = 5;
        }
        return cost;
    }
    public boolean isGoal(int row, int col) {
        return (board[row][col].equals("G"));
    }
    //return the letter in that block
    public String getLetter(int current_row, int current_col){
        return board[current_row][current_col];
    }
    //return the cost of a block without logic
    public int getCostSimple(int current_row, int current_col) {
        //String currBlock = board[current_row][current_col];
        String newBlock = board[current_row][current_col];


        int cost = 0;
        if (newBlock.equals("S")) {
            cost = 5;

        } else if (newBlock.equals("R")) {
            cost = 3;
        } else if (newBlock.equals("D")) {
            cost = 1;
        } else if (newBlock.equals("G")) {
            //System.out.println("i am insde");
            cost = 5;
        }
        else if (newBlock.equals("X")){
            cost=-1;
        }
        return cost;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(board[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
