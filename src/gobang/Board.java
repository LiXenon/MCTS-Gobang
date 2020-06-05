package gobang;

public class Board {
    Node[][] node = new Node[10][10];

    public void print() {
        for (int i = -1; i < 10; i++) {
            for (int j = -1; j < 10; j++) {
                if (i == -1 && j == -1) {
                    System.out.print("   ");
                    continue;
                }
                if (i == -1) {
                    System.out.print(j + 1 + "  ");
                    continue;
                }
                if (j == -1) {
                    if (i == 9)
                        System.out.print(i + 1 + " ");
                    else
                       System.out.print(i + 1 + "  ");
                    continue;
                }
                if (node[i][j].piece == -1) {
                    System.out.print('+' + "  ");
                } else if (node[i][j].piece == 0) {
                    System.out.print('●' + "  ");
                } else if (node[i][j].piece == 1) {
                    System.out.print('○' + "  ");
                }
            }
            System.out.println();
        }
    }

    public void boardCopy(Board target) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                node[i][j].piece = target.node[i][j].piece;
            }
        }
    }

    public boolean isLinked(int position) {
        int x = position / 10;
        int y = position % 10;
        if (node[x][y].piece == -1) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i < 10 && i > -1 && j > -1 && j < 10 && node[i][j].piece != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAvailable(int position) {
        return node[position / 10][position % 10].piece == -1;
    }

    public void init() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                node[i][j] = new Node(i * 10 + j);
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (node[i][j].piece == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                node[i][j].piece = -1;
            }
        }
    }
    public void move(int position, int piece) {
        node[position / 10][position % 10].piece = piece;
    }

    public int linkJudge(int position, int piece, int maxLink) {
        int currentX = position / 10;
        int currentY = position % 10;
        int firstChoice;
        int secondChoice;
        int lastPosition;

        int x = currentX;
        int y = currentY;
        while (x > 0 && y > 0 && node[x - 1][y - 1].piece == piece) {
            x--;
            y--;
        }
        if (x > 1 && y > 1 && node[x - 1][y - 1].piece == -1)
            firstChoice = (x - 1) * 10 + y - 1;
        else
            firstChoice = -1;
        lastPosition = leftObliqueJudge(x, y, piece, 0, maxLink);
        if (lastPosition != -1) {
            secondChoice = lastPosition + 11;
            if (maxLink == 3) {
                if (firstChoice != -1)
                    return firstChoice;
                else if (secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                    return secondChoice;
                }
            } else if (firstChoice != -1 && secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1){
                return firstChoice;
            }
        }

        x = currentX;
        y = currentY;
        while (y > 0 && node[x][y - 1].piece == piece) {
            y--;
        }
        if (y > 1 && node[x][y - 1].piece == -1)
            firstChoice = x * 10 + y - 1;
        else
            firstChoice = -1;
        lastPosition = lineJudge(x, y, piece, 0, maxLink);
        if (lastPosition != -1) {
            secondChoice = lastPosition + 1;
            if (maxLink == 3) {
                if (firstChoice != -1)
                    return firstChoice;
                else if (secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                    return secondChoice;
                }
            } else if (firstChoice != -1 && secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1){
                return firstChoice;
            }
        }

        x = currentX;
        y = currentY;
        while (x < 9 && y > 0 && node[x + 1][y - 1].piece == piece) {
            x++;
            y--;
        }
        if (x < 9 && y > 1 && node[x + 1][y - 1].piece == -1)
            firstChoice = (x + 1) * 10 + y - 1;
        else
            firstChoice = -1;
        lastPosition = rightObliqueJudge(x, y, piece, 0, maxLink);
        if (lastPosition != -1) {
            secondChoice = lastPosition - 9;
            if (maxLink == 3) {
                if (firstChoice != -1)
                    return firstChoice;
                else if (secondChoice > 0 && lastPosition % 10 != 9 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                    return secondChoice;
                }
            } else if (firstChoice != -1 && secondChoice > 0 && lastPosition % 10 != 9 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                double random = Math.random();
                if (random > 0.5)
                    return firstChoice;
                else
                    return secondChoice;
            }
        }

        x = currentX;
        y = currentY;
        while (x > 0 && node[x - 1][y].piece == piece) {
            x--;
        }
        if (x > 1 && node[x - 1][y].piece == -1)
            firstChoice = (x - 1) * 10 + y;
        else
            firstChoice = -1;
        lastPosition = rowJudge(x, y, piece, 0, maxLink);
        if (lastPosition != -1) {
            secondChoice = lastPosition + 10;
            if (maxLink == 3) {
                if (firstChoice != -1)
                    return firstChoice;
                else if (secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                    return secondChoice;
                }
            } else if (firstChoice != -1 && secondChoice < 100 && node[secondChoice / 10][secondChoice % 10].piece == -1) {
                double random = Math.random();
                if (random > 0.5)
                    return firstChoice;
                else
                    return secondChoice;
            }
        }

        return -1;
    }

    public boolean winJudge(int position, int piece) {
        int maxLink = 4;
        int currentX = position / 10;
        int currentY = position % 10;

        int x = currentX;
        int y = currentY;
        while (x > 0 && y > 0 && node[x - 1][y - 1].piece == piece) {
            x--;
            y--;
        }
        if (leftObliqueJudge(x, y, piece, 0, maxLink) != -1) {
            return true;
        }

        x = currentX;
        y = currentY;
        while (y > 0 && node[x][y - 1].piece == piece) {
            y--;
        }
        if (lineJudge(x, y, piece, 0, maxLink) != -1) {
            return true;
        }

        x = currentX;
        y = currentY;
        while (x < 9 && y > 0 && node[x + 1][y - 1].piece == piece) {
            x++;
            y--;
        }
        if (rightObliqueJudge(x, y, piece, 0, maxLink) != -1) {
            return true;
        }

        x = currentX;
        y = currentY;
        while (x > 0 && node[x - 1][y].piece == piece) {
            x--;
        }
        if (rowJudge(x, y, piece, 0, maxLink) != -1) {
            return true;
        }

        return false;
    }

    private int leftObliqueJudge(int x, int y, int piece, int link, int maxLink) {
        if (link == maxLink) {
            return x * 10 + y;
        }
        if (x < 9 && y < 9 && node[x + 1][y + 1].piece == piece) {
            return leftObliqueJudge(++x, ++y, piece, ++link, maxLink);
        } else {
            return -1;
        }
    }

    private int rightObliqueJudge(int x, int y, int piece, int link, int maxLink) {
        if (link == maxLink) {
            return x * 10 + y;
        }
        if (x > 0 && y < 9 && node[x - 1][y + 1].piece == piece) {
            return rightObliqueJudge(--x, ++y, piece, ++link, maxLink);
        } else {
            return -1;
        }
    }

    private int lineJudge(int x, int y, int piece, int link, int maxLink) {
        if (link == maxLink) {
            return x * 10 + y;
        }
        if (y < 9 && node[x][y + 1].piece == piece) {
            return lineJudge(x, ++y, piece, ++link, maxLink);
        } else {
            return -1;
        }
    }

    private int rowJudge(int x, int y, int piece, int link, int maxLink) {
        if (link == maxLink) {
            return x * 10 + y;
        }
        if (x < 9 && node[x + 1][y].piece == piece) {
            return rowJudge(++x, y, piece, ++link, maxLink);
        } else {
            return -1;
        }
    }
}
