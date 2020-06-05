package gobang;

public class Tree {
    Node[][] node = new Node[10][10];

    public void init() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                node[i][j] = new Node(i * 10 + j);
            }
        }
    }

    public void calculation(Node fatherPiece, Board board) {
        Node currentPiece = fatherPiece;
        int piece = 1;
        while (true) {
            board.move(currentPiece.position, piece);
            double UCB = 0;
            int bestStep = 0;
            int linkStep;
            currentPiece.timeAdd();

            if (!currentPiece.isHaveChild()) {
                currentPiece.newChild();
            }

            linkStep = board.linkJudge(currentPiece.position, piece, 3);
            if (linkStep != -1)
                bestStep = linkStep;
            else {
                linkStep = board.linkJudge(currentPiece.position, piece, 2);
                if (linkStep != -1)
                    bestStep = linkStep;
                else {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (board.isAvailable(i * 10 + j) && currentPiece.child[i][j] != null && board.isLinked(i * 10 + j)) {
                                if (UCB < currentPiece.getUCB(i * 10 + j)) {
                                    UCB = currentPiece.getUCB(i * 10 + j);
                                    bestStep = i * 10 + j;
                                    if (UCB == 100) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (UCB == 100) {
                            break;
                        }
                    }
                }
            }
            if(board.winJudge(currentPiece.position, piece) || board.isFull()) {
                break;
            }

            if (piece == 1) {
                piece = 0;
            } else {
                piece = 1;
            }
            currentPiece = currentPiece.child[bestStep / 10][bestStep % 10];
        }

        int[] path = currentPiece.father;
        int generation = currentPiece.generation;
        if (board.isFull() && !board.winJudge(currentPiece.position, piece)) {
            return;
        }

        currentPiece = fatherPiece;
        int excitation;

        for (int i = fatherPiece.generation + 1; i < fatherPiece.generation + 3; i++) {
            currentPiece = currentPiece.child[path[i] / 10][path[i] % 10];
            if (i == fatherPiece.generation + 1) {
                if (piece == 0)
                currentPiece.winAdd();
                else
                    currentPiece.winMinus();
            } else {
                if (piece == 1)
                    currentPiece.winAdd();
                else
                    currentPiece.winMinus();
            }
            currentPiece.getWinRate();
        }
        currentPiece.clear(path, generation);
//        board.print();
    }
}
