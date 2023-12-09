package sprint2.sprint2;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class GUI{
    BorderPane root;
    Insets padding = new Insets(20);
    Board logicBoard; //BOARD OBJECT


    //SCENES
    Stage primaryStage;
    Scene settingsScene;
    Scene gamePlayScene;


    //ATTRIBUTES
    int boardSize;
    boolean simple;
    boolean general;
    boolean sRedMove;
    boolean oRedMove;
    boolean sBlueMove;
    boolean oBlueMove;
    char turn;
    boolean gameEnd;
    Square[][] squares;

    //SETTINGS SCENE COMPONENTS
    Pane settingsRoot = new Pane();
    Slider boardSizeSlider;
    ToggleGroup gameModeGroup = new ToggleGroup();
    RadioButton simpleGameButton;
    RadioButton generalGameButton;

    ToggleGroup redPlayerTypeGroup = new ToggleGroup();
    RadioButton computerPlayerRed;
    RadioButton humanPlayerRed;
    char redPlayerType;
    ToggleGroup bluePlayerTypeGroup = new ToggleGroup();
    RadioButton computerPlayerBlue;
    RadioButton humanPlayerBlue;
    char bluePlayerType;
    Button startGameButton;

    //GAME SCENE COMPONENTS
    GridPane gameGrid;
    VBox soRed = new VBox();
    VBox soBlue = new VBox();
    VBox modeAndGameStatusLabels = new VBox();

    private Label gameStatusLabel = new Label("Red Player's turn");
    Font boldFont = Font.font("Arial", FontWeight.BOLD, 14);

    private Label redPlayerLabel = new Label("Red Player");
    RadioButton sMoveRedButton;
    RadioButton oMoveRedButton;
    ToggleGroup redMoveToggleGroup = new ToggleGroup();
    private Label bluePlayerLabel = new Label("Blue Player");
    RadioButton sMoveBlueButton;
    RadioButton oMoveBlueButton;
    ToggleGroup bluePlayerToggleGroup = new ToggleGroup();
    Button newGameButton;

    //CONSTRUCTOR
    GUI (BorderPane root) throws IOException {
        this.root = root;
        root.setPadding(padding);

        gameGrid = new GridPane(); //define game grid
        gameGrid.setPadding(padding);

        //SETTINGS PANE COMPONENTS
        this.boardSizeSlider = new Slider(3, 15, 8.9);
        this.simpleGameButton = new RadioButton("Simple Game");
        this.generalGameButton = new RadioButton("General Game");
        this.humanPlayerRed = new RadioButton("human");
        this.computerPlayerRed = new RadioButton("computer");
        this.humanPlayerBlue = new RadioButton("human");
        this.computerPlayerBlue = new RadioButton("computer");
        this.startGameButton = new Button("Start Game");

        //GAME PANE COMPONENTS
        this.sMoveRedButton = new RadioButton("S");
        this.oMoveRedButton = new RadioButton("O");
        this.sMoveBlueButton = new RadioButton("S");
        this.oMoveBlueButton = new RadioButton("O");
        this.newGameButton = new Button("New Game");

    }

    public void setPrimaryStage (Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setGameScene(Scene gameScene){
        this.gamePlayScene = gameScene;
    }

    public void setSettingsScene(Scene settingsScene){
        this.settingsScene = settingsScene;
    }
    public void SettingsPane(Pane settingsRoot){

        //SETTINGS SCENE LABEL
        Label promptSettingsLabel = new Label("Select a game board size and a game mode below:");
        promptSettingsLabel.setFont(new Font("Arial", 16));
        promptSettingsLabel.setTranslateX(70);
        promptSettingsLabel.setTranslateY(50);

        //BOARD SIZE LABEL
        Label boardSizeLabel = new Label("Select a board size");
        boardSizeLabel.setFont(new Font("Arial", 14));
        boardSizeLabel.setTranslateX(50);
        boardSizeLabel.setTranslateY(100);

        //BOARD SIZE SLIDER
        boardSizeSlider.setTranslateX(50);
        boardSizeSlider.setTranslateY(125);
        boardSizeSlider.setMax(16);
        boardSizeSlider.setMin(3);
        boardSizeSlider.setOnMouseReleased( e -> {
            boardSize = (int) boardSizeSlider.getValue();
            try {
                logicBoard = new Board(boardSize);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //GAME MODE LABEL
        Label gameModeLabel = new Label("Select a game mode");
        gameModeLabel.setFont(new Font("Arial", 14));
        gameModeLabel.setTranslateX(50);
        gameModeLabel.setTranslateY(160);

        //GAME MODE BUTTONS
        //simple game
        simpleGameButton.setFont(new Font("Arial" ,14));
        simpleGameButton.setToggleGroup(gameModeGroup);
        simpleGameButton.setTranslateX(50);
        simpleGameButton.setTranslateY(185);
        simpleGameButton.setOnAction( event -> {
            simple = true;
            general = false;
        });

        //general game
        generalGameButton.setFont(new Font("Arial", 14));
        generalGameButton.setToggleGroup(gameModeGroup);
        generalGameButton.setTranslateX(50);
        generalGameButton.setTranslateY(210);
        generalGameButton.setOnAction(event -> {
            simple = false;
            general = true;

        });

        //Red player label
        Label redPlayerLabel = new Label("Red Player");
        redPlayerLabel.setFont(new Font("Arial", 14));
        redPlayerLabel.setTranslateX(325);
        redPlayerLabel.setTranslateY(100);

        //PLAYER BUTTONS
        //red player buttons
        humanPlayerRed.setFont(new Font("Arial", 14));
        humanPlayerRed.setToggleGroup(redPlayerTypeGroup);
        humanPlayerRed.setTranslateX(325);
        humanPlayerRed.setTranslateY(125);
        humanPlayerRed.setOnAction(event -> {
            redPlayerType = 'H';
        });

        computerPlayerRed.setFont(new Font("Arial", 14));
        computerPlayerRed.setToggleGroup(redPlayerTypeGroup);
        computerPlayerRed.setTranslateX(325);
        computerPlayerRed.setTranslateY(150);
        computerPlayerRed.setOnAction(event -> {
            redPlayerType = 'C';
        });

        //blue player label
        Label bluePlayerLabel = new Label("Blue Player");
        bluePlayerLabel.setFont(new Font("Arial", 14));
        bluePlayerLabel.setTranslateX(325);
        bluePlayerLabel.setTranslateY(200);

        //blue player buttons
        humanPlayerBlue.setFont(new Font("Arial", 14));
        humanPlayerBlue.setToggleGroup(bluePlayerTypeGroup);
        humanPlayerBlue.setTranslateX(325);
        humanPlayerBlue.setTranslateY(225);
        humanPlayerBlue.setOnAction(event -> {
            bluePlayerType = 'H';
        });

        computerPlayerBlue.setFont(new Font("Arial", 14));
        computerPlayerBlue.setToggleGroup(bluePlayerTypeGroup);
        computerPlayerBlue.setTranslateX(325);
        computerPlayerBlue.setTranslateY(250);
        computerPlayerBlue.setOnAction(event -> {
            bluePlayerType = 'C';
        });


        //START BUTTON
        startGameButton.setTranslateX(210);
        startGameButton.setTranslateY(300);


        //PLACE COMPONENTS IN SETTINGS SCENE
        settingsRoot.getChildren().addAll(
                promptSettingsLabel,
                boardSizeLabel,
                gameModeLabel,
                boardSizeSlider,
                simpleGameButton,
                generalGameButton,
                startGameButton,
                redPlayerLabel,
                humanPlayerRed,
                computerPlayerRed,
                bluePlayerLabel,
                humanPlayerBlue,
                computerPlayerBlue


        );

        startGameButton.setOnAction(event -> {
            if(simple && !general){
                logicBoard.setGameMode('S');
                logicBoard.setRedPlayerType(redPlayerType);
                logicBoard.setBluePlayerType(bluePlayerType);

                logicBoard.writeInitializationToOutput();


            }

            if(!simple && general){
                logicBoard.setGameMode('G');
                logicBoard.setRedPlayerType(redPlayerType);
                logicBoard.setBluePlayerType(bluePlayerType);

                logicBoard.writeInitializationToOutput();

            }
            try {
                GamePane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


    }


    public void GamePane() throws IOException {
        //setGameModeTest()
        primaryStage.setScene(gamePlayScene);

        //GAME MODE LABEL
        if(simple){
            Label gameModeLabel = new Label("Game Mode: Simple");
            modeAndGameStatusLabels.getChildren().add(gameModeLabel);

        }
        if(general){
            Label gameModeLabel = new Label("Game Mode: General");
            modeAndGameStatusLabels.getChildren().add(gameModeLabel);

        }

        modeAndGameStatusLabels.getChildren().add(gameStatusLabel);


        //CREATE GUI BOARD
        buildBoardGui();
        gameEnd = false;

        //SET RED PLAYER LABELS AND BUTTONS
        redPlayerLabel = new Label("Red Player");
        sMoveRedButton.setFont(new Font("Arial", 14));
        sMoveRedButton.setToggleGroup(redMoveToggleGroup);
        sMoveRedButton.setOnAction(event -> {
            sRedMove = true;
            oRedMove = false;

        });
        soRed.getChildren().add(redPlayerLabel);
        soRed.getChildren().add(sMoveRedButton);

        oMoveRedButton.setFont(new Font("Arial", 14));
        oMoveRedButton.setToggleGroup(redMoveToggleGroup);
        oMoveRedButton.setOnAction(event -> {
            sRedMove = false;
            oRedMove = true;

        });
        //buttons to HStack (Vbox??)
        soRed.getChildren().add(oMoveRedButton);
        soRed.setSpacing(10);

        //SET BLUE PLAYER LABELS AND BUTTONS
        bluePlayerLabel = new Label("Blue Player");
        sMoveBlueButton.setFont(new Font("Arial", 14));
        sMoveBlueButton.setToggleGroup(bluePlayerToggleGroup);
        sMoveBlueButton.setOnAction(event -> {
            sBlueMove = true;
            oBlueMove = false;

        });
        soBlue.getChildren().add(bluePlayerLabel);
        soBlue.getChildren().add(sMoveBlueButton);

        oMoveBlueButton.setFont(new Font("Arial", 14));
        oMoveBlueButton.setToggleGroup(bluePlayerToggleGroup);
        oMoveBlueButton.setOnAction(event -> {
            sBlueMove = false;
            oBlueMove = true;

        });

        soBlue.getChildren().add(oMoveBlueButton);
        soBlue.setSpacing(10);

        //SET NEW GAME BUTTON
        newGameButton.setOnAction(event -> {

            Main backToMain = null;
            try {
                backToMain = new Main();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            backToMain.start(primaryStage);

        });

        //FORMAT SCENE
        root.setTop(modeAndGameStatusLabels);
        root.setLeft(soBlue);
        root.setRight(soRed);
        root.setCenter(gameGrid);
        root.setBottom(newGameButton);


    }
    public void buildBoardGui() throws IOException {

        squares = new Square[boardSize][boardSize];
        for (int i =0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                gameGrid.add(squares[i][j] = new Square(i, j), j, i);


            }
        }

        if (redPlayerType == 'C'){
            logicBoard.computerMove();
            logicBoard.changeTurns();

            drawBoard();
            displayGameStatus();

            if(bluePlayerType == 'C'){
                while(logicBoard.getCurrGameState() == Board.GameState.PLAYING){
                    logicBoard.bothComputers();
                    drawBoard();
                    displayGameStatus();

                }

            }

        }

    }


    private void drawBoard(){

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                squares[row][col].getChildren().clear();

                if (logicBoard.getCell(row, col) == Board.Cell.S) {

                    if(logicBoard.colorGrid[row][col] == Board.Color.BLACK){
                        squares[row][col].drawS();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.RED){
                        squares[row][col].drawRedS();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.BLUE){
                        squares[row][col].drawBlueS();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.PURPLE){
                        squares[row][col].drawPurpleS();
                    }



                }

                else if (logicBoard.getCell(row, col) == Board.Cell.O) {

                    if(logicBoard.colorGrid[row][col] == Board.Color.BLACK){
                        squares[row][col].drawO();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.RED){
                        squares[row][col].drawRedO();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.BLUE){
                        squares[row][col].drawBlueO();
                    }

                    if(logicBoard.colorGrid[row][col] == Board.Color.PURPLE){
                        squares[row][col].drawPurpleO();
                    }
                }

            }
        }



    }

    private void displayGameStatus() throws IOException {

        if(logicBoard.getCurrGameState() == Board.GameState.PLAYING){
            if(logicBoard.getPlayerTurn() == 'R'){
                gameStatusLabel.setText("Red player's turn");

            }
            else{
                gameStatusLabel.setText("Blue Player's Turn");
            }
        }

        if(logicBoard.getCurrGameState() == Board.GameState.R_WON){
            gameStatusLabel.setText("Red player Win!!");
            gameStatusLabel.setFont(boldFont);
            logicBoard.closeOutput();
            gameEnd = true;
        }

        if(logicBoard.getCurrGameState() == Board.GameState.B_WON){
            gameStatusLabel.setText("Blue player Win!!");
            gameStatusLabel.setFont(boldFont);
            logicBoard.closeOutput();
            gameEnd = true;
        }

        if(logicBoard.getCurrGameState() == Board.GameState.DRAW){
            gameStatusLabel.setText("Game ended in a draw.");
            gameStatusLabel.setFont(boldFont);
            logicBoard.closeOutput();
            gameEnd = true;

        }
    }



    public class Square extends Pane {
        private int row;
        private int col;
        private Text text;

        //CONSTRUCTOR
        public Square(int row, int column){
            this.row = row;
            this.col = column;
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000,2000);
            this.setOnMouseClicked(event -> {
                try {
                    handleMouseClick();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        }
        void handleMouseClick() throws IOException {
            if (!gameEnd) {
                turn = logicBoard.getPlayerTurn();

                if (turn == 'R') {
                    //red player human move
                    logicBoard.makeMove(row, col, sRedMove, oRedMove);

                    //if blue player is computer, their move will be drawn too
                    drawBoard();

                    displayGameStatus();

                }


                if(turn == 'B'){
                    //blue player human move
                    logicBoard.makeMove(row, col, sBlueMove, oBlueMove);

                    drawBoard();
                    displayGameStatus();

                }


            }

        }



        public void drawS() {
            File file = new File("./src/assets/S.png");
            Image sImage = new Image(file.toURI().toString());
            ImageView S = new ImageView(sImage);
            StackPane sBox = new StackPane(S);

            sBox.prefWidthProperty().bind(this.widthProperty());
            sBox.prefHeightProperty().bind(this.heightProperty());
            sBox.setAlignment(Pos.CENTER);

            S.fitWidthProperty().bind(this.widthProperty().subtract(15));
            S.fitHeightProperty().bind(this.heightProperty().subtract(15));


            squares[row][col].getChildren().add(sBox);
        }

        public void drawRedS() {
            File file = new File("./src/assets/redS.png");
            Image sImage = new Image(file.toURI().toString());
            ImageView redS = new ImageView(sImage);
            StackPane sBox = new StackPane(redS);

            sBox.prefWidthProperty().bind(this.widthProperty());
            sBox.prefHeightProperty().bind(this.heightProperty());
            sBox.setAlignment(Pos.CENTER);

            redS.fitWidthProperty().bind(this.widthProperty().subtract(15));
            redS.fitHeightProperty().bind(this.heightProperty().subtract(15));


            squares[row][col].getChildren().add(sBox);
        }

        public void drawBlueS() {
            File file = new File("./src/assets/blueS.png");
            Image sImage = new Image(file.toURI().toString());
            ImageView redS = new ImageView(sImage);
            StackPane sBox = new StackPane(redS);

            sBox.prefWidthProperty().bind(this.widthProperty());
            sBox.prefHeightProperty().bind(this.heightProperty());
            sBox.setAlignment(Pos.CENTER);

            redS.fitWidthProperty().bind(this.widthProperty().subtract(15));
            redS.fitHeightProperty().bind(this.heightProperty().subtract(15));


            squares[row][col].getChildren().add(sBox);
        }

        public void drawPurpleS() {
            File file = new File("./src/assets/purpleS.png");
            Image sImage = new Image(file.toURI().toString());
            ImageView redS = new ImageView(sImage);
            StackPane sBox = new StackPane(redS);

            sBox.prefWidthProperty().bind(this.widthProperty());
            sBox.prefHeightProperty().bind(this.heightProperty());
            sBox.setAlignment(Pos.CENTER);

            redS.fitWidthProperty().bind(this.widthProperty().subtract(15));
            redS.fitHeightProperty().bind(this.heightProperty().subtract(15));


            squares[row][col].getChildren().add(sBox);
        }

        public void drawO() {
            File file = new File("./src/assets/O.png");
            Image oImage = new Image(file.toURI().toString());
            ImageView O = new ImageView(oImage);
            StackPane oBox = new StackPane(O);

            oBox.prefWidthProperty().bind(this.widthProperty());
            oBox.prefHeightProperty().bind(this.heightProperty());
            oBox.setAlignment(Pos.CENTER);

            O.fitWidthProperty().bind(this.widthProperty().subtract(17));
            O.fitHeightProperty().bind(this.heightProperty().subtract(17));


            squares[row][col].getChildren().add(oBox);
        }

        public void drawRedO() {
            File file = new File("./src/assets/redO.png");
            Image oImage = new Image(file.toURI().toString());
            ImageView O = new ImageView(oImage);
            StackPane oBox = new StackPane(O);

            oBox.prefWidthProperty().bind(this.widthProperty());
            oBox.prefHeightProperty().bind(this.heightProperty());
            oBox.setAlignment(Pos.CENTER);

            O.fitWidthProperty().bind(this.widthProperty().subtract(17));
            O.fitHeightProperty().bind(this.heightProperty().subtract(17));


            squares[row][col].getChildren().add(oBox);
        }

        public void drawBlueO() {
            File file = new File("./src/assets/blueO.png");
            Image oImage = new Image(file.toURI().toString());
            ImageView O = new ImageView(oImage);
            StackPane oBox = new StackPane(O);

            oBox.prefWidthProperty().bind(this.widthProperty());
            oBox.prefHeightProperty().bind(this.heightProperty());
            oBox.setAlignment(Pos.CENTER);

            O.fitWidthProperty().bind(this.widthProperty().subtract(17));
            O.fitHeightProperty().bind(this.heightProperty().subtract(17));


            squares[row][col].getChildren().add(oBox);
        }

        public void drawPurpleO() {
            File file = new File("./src/assets/purpleO.png");
            Image oImage = new Image(file.toURI().toString());
            ImageView O = new ImageView(oImage);
            StackPane oBox = new StackPane(O);

            oBox.prefWidthProperty().bind(this.widthProperty());
            oBox.prefHeightProperty().bind(this.heightProperty());
            oBox.setAlignment(Pos.CENTER);

            O.fitWidthProperty().bind(this.widthProperty().subtract(17));
            O.fitHeightProperty().bind(this.heightProperty().subtract(17));


            squares[row][col].getChildren().add(oBox);
        }

    }
}
