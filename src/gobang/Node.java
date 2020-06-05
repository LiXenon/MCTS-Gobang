package gobang;

public class Node {
    int position; // count from top to bottom, from left to right
    int[] father = new int[100]; // ancestors' position
    Node[][] child = new Node[10][10]; // next possible move
    int generation;
    int piece = -1; // 0 is black(ai defaults black), 1 is white

    private int win = 0;
    private int time = 0;
    double rate = 0;

    public Node(int position, int[] father, int generation) {
        this.position = position;
        System.arraycopy(father, 0, this.father, 0, 100);
        this.generation = generation + 1;
    }

    public Node(int position) {
        this.position = position;
        generation = 0; // first generation
    }

    public boolean isHaveChild() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (child[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInArray(int n, int[] a) {
        for (int i = 0; i < 10; i++) {
            if (a[i] == n) {
                return true;
            }
        }
        return false;
    }

    public void newChild() {
        father[generation] = position;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int childPosition = i * 10 + j;
                if (!isInArray(childPosition, father))
                    child[i][j] = new Node(childPosition, father, generation);
            }
        }
    }

    public void clear(int[] path, int maxGeneration) {
        if (generation == maxGeneration) {
            return;
        }
        child[path[generation + 1] / 10][path[generation + 1] % 10].clear(path, maxGeneration);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (child[i][j] != null) {
                    child[i][j] = null;
                }
            }
        }
    }

    public double getWinRate() {
        if (time > 0) {
            rate = (double) win / time;
        }
        return rate;
    }

    public double getUCB(int kidsPosition) { // get the Upper Confidence Bound(上限置信区间)
        if (child[kidsPosition / 10][kidsPosition % 10].time == 0) {
            return 100;
        }
        double winRate = child[kidsPosition / 10][kidsPosition % 10].getWinRate();
        double UCB = 1.414 * Math.sqrt((double)time / child[kidsPosition / 10][kidsPosition % 10].time);
        return winRate + UCB;
    }
    public void winAdd() {
        win++;
    }

    public void winMinus() {
        win--;
    }

    public void timeAdd() {
        time++;
    }

    public int getGeneration() {
        return generation;
    }
}
