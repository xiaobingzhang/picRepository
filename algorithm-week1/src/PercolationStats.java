/**
 * Created by zxb on 2015/6/28.
 */
public class PercolationStats {
    private double[] x;
    private int expTimes;

    // perform T independent experiments on an N-by-N grid

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        x = new double[T + 1];
        expTimes = T;
        for (int i = 1; i <= T; i++) {
            Percolation percolation = new Percolation(N);
            boolean[] isEmpty = new boolean[N + 1];
            int numOfLine = 0;
            while (true) {
                int posX, posY;
                do {
                    posX = StdRandom.uniform(N) + 1;
                    posY = StdRandom.uniform(N) + 1;
                } while (percolation.isOpen(posX, posY));
                percolation.open(posX, posY);
                x[i] += 1;
                if (!isEmpty[posX]) {
                    isEmpty[posX] = true;
                    numOfLine++;
                }
                if (numOfLine == N) {
                    if (percolation.percolates()) {
                        break;
                    }
                }
            }
            x[i] = x[i] / (double) (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double mu = 0.0;
        for (int i = 1; i <= expTimes; i++) {
            mu += x[i];
        }
        return mu / (double) expTimes;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (expTimes == 1) {
            return Double.NaN;
        }
        double sigma = 0.0;
        double mu = mean();
        for (int i = 1; i <= expTimes; i++) {
            sigma += (x[i] - mu) * (x[i] - mu);
        }
        return Math.sqrt(sigma / (double) (expTimes - 1));
    }


    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        double mu = mean();
        double sigma = stddev();
        return mu - 1.96 * sigma / Math.sqrt(expTimes);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double mu = mean;
        double sigma = stddev();
        return mu + 1.96 * sigma / Math.sqrt(expTimes);
    }


    // test client (described below)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.printf("mean = %f\n", percolationStats.mean());
        StdOut.printf("stddev = %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n",
                percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
