package sprint2.sprint2;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Board{
    //ENUMS
    public enum Cell {EMPTY, S, O};
    public enum GameState {PLAYING, DRAW, R_WON, B_WON};
    public enum Color{BLACK, RED, BLUE, PURPLE, EMPTY};

    //ARRAYS
    public Cell[][] logicBoard;
    public Color[][] colorGrid;
    public int[] row_col;

    //ATTRIBUTES
    String outputFile;
    FileWriter write;
    BufferedWriter buffer;
    public char currMove;
    private GameState currGameState;
    boolean generalUpdate = false;
    private char gameMode;
    public int boardSize;
    private char redPlayer;
    private char bluePlayer;
    private char playerTurn;
    private int redSOS = 0;
    private int blueSOS = 0;

    //CONSTRUCTOR
    public Board(int boardSize) throws IOException {
        this.boardSize = boardSize;
        logicBoard = new Cell[boardSize][boardSize];
        colorGrid = new Color[boardSize][boardSize];

        //DEFINE OUTPUT FILE PATH
        outputFile ="./sosOutputFile";

        //DECLARE FileWriter OBJECT
        write = new FileWriter(outputFile);
        buffer = new BufferedWriter(write);

        initBoard();

    }

    //INITIALIZE BOARD FUNCTION
    public void initBoard(){
        for (int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                logicBoard[row][col] = Cell.EMPTY;
            }
        }

        for (int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                colorGrid[row][col] = Color.EMPTY;
            }
        }

        currGameState = GameState.PLAYING;
        playerTurn = 'R';


    }

    //SETTERS
    public void setGameMode(char gM){
        gameMode = gM;
    }
    public void setPlayerTurn(char pT) {playerTurn = pT; } //used for test class only
    public void setRedPlayerType(char redPlayerType){redPlayer = redPlayerType;}
    public void setBluePlayerType(char bluePlayerType){bluePlayer = bluePlayerType;}

    //GETTERS
    public char getGameMode(){return gameMode;};
    public char getPlayerTurn(){return playerTurn;};
    public GameState getCurrGameState(){return currGameState;};

    public Cell getCell(int row, int col){
        if (row >= 0 && row < boardSize
                && col >= 0 && col < boardSize)
            return logicBoard[row][col];
        else
            return null;
    }


    //MAIN METHODS
    public void makeMove(int row, int col, boolean sMove, boolean oMove){

        //CLICK == HUMAN MOVE
        move(row, col, sMove, oMove);

        //UPDATE GENERAL GAME
        if(gameMode == 'G'){
            generalGameUpdate(row, col);
        }

        //UPDATE GAME STATE
        currGameState = updateGameState(row, col);
        writeMoveToOutput(row, col);

        //CHANGE TURNS
        changeTurns();

        //IF NEXT PLAYER IS A COMPUTER, THEY MAKE THEIR MOVE
        //red computer player
        while (currGameState == GameState.PLAYING && playerTurn == 'R' && redPlayer == 'C'){
            computerMove();

            //REASSIGN ROW AND COLUMN FOR SECOND UPDATE
            row = row_col[0];
            col = row_col[1];

            //UPDATE GENERAL GAME
            if(gameMode == 'G'){
                generalGameUpdate(row, col);

            }

            //UPDATE GAME STATE
            currGameState = updateGameState(row, col);
            writeMoveToOutput(row, col);

            //CHANGE TURNS
            changeTurns();
        }

        //blue computer player
        while (currGameState == GameState.PLAYING && playerTurn == 'B' && bluePlayer == 'C'){
            computerMove();

            //REASSIGN ROW AND COLUMN FOR SECOND UPDATE
            row = row_col[0];
            col = row_col[1];

            //UPDATE GENERAL GAME
            if(gameMode == 'G'){
                generalGameUpdate(row, col);
            }

            //UPDATE GAME STATE
            currGameState = updateGameState(row, col);
            writeMoveToOutput(row, col);

            //CHANGE TURNS
            changeTurns();

        }


    }

    private  void move(int row, int col, boolean sMove, boolean oMove){
        //check to make sure it's empty
        if(row >= 0 && row < boardSize
                && col >= 0 && col < boardSize
                && logicBoard[row][col] == Cell.EMPTY) {

            //make the move
            if (sMove && !oMove) {
                logicBoard[row][col] = Cell.S;
                colorGrid[row][col] = Color.BLACK;
                currMove = 'S';
            }
            if (!sMove && oMove) {
                logicBoard[row][col] = Cell.O;
                colorGrid[row][col] = Color.BLACK;
                currMove = 'O';
            }

        }

    }

    public void computerMove(){
        int row;
        int col;
        boolean sMove = false;
        boolean oMove = false;



        //GET PLACEMENT AND MOVE TYPE
        generateComputerPlacement(boardSize);
        generateMoveType();

        //SET ROW COLUMN VALUES
        row = row_col[0];
        col = row_col[1];

        //SET BOOLEAN VALUES
        if(currMove == 'S'){
            sMove = true;
            oMove = false;
        }
        if(currMove == 'O'){
            sMove = false;
            oMove = true;
        }

        //CALL GENERAL MOVE FUNCTION
        move(row, col, sMove, oMove);


    }

    void bothComputers(){

        int row;
        int col;
        computerMove();

        //REASSIGN ROW AND COLUMN FOR SECOND UPDATE
        row = row_col[0];
        col = row_col[1];

        //UPDATE GENERAL GAME
        if(gameMode == 'G'){
            generalGameUpdate(row, col);
            writeMoveToOutput(row, col);
        }

        //CHANGE TURNS
        changeTurns();

        //UPDATE GAME STATE
        currGameState = updateGameState(row, col);
        writeMoveToOutput(row, col);

    }

    void changeTurns(){

        if(gameMode == 'S'){
            playerTurn = (playerTurn == 'R') ? 'B' : 'R';
        }

        if(gameMode == 'G'){
            if(generalUpdate && playerTurn == 'R'){
                playerTurn = (playerTurn == 'R') ? 'R' : 'B';
            }

            if(generalUpdate && playerTurn == 'B'){
                playerTurn = (playerTurn == 'B') ? 'B' : 'R';
            }

            if(!generalUpdate){
                playerTurn = (playerTurn == 'R') ? 'B' : 'R';
            }

        }
    }

    private boolean simpleGameWin( int row, int col){

        if (columnCheck(row, col)){
            System.out.println("simple game win by column");
            return true;
        };
        if (rowCheck(row, col)){
            System.out.println("simple game win by row");
            return true;
        };
        if(diagonalCheck(row, col)){
            System.out.println("simple game win by diagonal");
            return true;
        };

        System.out.println("no win logged");
        return false;

    }

    private boolean simpleGameDraw(){
        for(int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                if(logicBoard[row][col] == Cell.EMPTY){
                    return false;
                }
            }
        }

        return true;

    }

    private void  generalGameUpdate(int row, int col) {
        int oldRedSOS = redSOS;
        int oldBlueSOS = blueSOS;

        rowCheck(row, col);
        columnCheck(row, col);
        diagonalCheck(row, col);

        if(oldRedSOS < redSOS || oldBlueSOS < blueSOS){
            generalUpdate = true;
        }

        else{
            generalUpdate = false;
        }


    }
    private boolean generalGameWin(int row, int col){

        if(simpleGameDraw()){
            return (redSOS != blueSOS) ? true: false;

        }

        return  false;
    }

    private boolean generalGameDraw(){

        if(simpleGameDraw()){
            return (redSOS == blueSOS) ? true : false;

        }

        return false;

    }

    private boolean columnCheck(int row, int col){
        Cell token = (currMove == 'S') ? Cell.S : Cell.O;

        //3 in a column
        int rowAbove = row - 1;
        rowAbove = (rowAbove < 0) ?  0 : rowAbove;
        int row2Above = row - 2;
        row2Above = (row2Above < 0) ?  0 : row2Above;

        int rowBelow = row + 1;
        rowBelow = (rowBelow > boardSize -1 ) ? boardSize -1: rowBelow;
        int row2Below = row + 2;
        row2Below = (row2Below > boardSize -1 ) ? boardSize -1: row2Below;

        //O placed in middle
        if(token == Cell.O){
            if(logicBoard[rowAbove][col] != token &&
                    logicBoard[rowAbove][col] != Cell.EMPTY &&
                    logicBoard[rowBelow][col] != token &&
                    logicBoard[rowBelow][col] != Cell.EMPTY){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[rowAbove][col] = (colorGrid[rowAbove][col] == Color.BLUE
                            || colorGrid[rowAbove][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE ) ? Color.PURPLE : Color.RED;

                    colorGrid[rowBelow][col] = (colorGrid[rowBelow][col] == Color.BLUE
                            || colorGrid[rowBelow][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[rowAbove][col] = (colorGrid[rowAbove][col] == Color.RED
                            || colorGrid[rowAbove][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[row][col] =  (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[rowBelow][col] =  (colorGrid[rowBelow][col] == Color.RED
                            || colorGrid[rowBelow][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                System.out.println("placed in middle of column");
                return true;

            }

        }

        //S placement
        if(token == Cell.S){

            // placed on top
            if(logicBoard[row2Below][col] == token &&
                    logicBoard[rowBelow][col] != token &&
                    logicBoard[rowBelow][col] != Cell.EMPTY ){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[rowBelow][col] = (colorGrid[rowBelow][col] == Color.BLUE
                            || colorGrid[rowBelow][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[row2Below][col] = (colorGrid[row2Below][col] == Color.BLUE
                            || colorGrid[row2Below][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE)  ? Color.PURPLE : Color.BLUE;

                    colorGrid[rowBelow][col] = (colorGrid[rowBelow][col] == Color.RED
                            || colorGrid[rowBelow][col] == Color.PURPLE)  ? Color.PURPLE : Color.BLUE;

                    colorGrid[row2Below][col] = (colorGrid[row2Below][col] == Color.RED
                            || colorGrid[row2Below][col] == Color.PURPLE)  ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                System.out.println("placed on top of column");
                return true;
            }

            //placed on bottom
            if(logicBoard[row2Above][col] == token &&
                    logicBoard[rowAbove][col] != token &&
                    logicBoard[rowAbove][col] != Cell.EMPTY ){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row2Above][col] = (colorGrid[row2Above][col] == Color.BLUE
                            || colorGrid[row2Above][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[rowAbove][col] = (colorGrid[rowAbove][col] == Color.BLUE
                            || colorGrid[rowAbove][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[row2Above][col] = (colorGrid[row2Above][col] == Color.RED
                            || colorGrid[row2Above][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[rowAbove][col] = (colorGrid[rowAbove][col] == Color.RED
                            || colorGrid[rowAbove][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                System.out.println("placed on bottom of column");
                return true;
            }

        }

        return false;

    }

    private boolean rowCheck(int row, int col){
        Cell token = (currMove == 'S') ? Cell.S : Cell.O;

        //3 in a row
        int colLeft = col- 1;
        colLeft = (colLeft < 0) ?  0 : colLeft;
        int col2Left = col -2;
        col2Left = (col2Left < 0) ? 0 : col2Left;

        int colRight = col + 1;
        colRight = (colRight > boardSize -1 ) ? boardSize -1: colRight;
        int col2Right = col + 2;
        col2Right = (col2Right > boardSize -1) ? boardSize -1: col2Right;

        //O placed in middle
        if(token == Cell.O){
            if(logicBoard[row][colLeft] != token &&
                    logicBoard[row][colLeft] != Cell.EMPTY &&
                    logicBoard[row][colRight] != token &&
                    logicBoard[row][colRight] != Cell.EMPTY){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][colLeft] = (colorGrid[row][colLeft] == Color.BLUE
                            || colorGrid[row][colLeft] == Color.PURPLE) ? Color.PURPLE: Color.RED;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.RED;

                    colorGrid[row][colRight] = (colorGrid[row][colRight] == Color.BLUE
                            || colorGrid[row][colRight] == Color.PURPLE) ? Color.PURPLE: Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[row][colLeft] = (colorGrid[row][colLeft] == Color.RED
                            || colorGrid[row][colLeft] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][colRight] = (colorGrid[row][colRight] == Color.RED
                            || colorGrid[row][colRight] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;

                return true;

            }
        }

        //S placement
        if(token == Cell.S){
            //placed on right edge
            if(logicBoard[row][col2Left] == token &&
                    logicBoard[row][colLeft] !=token &&
                    logicBoard[row][colLeft] != Cell.EMPTY ){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col2Left] = (colorGrid[row][col2Left] == Color.BLUE
                            || colorGrid[row][col2Left] == Color.PURPLE ) ? Color.PURPLE: Color.RED;

                    colorGrid[row][colLeft] = (colorGrid[row][colLeft] == Color.BLUE
                            || colorGrid[row][colLeft] == Color.PURPLE) ? Color.PURPLE: Color.RED;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[row][col2Left] = (colorGrid[row][col2Left] == Color.RED
                            || colorGrid[row][col2Left] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][colLeft] = (colorGrid[row][colLeft] == Color.RED
                            || colorGrid[row][colLeft] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;
                }


                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;

                System.out.println("placed on right edge of row");
                return true;
            }

            //placed on left edge
            if(logicBoard[row][col2Right] == token &&
                    logicBoard[row][colRight] != token &&
                    logicBoard[row][colRight] != Cell.EMPTY ){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col2Right] = (colorGrid[row][col2Right] == Color.BLUE
                            || colorGrid[row][col2Right] == Color.PURPLE) ? Color.PURPLE: Color.RED;

                    colorGrid[row][colRight] = (colorGrid[row][colRight] == Color.BLUE
                            || colorGrid[row][colRight] == Color.PURPLE) ? Color.PURPLE: Color.RED;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.RED;
                }

                if(playerTurn == 'B') {
                    colorGrid[row][col2Right] = (colorGrid[row][col2Right] == Color.RED
                            || colorGrid[row][col2Right] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][colRight] = (colorGrid[row][colRight] == Color.RED
                            || colorGrid[row][colRight] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;

                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE: Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                System.out.println("placed on left edge of row");
                return true;
            }


        }

        return false;

    }

    private boolean diagonalCheck(int row, int col){
        Cell token = (currMove == 'S') ? Cell.S : Cell.O;

        //TOP RIGHT ROW/COLUMN
        int topRightRow = row-1;
        topRightRow = (topRightRow < 0) ?  0 : topRightRow;
        int topRightRow2 = row - 2;
        topRightRow2 = (topRightRow2 < 0) ? 0 : topRightRow2;

        int topRightCol = col + 1;
        topRightCol = (topRightCol > boardSize -1 ) ? boardSize -1: topRightCol;
        int topRightCol2 = col + 2;
        topRightCol2 = (topRightCol2 > boardSize -1) ? boardSize -1: topRightCol2;

       //BOTTOM RIGHT ROW/COLUMN
        int bottomRightRow = row+1;
        bottomRightRow = (bottomRightRow > boardSize -1 ) ?  boardSize -1  : bottomRightRow;
        int bottomRightRow2 = row + 2;
        bottomRightRow2 = (bottomRightRow2 > boardSize -1 ) ? boardSize -1  : bottomRightRow2;

        int bottomRightCol = col + 1;
        bottomRightCol = (bottomRightCol > boardSize -1 ) ? boardSize -1: bottomRightCol;
        int bottomRightCol2 = col + 2;
        bottomRightCol2 = (bottomRightCol2 > boardSize -1) ? boardSize -1: bottomRightCol2;

       //TOP LEFT ROW/COLUMN
        int topLeftRow = row-1;
        topLeftRow = (topLeftRow < 0) ?  0 : topLeftRow;
        int topLeftRow2 = row - 2;
        topLeftRow2 = (topLeftRow2 < 0) ? 0 : topLeftRow2;

        int topLeftCol = col - 1;
        topLeftCol = (topLeftCol < 0 ) ? 0: topLeftCol;
        int topLeftCol2 = col - 2;
        topLeftCol2 = (topLeftCol2 < 0) ? 0 : topLeftCol2;


        //BOTTOM LEFT ROW/COLUMN
        int bottomLeftRow = row+1;
        bottomLeftRow = (bottomLeftRow > boardSize - 1) ?  boardSize - 1 : bottomLeftRow;
        int bottomLeftRow2 = row + 2;
        bottomLeftRow2 = (bottomLeftRow2 > boardSize - 1) ? boardSize - 1 : bottomLeftRow2;

        int bottomLeftCol = col - 1;
        bottomLeftCol = (bottomLeftCol < 0) ? 0: bottomLeftCol;
        int bottomLeftCol2 = col - 2;
        bottomLeftCol2 = (bottomLeftCol2 < 0) ? 0: bottomLeftCol2;


        if(token == Cell.O){
            //O PLACED IN MIDDLE
            //check for both moving to the right and the left
            if(logicBoard[topRightRow][topRightCol] != token &&
                    logicBoard[topRightRow][topRightCol] != Cell.EMPTY &&
                    logicBoard[bottomLeftRow][bottomLeftCol] != token &&
                    logicBoard[bottomLeftRow][bottomLeftCol] != Cell.EMPTY &&
                    logicBoard[topLeftRow][topLeftCol] != token &&
                    logicBoard[topLeftRow][topLeftCol] != Cell.EMPTY &&
                    logicBoard[bottomRightRow][bottomRightCol] != token &&
                    logicBoard[bottomRightRow][bottomRightCol] != Cell.EMPTY){

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.BLUE
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.BLUE
                            || colorGrid[bottomLeftRow][bottomLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.BLUE
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.BLUE
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;


                }

                if(playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.RED
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.RED
                            || colorGrid[bottomLeftRow][bottomLeftCol] == Color.PURPLE)  ? Color.PURPLE : Color.BLUE;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.RED
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.RED
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }


                System.out.println("completes both diagonals");

                //add to SOS count for general game
                //irrelevant for simple game
                redSOS = (playerTurn == 'R') ? redSOS + 2 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 2: blueSOS;
                return true;

            }


            // moving up to the right
            if(logicBoard[topRightRow][topRightCol] != token &&
                    logicBoard[topRightRow][topRightCol] != Cell.EMPTY &&
                    logicBoard[bottomLeftRow][bottomLeftCol] != token &&
                    logicBoard[bottomLeftRow][bottomLeftCol] != Cell.EMPTY){

                if(bottomLeftRow == row || bottomLeftCol == col || topRightRow == row || topRightCol == col){
                    return false;
                }

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.BLUE
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.BLUE
                            || colorGrid[bottomLeftRow][bottomLeftCol] == Color.PURPLE)  ? Color.PURPLE : Color.RED;

                }

                if(playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.RED
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.RED
                            || colorGrid[bottomLeftRow][bottomLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }


                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("moving up to the right");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                return true;

            }

            // moving up to the left
            if(logicBoard[topLeftRow][topLeftCol] != token &&
                    logicBoard[topLeftRow][topLeftCol] != Cell.EMPTY &&
                    logicBoard[bottomRightRow][bottomRightCol] != token &&
                    logicBoard[bottomRightRow][bottomRightCol] != Cell.EMPTY){

                if(topLeftRow == row || topLeftCol == col || bottomRightRow == row || bottomRightCol == col){
                    return false;
                }

                //update color grid
                if(playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.BLUE
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.BLUE
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                }

                if(playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.RED
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.RED
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }


                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("moving up to the left");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1: blueSOS;
                return true;

            }

        }

        //S PLACEMENT
        if(token == Cell.S) {
            //placed top right
            if (logicBoard[bottomLeftRow2][bottomLeftCol2] == Cell.S &&
                    logicBoard[bottomLeftRow][bottomLeftCol] == Cell.O) {


                if (row == bottomLeftRow2 || col == bottomLeftCol2
                        || bottomLeftRow == bottomLeftRow2 || bottomLeftCol == bottomLeftCol2 ) {
                    return false;
                }

                //update color grid
                if (playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.BLUE
                            || colorGrid[bottomRightRow][bottomLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomLeftRow2][bottomLeftCol2] = (colorGrid[bottomLeftRow2][bottomLeftCol2] == Color.BLUE
                            || colorGrid[bottomLeftRow2][bottomLeftCol2] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                }

                if (playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomLeftRow][bottomLeftCol] = (colorGrid[bottomLeftRow][bottomLeftCol] == Color.RED
                            || colorGrid[bottomLeftRow][bottomLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomLeftRow2][bottomLeftCol2] = (colorGrid[bottomLeftRow2][bottomLeftCol2] == Color.RED
                            || colorGrid[bottomLeftRow2][bottomLeftCol2] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("placed top right");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1 : blueSOS;
                return true;

            }

            //placed top left
            if (logicBoard[bottomRightRow2][bottomRightCol2] == Cell.S &&
                    logicBoard[bottomRightRow][bottomRightCol] == Cell.O) {

                if (bottomRightRow2 == row || bottomRightCol2 == col
                    || bottomRightRow == bottomRightRow2 || bottomRightCol == bottomRightCol2 ) {
                    return false;
                }

                //update color grid
                if (playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.BLUE
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[bottomRightRow2][bottomRightCol2] = (colorGrid[bottomRightRow2][bottomRightCol2] == Color.BLUE
                            || colorGrid[bottomRightRow2][bottomRightCol2] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                }

                if (playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomRightRow][bottomRightCol] = (colorGrid[bottomRightRow][bottomRightCol] == Color.RED
                            || colorGrid[bottomRightRow][bottomRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[bottomRightRow2][bottomRightCol2] = (colorGrid[bottomRightRow2][bottomRightCol2] == Color.RED
                            || colorGrid[bottomRightRow2][bottomRightCol2] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("placed top left");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1 : blueSOS;
                return true;
            }

            //placed bottom right
            if (logicBoard[topLeftRow2][topLeftCol2] == Cell.S &&
                    logicBoard[topLeftRow][topLeftCol] == Cell.O) {

                if (topLeftRow2 == row || topLeftCol2 == col ||
                        topLeftRow == topLeftRow2 || topLeftCol == topLeftCol2) {
                    return false;
                }

                //update color grid
                if (playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.BLUE
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topLeftRow2][topLeftCol2] = (colorGrid[topLeftRow2][topLeftCol2] == Color.BLUE
                            || colorGrid[topLeftRow2][topLeftCol2] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                }

                if (playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topLeftRow][topLeftCol] = (colorGrid[topLeftRow][topLeftCol] == Color.RED
                            || colorGrid[topLeftRow][topLeftCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topLeftRow2][topLeftCol2] = (colorGrid[topLeftRow2][topLeftCol2] == Color.RED
                            || colorGrid[topLeftRow2][topLeftCol2] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("placed bottom right");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1 : blueSOS;
                return true;

            }

            //placed bottom left
            if (logicBoard[topRightRow2][topRightCol2] == Cell.S &&
                    logicBoard[topRightRow][topRightCol] == Cell.O) {

                if (topRightRow2 == row || col == topRightCol2
                        || topRightRow == topRightRow2 || topRightCol == topRightCol2 ) {
                    return false;
                }

                //update color grid
                if (playerTurn == 'R') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.BLUE
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.BLUE
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                    colorGrid[topRightRow2][topRightCol2] = (colorGrid[topRightRow2][topRightCol2] == Color.BLUE
                            || colorGrid[topRightRow2][topRightCol2] == Color.PURPLE) ? Color.PURPLE : Color.RED;

                }

                if (playerTurn == 'B') {
                    colorGrid[row][col] = (colorGrid[row][col] == Color.RED
                            || colorGrid[row][col] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topRightRow][topRightCol] = (colorGrid[topRightRow][topRightCol] == Color.RED
                            || colorGrid[topRightRow][topRightCol] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;

                    colorGrid[topRightRow2][topRightCol2] = (colorGrid[topRightRow2][topRightCol2] == Color.RED
                            || colorGrid[topRightRow2][topRightCol2] == Color.PURPLE) ? Color.PURPLE : Color.BLUE;
                }

                //add to SOS count for general game
                //irrelevant for simple game
                System.out.println("placed bottom left");
                redSOS = (playerTurn == 'R') ? redSOS + 1 : redSOS;
                blueSOS = (playerTurn == 'B') ? blueSOS + 1 : blueSOS;
                return true;
            }
        }


       return false;


    }


    //GENERATE ROW,COL FOR COMPUTER PLAYER MOVE
    public void generateComputerPlacement(int boardSize){
        boolean run = true;
        row_col = new int[2];
        Random random = new Random();

        int row = random.nextInt(boardSize);
        int col = random.nextInt(boardSize);

        while(run) {

            row = random.nextInt(boardSize);
            col = random.nextInt(boardSize);

            if (logicBoard[row][col] == Cell.EMPTY) {
                row_col[0] = row;
                row_col[1] = col;

                run = false;
            }


        }

    }

    public void generateMoveType(){
        Random random = new Random();
        int moveType = random.nextInt(2);

        if(moveType == 0){
            currMove = 'S';
        }
        else{
            currMove = 'O';
        }

        System.out.println(currMove);
    }

    private GameState updateGameState( int row, int col){

        if(gameMode == 'S'){
            boolean simpleWin = simpleGameWin(row, col);
            //red player win
            if(simpleWin && getPlayerTurn() == 'R'){
                System.out.println("simple win check");
                return GameState.R_WON;

            }

            //Blue player win
            if(simpleWin && getPlayerTurn() == 'B'){
                return GameState.B_WON;

            }

            //draw
            if(simpleGameDraw()){
                return GameState.DRAW;

            }
        }

        //GENERAL GAME
        if(gameMode == 'G'){
            boolean generalWin = generalGameWin(row, col);
            if(generalWin){
                //red player win
                if(redSOS > blueSOS){
                    return GameState.R_WON;

                }
                //blue player win
                if(blueSOS > redSOS){
                    return GameState.B_WON;
                }
            }

            //draw
            if(generalGameDraw()){
                return GameState.DRAW;
            }
        }
        else{
            currGameState = GameState.PLAYING;
        }

        return currGameState;

    }

    public void writeInitializationToOutput(){
        //open output file
        try {
            buffer.write("game mode: " + gameMode);
            buffer.newLine();

            buffer.write("board size: " + boardSize);
            buffer.newLine();

            buffer.write("red player: " + redPlayer);
            buffer.newLine();

            buffer.write("blue player: " + bluePlayer);
            buffer.newLine();
            buffer.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeMoveToOutput(int row, int col){
        try {
            if(gameMode == 'S'){
                buffer.write("current turn: " + playerTurn);
                buffer.newLine();

                buffer.write(playerTurn + " placed " + getCell(row, col) +
                        " at location [ " + row + " , " + col + " ]");
                buffer.newLine();
                buffer.newLine();
            }

            if(gameMode == 'G'){
                buffer.write("current turn: " + playerTurn);
                buffer.newLine();

                buffer.write(playerTurn + " placed " + getCell(row, col) +
                        " at location [ " + row + " , " + col + " ]");
                buffer.newLine();

                buffer.write("RedSOS count: " + redSOS);
                buffer.newLine();
                buffer.write("BlueSOS count: " + blueSOS);
                buffer.newLine();

                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeOutput() throws IOException {
        if(currGameState == GameState.R_WON){
            buffer.write("red player win");
            buffer.newLine();

        }
        if(currGameState == GameState.B_WON){
            buffer.write("blue player win");
            buffer.newLine();

        }

        if(currGameState == GameState.DRAW){
            buffer.write("game ended in a draw");
            buffer.newLine();

        }

        buffer.close();
    }

}
