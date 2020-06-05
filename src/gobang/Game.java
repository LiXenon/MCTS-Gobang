package gobang;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        Tree tree = new Tree();
        board.init();
        tree.init();

        Scanner in = new Scanner(System.in);
        System.out.println("Input 1 to go first move, input 2 to go second move.");
        int choice;
        while (true) {
            choice = in.nextInt();
            if (choice == 1 || choice == 2) {
                break;
            } else {
                System.out.println("Illegal input");
            }
        }

        if (choice == 2) {
            board.move(4 * 10 + 4, 0);
            board.print();
        }
        int x, y;
        Node currentPiece;
        System.out.println("Please input your move");
        while (true) {
            x = in.nextInt() - 1;
            y = in.nextInt() - 1;
            if (x > -1 && x < 10 && y > -1 && y < 10 && board.node[x][y].piece == -1) {
                board.move(x * 10 + y, 1);
                currentPiece = tree.node[x][y];
                break;
            }
            System.out.println("Illegal input");
        }
        Node root = currentPiece;
        board.print();

        while (true) {
            for (int i = 0; i < 500; i++) {
                Board copyBoard = new Board();
                copyBoard.init();
                copyBoard.boardCopy(board);
                tree.calculation(currentPiece, copyBoard);
                copyBoard.clear();
            }

            double maxRate = -1.1;
            int maxRatePosition = -1;
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i > -1 && i < 10 && j > -1 && j < 10 && currentPiece.child[i][j] != null) {
                        for (int k = i - 1; k <= i + 1; k++) {
                            for (int l = j - 1; l <= j + 1; l++) {
                                if (k > -1 && k < 10 && l > -1 && l < 10 && currentPiece.child[i][j].child[k][l] != null) {
                                    for (int m = k - 1; m <= k + 1; m++) {
                                        for (int n = l - 1; n <= l + 1; n++) {
                                            if (m > -1 && m < 10 && n > -1 && n < 10 && currentPiece.child[i][j].child[k][l].child[m][n] != null) {
                                                if (currentPiece.child[i][j].child[k][l].child[m][n].rate > maxRate && board.node[m][n].piece == -1) {
                                                    maxRate = currentPiece.child[i][j].child[k][l].child[m][n].rate;
                                                    maxRatePosition = i * 10 + j;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (maxRatePosition == -1) {
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (i > -1 && i < 10 && j > -1 && j < 10 && currentPiece.child[i][j] != null) {
                            if (currentPiece.child[i][j].rate > maxRate && board.node[i][j].piece == -1) {
                                maxRate = currentPiece.child[i][j].rate;
                                maxRatePosition = i * 10 + j;
                            }
                        }
                    }
                }
            }

            board.move(maxRatePosition, 0);
            currentPiece = currentPiece.child[maxRatePosition / 10][maxRatePosition % 10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (root.child[i][j] != null && i * j != maxRatePosition) {
                        root.child[i][j] = null;
                    }
                }
            }
            root = currentPiece;
            System.out.println();
            System.out.println("AI's move is on " + (maxRatePosition / 10 + 1) + " " + (maxRatePosition % 10 + 1));
            board.print();
            if (board.winJudge(maxRatePosition, 0)) {
                System.out.println("AI win!");
                break;
            }

            System.out.println("Please input your move");
            while (true) {
                x = in.nextInt() - 1;
                y = in.nextInt() - 1;
                if (x > -1 && x < 10 && y > -1 && y < 10 && board.node[x][y].piece == -1) {
                    break;
                }
                System.out.println("Illegal input");
            }
            board.move(x * 10 + y, 1);
            if (!currentPiece.isHaveChild()) {
                currentPiece.newChild();
            }
            currentPiece = currentPiece.child[x][y];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (root.child[i][j] != null && (i != x && j != y)) {
                        root.child[i][j] = null;
                    }
                }
            }
            root = currentPiece;
            board.print();
            if (board.winJudge(x * 10 + y, 1)) {
                System.out.println("Player win!");
                break;
            }
        }
    }
}
