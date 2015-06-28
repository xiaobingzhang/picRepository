/**
 * @author: hanxi.zxb
 * @Date :2015年6月28日 15:26:37
 * Created by zxb on 2015/6/28.
 */
public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private WeightedQuickUnionUF weightedQuickUnionUFTop;
    private int col, row;
    private boolean[] matrix;
    private boolean alreadyPercolates;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N < 1) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
        weightedQuickUnionUFTop = new WeightedQuickUnionUF(N * N + 2);

        alreadyPercolates = false;
        row = N;
        col = N;
        matrix = new boolean[N * N + 1];
    }

    private void validate(int i, int j) {
        if (i < 1 || i > row) {
            throw new IndexOutOfBoundsException("row index " + i + " out bounds");
        }
        if (j < 1 || j > col) {
            throw new IndexOutOfBoundsException("col index" + j + " out of bounds");
        }
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j);
        int curIndex = (i - 1) * col + j;
        matrix[curIndex] = true;
        //the first line set both uf union(curIndex,0)
        if (i == 1) {
            weightedQuickUnionUF.union(curIndex, 0);
            weightedQuickUnionUFTop.union(curIndex, 0);
        }
        //the others only set
        if (i == row) {
            weightedQuickUnionUF.union(curIndex, row * col + 1);
        }

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int k = 0; k < dx.length; k++) {
            int posX = i + dx[k];
            int posY = j + dy[k];
            if (posX <= row && posX >= 1 && posY <= row && posY >= 1 && isOpen(posX, posY)) {
                weightedQuickUnionUF.union(curIndex, (posX - 1) * col + posY);
                weightedQuickUnionUFTop.union(curIndex, (posX - 1) * col + posY);
            }
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return matrix[(i - 1) * col + j];
    }


    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        int index = (i - 1) * col + j;
        if (weightedQuickUnionUFTop.find(index) == weightedQuickUnionUFTop.find(0)) {
            return true;
        }
        return false;
    }

    public boolean percolates() {
        if (alreadyPercolates) {
            return true;
        }
        if (weightedQuickUnionUF.find(0) == weightedQuickUnionUF.find(row * col + 1)) {
            alreadyPercolates = true;
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        Percolation percolation = new Percolation(2);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 1);
        System.out.println(percolation.percolates());
    }
}
