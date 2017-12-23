package com.example.teja.connect4ai;

import android.widget.Toast;

import java.util.Random;

/**
 * Created by teja on 11/27/2017.
 */

public class Board {
    public int boardData[][];
    public int row;
    public int col;
    public int connect_t;
    public int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
    public int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};
    public static int CPUTURN = 0;
    public int board_top[];
    public int tpositions;
    public MainActivity mainActivity;
    private boolean alpha_beta;
    private int tDepth;
    //Initialize Board constructor
    public Board(int row, int col, int connect, MainActivity main,boolean alpha_beta)
    {
        this.row = row;
        this.col = col;
        this.connect_t = connect;
        this.boardData = new int[row][col];
        this.alpha_beta = alpha_beta;
        for (int i= 0;i<row;i++){
            for (int j= 0;j<col;j++){
                this.boardData[i][j] = 0;
            }
        }
        this.tpositions = row * col;
        this.board_top = new int[col];
        this.mainActivity = main;
        for(int i =0; i < col; i++) {
            board_top[i] = row - 1;
        }
        if(alpha_beta) {
            tDepth = 4;
        } else {
            tDepth = 4;
        }
    }
    //Set the player inside the board for calculating the best position
    public int AddCoin(int[][] board, int col, int ply)
    {
        try {
            if (board_top[col] >= 0) {
                board[board_top[col]][col] = ply;
                board_top[col]--;
                return 1;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return 0;
    }
    //To remove the data from board after calculating the board value
    public int SubCoin(int[][] board, int col)
    {
        try {
            if (board_top[col] >= -1 && board_top[col] < this.row) {
                board_top[col]++;
                board[board_top[col]][col] = 0;
                return 1;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return 0;
    }
    //Function to place the coin in the board
    public void PlaceCoin(int col, int ply)
    {
        int win = -1;
        try {
            if (board_top[col] >= 0) {
                this.boardData[board_top[col]][col] = ply;
                tpositions--;
                mainActivity.setImageView(board_top[col] + 1, col + 1, ply);
                mainActivity.DisplayMessage(CheckWinBoard(boardData));
                this.board_top[col]--;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    //Check for number of consecutive tiles in the vertical direction
    public int ConnectV(int[][] board, int tcol, int ply)
    {
        int max =0;
        int arr[] = new int[row];
        for(int i =0; i < row; i++) {
            arr[i] = 0;
        }
        for(int i = row -1; i >=0; i--){
            if(board[i][tcol] == ply) {
                if(i == row -1) {
                    arr[i] = 1;
                } else {
                    arr[i] = arr[i + 1] + 1;
                }
            }
        }
        for(int i =0; i < row; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }
    //Check for number of consecutive tiles in the horizontal direction
    public int ConnectR(int[][] board, int trow, int ply)
    {
        int max =0;
        int arr[] = new int[col];
        for(int i =0; i < col; i++) {
            arr[i] = 0;
        }
        for(int i = col -1; i >=0; i--){
            if(board[trow][i] == ply) {
                if(i == col -1) {
                    arr[i] = 1;
                } else {
                    arr[i] = arr[i + 1] + 1;
                }
            }
        }
        for(int i =0; i < col; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public int ConnectRD(int sz, int r1, int c1, int r2, int c2, int[][] board, int ply)
    {
        int arr[] = new int[sz];
        int r = r1;
        int c = c1;
        int cnt = 0;
        int max = 0;
        for(int i = 0; i < sz; i++) {
            arr[i] = 0;
        }
        while (r<=r2 && c <=c2 && cnt < sz) {
            if(board[r][c] == ply) {
                if (r == r1 && c == c1) {
                    arr[cnt] = 1;
                } else {
                    arr[cnt] = arr[cnt -1] +1;
                }
            }
            r++;
            c++;
            cnt++;
        }

        for(int i =0; i < sz; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public int ConnectLD(int sz, int r1, int c1, int r2, int c2, int[][] board, int ply)
    {
        int arr[] = new int[sz];
        int r = r1;
        int c = c1;
        int cnt = 0;
        int max = 0;
        for(int i = 0; i < sz; i++) {
            arr[i] = 0;
        }
        while (r <= r2 && c >=c2 && cnt < sz) {
            if(board[r][c] == ply) {
                if (r == r1 && c == c1) {
                    arr[cnt] = 1;
                } else {
                    arr[cnt] = arr[cnt -1] +1;
                }
            }
            r++;
            c--;
            cnt++;
        }

        for(int i =0; i < sz; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }
    //Assign score for number of consecutive tiles
    public int AssignScore(int val) {
        if(val >= 4) {
            return 80;
        } else if (val == 3) {
            return 60;
        } else if(val == 2) {
            return 20;
        }
        return 10;
    }
    //Evaluation function used to calculate the value
    public int CalcBoardValue(int[][] board ,int ply, int oPly)
    {
        int score = 0;
        int C4 = 0;
        // vertical
        for(int i = 0; i < col; i++) {
            //caluclate number of consecutive tiles for the current player
            score += AssignScore(ConnectV(board,i,ply));
            if(C4 == 0) {
                if(ConnectV(board,i,oPly) == 4) {
                    score -= AssignScore(4);
                    C4 = 1;
                }
            }
        }
        // horizontal
        for(int i =0; i < row; i++) {
            //caluclate no 4s, 3s, 2s, 1s for the current player
            score += AssignScore(ConnectR(board,i,ply));
            if(C4 == 0) {
                if(ConnectR(board,i,oPly) == 4) {
                    score -= AssignScore(4);
                    C4 = 1;
                }
            }
        }
        // diagonal left
        score += AssignScore(ConnectRD(6,0,0,5,5, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(6,0,0,5,5, board,oPly));
        }
        score += AssignScore(ConnectRD(6,0,1,5,6, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(6,0,1,5,6, board,oPly));
        }
        score += AssignScore(ConnectRD(5,0,2,4,6, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(5,0,2,4,6, board,oPly));
        }
        score += AssignScore(ConnectRD(4,0,3,3,6, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(4,0,3,3,6, board,oPly));
        }
        score += AssignScore(ConnectRD(5,1,0,5,4, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(5,1,0,5,4, board,oPly));
        }
        score += AssignScore(ConnectRD(4,2,0,5,3, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectRD(4,2,0,5,3, board,oPly));
        }

        // diagonal right
        score += AssignScore(ConnectLD(6,0,6,5,1, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(6,0,6,5,1, board,oPly));
        }
        score += AssignScore(ConnectLD(6,0,5,5,0, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(6,0,5,5,0, board,oPly));
        }
        score += AssignScore(ConnectLD(5,0,4,4,0, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(5,0,4,4,0, board,oPly));
        }
        score += AssignScore(ConnectLD(4,0,3,3,0, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(4,0,3,3,0, board,oPly));
        }
        score += AssignScore(ConnectLD(5,1,6,5,2, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(5,1,6,5,2, board,oPly));
        }
        score += AssignScore(ConnectLD(4,2,6,5,3, board,ply));
        if(C4 == 0) {
            score -= AssignScore(ConnectLD(4,2,6,5,3, board,oPly));
        }
        return score;
    }
    //Used to return the win in order to show the toast about who won the game
    public int CheckWinBoard(int[][] board) {
        int winner = 0;
        for(int i=0; i < row; i++) {
            for(int j =0; j < col; j++) {
                if(CheckWin(board,i,j,MainActivity.Player1) == MainActivity.Player1) {
                    return MainActivity.Player1;
                }
                if(CheckWin(board,i,j,MainActivity.Player2) == MainActivity.Player2) {
                    return MainActivity.Player2;
                }
            }
        }

        for(int i =0; i < row; i++) {
            for(int j =0; j < col; j++) {
                if(board[i][j] == 0) {
                    return -1;
                }
            }
        }
        return winner;
    }

    int max(int a, int b) {
        if(a > b) {
            return a;
        } else return b;
    }

    int min(int a, int b) {
        if(a < b) {
            return a;
        } else return b;
    }

    // Function to implement Minimax Algorithm . It considers all
    // the possible ways the game can go and returns
    // the value of the board
    int MinMax(int[][]state, int depth ,boolean IsMax)
    {
        int win = CheckWinBoard(state);
        if(win == MainActivity.Player1) {
            return 1000;
        }
        if(win == MainActivity.Player2) {
            return -1000;
        }
        if(win == 0) {
            return 0;
        }
        if(depth == tDepth) {
            if(IsMax) {
                return  CalcBoardValue(state, MainActivity.Player1, MainActivity.Player2 );
            } else {
                return CalcBoardValue(state, MainActivity.Player1, MainActivity.Player2 );
            }
        }
        // Check if it is max move
        //Maximizer is CPU
        if(IsMax) {
            int best = -50000;
            for(int i = 0; i < col; i++) {
                if(AddCoin(state, i, MainActivity.Player1) == 1) {
                    best = max(best, MinMax(state, depth + 1, !IsMax));
                    SubCoin(state, i);
                }
            }
            return best;
        } else {
            int best = 50000;
            for(int i = 0; i < col; i++) {
                if(AddCoin(state,i,MainActivity.Player2) == 1) {
                    best = min(best, MinMax(state, depth + 1, !IsMax));
                    SubCoin(state, i);
                }
            }
            return best;
        }
    }
    // Function to implement Minimax Alpha Beta Algorithm . It considers all
    // the possible ways the game can go upto depth 5 and returns
    // the value of the board
    int MinMaxAlphaBeta(int[][] state, int depth, boolean IsMax, int alpha, int beta)
    {
        int win = CheckWinBoard(state);
        if(win == MainActivity.Player1) {
            return 1000;
        }
        if(win == MainActivity.Player2) {
            return -1000;
        }
        if(win == 0) {
            return 0;
        }
        if(depth == tDepth) {
            if(IsMax) {
                return  CalcBoardValue(state, MainActivity.Player1, MainActivity.Player2 );
            } else {
                return CalcBoardValue(state, MainActivity.Player1, MainActivity.Player2 );
            }
        }
        // Check if it is max move
        if(IsMax) {
            int best = -50000;
            for(int i = 0; i < col; i++) {
                if(AddCoin(state, i, MainActivity.Player1) == 1) {
                    best = max(best, MinMax(state, depth + 1, !IsMax));
                    alpha = max (alpha, best);
                    SubCoin(state, i);
                    if (beta <= alpha);
                      break;
                }
            }
            return best;
        } else {
            int best = 50000;
            for(int i = 0; i < col; i++) {
                if(AddCoin(state,i,MainActivity.Player2) == 1) {
                    best = min(best, MinMax(state, depth + 1, !IsMax));
                    beta = min (best,beta);
                    SubCoin(state, i);
                    if(beta <= alpha) {
                        break;
                    }
                }
            }
            return best;
        }
    }
    //Returns the best position and place the coin at that particular position
    void CPUPlayer()
    {
        if(CPUTURN == 0) {
            Random rnd = new Random();
            int randomP = rnd.nextInt(5) + 1;
            PlaceCoin(randomP,MainActivity.Player1);
            CPUTURN = 1;
            return;
        }
        int bestMove = - 50000;
        int bestPosition = -1;
        for(int i=0; i <col; i++) {
            if(AddCoin(boardData,i,MainActivity.Player1) == 1) {
                int mov_value;
                if(alpha_beta) {
                    mov_value = MinMaxAlphaBeta(boardData, 0, false, -50000, 50000);
                } else {
                    mov_value = MinMax(boardData, 0, false);
                }
                SubCoin(boardData, i);
                if (mov_value > bestMove) {
                    bestMove = mov_value;
                    bestPosition = i;
                }
            }
        }
        PlaceCoin(bestPosition,MainActivity.Player1);
    }
    //Check the Win condition across the board
    public int CheckWin(int[][] board, int x, int y, int ply) {
        for (int i = 0; i < dx.length; i++) {
            int r = x;
            int c = y;
            int count = 0;
            while (r >= 0 && r < row && c >= 0 && c < col && count != connect_t) {
                if (board[r][c] == ply) {
                    count++;
                    r += dx[i];
                    c += dy[i];
                } else {
                    break;
                }
            }
            if (count == connect_t) {
                return ply;
            }
        }
        return 0;
    }
}
