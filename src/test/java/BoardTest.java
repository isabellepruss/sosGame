import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sprint2.sprint2.Board;

import java.io.IOException;


class BoardTest {

    int row;
    int col;
    boolean sMove;
    boolean oMove;

    //ACCEPTANCE CRITERION 1.1
    @Test
    void initBoardTest() throws IOException{
        Board boardTest = new Board(3); //testing 3x3
        boardTest.initBoard();

        Assertions.assertEquals(boardTest.boardSize, 3);

    }

    //ACCEPTANCE CRITERION 2.2
    @Test
    void gameModeTest() throws IOException{
        Board boardTest = new Board(3); //testing 3x3
        boardTest.setGameMode('S');
        Assertions.assertEquals(boardTest.getGameMode(), 'S');

        boardTest.setGameMode('G');
        Assertions.assertEquals(boardTest.getGameMode(), 'G');


    }

    @Test
    void makeMoveTestSimple() throws IOException{
        Board boardTest = new Board(3);

        //ACCEPTANCE CRITERION 4.1
        boardTest.setGameMode('S');
        //S move
        boardTest.makeMove(1, 1, true, false);
        Assertions.assertEquals(boardTest.getCell(1,1), Board.Cell.S);
        //O move
        boardTest.makeMove(2, 2, false, true);
        Assertions.assertEquals(boardTest.getCell(2,2), Board.Cell.O);

        //ACCEPTANCE CRITERION 4.2
        //tries to place S in cell already occupied by O
        boardTest.makeMove(2,2,true, false);
        Assertions.assertNotEquals(Board.Cell.S, Board.Cell.O);



    }

    @Test
    void makeMoveTestGeneral() throws IOException{
        Board boardTest = new Board(3);

        //ACCEPTANCE CRITERION 6.1
        boardTest.setGameMode('G');
        //S move
        boardTest.makeMove(1, 1, true, false);
        Assertions.assertEquals(boardTest.getCell(1,1), Board.Cell.S);
        //O move
        boardTest.makeMove(2, 2, false, true);
        Assertions.assertEquals(boardTest.getCell(2,2), Board.Cell.O);

        //ACCEPTANCE CRITERION 6.2
        //tries to place S in cell already occupied by O
        boardTest.makeMove(2,2,true, false);
        Assertions.assertNotEquals(Board.Cell.S, Board.Cell.O);

    }

    @Test
    void simpleGameWinColumnTest() throws IOException {
        Board boardTest = new Board(3);
        boardTest.setGameMode('S');

        //ACCEPTANCE CRITERION 5.1
        //3 in a column, red player win
        boardTest.makeMove(0,0,true,false);
        boardTest.makeMove(1,0,false, true);
        boardTest.makeMove(2,0, true, false);

        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.R_WON);

    }

    @Test
    void simpleGameWinRowTest() throws IOException {
        Board boardTest = new Board(3);
        boardTest.setGameMode('S');

        //ACCEPTANCE CRITERION 5.1
        //3 in a row, red player win
        boardTest.makeMove(0,0,true,false);
        boardTest.makeMove(0,1,false, true);
        boardTest.makeMove(0,2, true, false);
        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.R_WON);


    }

    @Test
    void simpleGameWinDiagonalTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('S');

        //ACCEPTANCE CRITERION 5.1
        //3 in a row, red player win
        boardTest.makeMove(0,0,true,false);
        boardTest.makeMove(1,1,false, true);
        boardTest.makeMove(2,2, true, false);
        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.R_WON);


    }

    @Test
    void simpleGameDrawTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('S');

        //ACCEPTANCE CRITERION 5.2
        //every cell filled with an S
        boardTest.makeMove(0,0,true,false);
        boardTest.makeMove(0,1,true,false);
        boardTest.makeMove(0,2,true,false);
        boardTest.makeMove(1,0,true,false);
        boardTest.makeMove(1,1,true,false);
        boardTest.makeMove(1,2,true,false);
        boardTest.makeMove(2,0,true,false);
        boardTest.makeMove(2,1,true,false);
        boardTest.makeMove(2,2,true,false);

        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.DRAW);

    }

    @Test
    void generalGameEndTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('G');

        //ACCEPTANCE CRITERION 7.1
        boardTest.makeMove(0,0,false,true); //red
        boardTest.makeMove(0,1,true,false); //blue
        boardTest.makeMove(0,2,false,true); //red

        boardTest.makeMove(1,0,false,true); // blue
        boardTest.makeMove(1,1,true,false); //red
        boardTest.makeMove(1,2,false,true); // blue

        boardTest.makeMove(2,0,false,true); //red
        boardTest.makeMove(2,1,true,false); //blue
        boardTest.makeMove(2,2,false,true); //red

        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.DRAW);

    }

    @Test
    void generalGameWinTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('G');

        boardTest.makeMove(0,0, true, false); // red
        boardTest.makeMove(0,1, false, true); // blue
        boardTest.makeMove(0,2, true, false); // red

        boardTest.makeMove(1,0, false, true); // blue
        boardTest.makeMove(1,1, false, true); // red
        boardTest.makeMove(1,2, false, true); // blue

        boardTest.makeMove(2,0, true, false); // red
        boardTest.makeMove(2,1, false, true); // blue
        boardTest.makeMove(2,2, true, false); // red

        System.out.println(boardTest.getCurrGameState());
        Assertions.assertEquals(boardTest.getCurrGameState(), Board.GameState.R_WON);
    }

    @Test
    void computerMoveRedTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('G');
        boardTest.setPlayerTurn('R');

        boardTest.generateComputerPlacement(boardTest.boardSize);
        Assertions.assertTrue(boardTest.row_col[0] < boardTest.boardSize);
        Assertions.assertTrue(boardTest.row_col[1] < boardTest.boardSize);

        boardTest.generateMoveType();
        Assertions.assertTrue(boardTest.currMove == 'S' || boardTest.currMove == 'O');


        boardTest.computerMove();

        Board.Cell move = (boardTest.currMove == 'S' &&
                boardTest.getPlayerTurn() == 'R') ? Board.Cell.S : Board.Cell.O;

        Assertions.assertSame(boardTest.logicBoard[boardTest.row_col[0]][boardTest.row_col[1]], move);


    }

    @Test
    void computerMoveBlueTest() throws IOException{
        Board boardTest = new Board(3);
        boardTest.setGameMode('G');
        boardTest.setPlayerTurn('B');

        boardTest.generateComputerPlacement(boardTest.boardSize);
        Assertions.assertTrue(boardTest.row_col[0] < boardTest.boardSize);
        Assertions.assertTrue(boardTest.row_col[1] < boardTest.boardSize);

        boardTest.generateMoveType();
        Assertions.assertTrue(boardTest.currMove == 'S' || boardTest.currMove == 'O');


        boardTest.computerMove();

        Board.Cell move = (boardTest.currMove == 'S' &&
                boardTest.getPlayerTurn() == 'B') ? Board.Cell.S : Board.Cell.O;


        Assertions.assertSame(boardTest.logicBoard[boardTest.row_col[0]][boardTest.row_col[1]], move);


    }
}

