import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andrii Seliverstov
 */
public class HorseJumpsOnTable extends Application {
    static int rowSize;
    static int columnSize;
    static int[] rows;
    static int[] columns;

    public static void main(String[] args) throws InterruptedException {
        /**
         * Тут меняем размер поля, на котором хотим выгулять коня
         */
        rowSize = 6;
        columnSize = 6;

        rows = new int[rowSize * columnSize];
        columns = new int[rowSize * columnSize];

        rows[0] = (int) (Math.random() * (rowSize - 1));
        columns[0] = (int) (Math.random() * (columnSize - 1));

        try {
            calcHorsePath(rows, columns, 0, new boolean[rowSize][columnSize]);
        } catch (RuntimeException ignore) {/* NOP */}

        /** После подсчетов запускаем наше окно */
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Chess Desk created here
         */
        final GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(10));

        Label tempLabel;
        String[] chessLines = {"a", "b", "c", "d", "e", "f", "e", "h"};
        for (int i = 0; i < columnSize; i++) {
            tempLabel = new Label(chessLines[i]);
            tempLabel.setFont(new Font(18));
            tempLabel.setAlignment(Pos.CENTER);
            tempLabel.setPrefSize(100, 20);
            grid.add(tempLabel, i + 1, 0);

            tempLabel = new Label("" + (columnSize - i));
            tempLabel.setFont(new Font(18));
            tempLabel.setAlignment(Pos.CENTER);
            tempLabel.setPrefSize(20, 100);
            grid.add(tempLabel, 0, i + 1);
        }

        for (int i = 0; i < rowSize; i++) {
            tempLabel = new Label(chessLines[i]);
            tempLabel.setFont(new Font(18));
            tempLabel.setAlignment(Pos.CENTER);
            tempLabel.setPrefSize(100, 20);
            grid.add(tempLabel, i + 1, 10);

            tempLabel = new Label("" + (rowSize - i));
            tempLabel.setFont(new Font(18));
            tempLabel.setAlignment(Pos.CENTER);
            tempLabel.setPrefSize(20, 100);
            grid.add(tempLabel, rowSize + 2, i + 1);
        }


        final AtomicInteger counter = new AtomicInteger(1);
        grid.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int iter = counter.get();
                if (iter < rowSize * columnSize) {
                    grid.add(new ImageView(new Image("cross.png", 96, 96, true, true)), rows[iter - 1] + 1, columns[iter - 1] + 1);
                    grid.add(new ImageView(new Image("horse.png", 96, 96, true, true)), rows[iter] + 1, columns[iter] + 1);
                    counter.incrementAndGet();
                }
            }
        });
        grid.add(new ImageView(new Image("horse.png", 96, 96, true, true)), rows[0] + 1, columns[0] + 1);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private static void calcHorsePath(int[] rows, int[] columns, int step, boolean[][] testBoard) {
        testBoard[rows[step]][columns[step]] = true;
        if (step == rowSize * columnSize - 1) {
            System.out.println("Случилось " + step);
            HorseJumpsOnTable.rows = rows;
            HorseJumpsOnTable.columns = columns;
            throw new RuntimeException("I found path");
        }

        // up
        if (rows[step] - 2 >= 0) {
            rows[step + 1] = rows[step] - 2;
            // up left
            if (columns[step] - 1 >= 0) {
                columns[step + 1] = columns[step] - 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
            //up right
            if (columns[step] + 1 < columnSize) {
                columns[step + 1] = columns[step] + 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
        }
        // down
        if (rows[step] + 2 < rowSize) {
            rows[step + 1] = rows[step] + 2;
            // down left
            if (columns[step] - 1 >= 0) {
                columns[step + 1] = columns[step] - 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
            // down right
            if (columns[step] + 1 < columnSize) {
                columns[step + 1] = columns[step] + 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
        }
        // left
        if (columns[step] - 2 >= 0) {
            columns[step + 1] = columns[step] - 2;
            // left up
            if (rows[step] - 1 >= 0) {
                rows[step + 1] = rows[step] - 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
            // left down
            if (rows[step] + 1 < rowSize) {
                rows[step + 1] = rows[step] + 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
        }
        // right
        if (columns[step] + 2 < columnSize) {
            columns[step + 1] = columns[step] + 2;
            // right up
            if (rows[step] - 1 >= 0) {
                rows[step + 1] = rows[step] - 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
            // right down
            if (rows[step] + 1 < rowSize) {
                rows[step + 1] = rows[step] + 1;
                if (!testBoard[rows[step + 1]][columns[step + 1]]) {
                    calcHorsePath(rows, columns, step + 1, testBoard);
                    testBoard[rows[step + 1]][columns[step + 1]] = false;
                }
            }
        }
    }
}