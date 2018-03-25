package umlKurs;

import java.util.ArrayList;

import static java.lang.Math.*;

public class ChartCreator {
    double gammaK, length, D, c, alpha, time, eps;
    int nt, nx, nSum;

    public ChartCreator(double gammaK, double length, double d, double c, double alpha, double time, int N, double xStep, double tStep) {
        this.gammaK = gammaK;
        this.length = length;
        D = d;
        this.c = c;
        this.alpha = alpha;
        this.time = time;
        this.nt = (int) (time/tStep);
        this.nx = (int) (length/xStep);
        this.nSum = N;
    }

    public double getLength() {
        return length;
    }

    public double getTime() {
        return time;
    }

    public int getNt() {
        return nt;
    }

    public int getNx() {
        return nx;
    }

    public int getnSum() {
        return nSum;
    }

    private double[] getNextKStep(double[] q, int k, double gam, double r, double p) {
        double[] nextCol = new double[q.length];
        q[1] += alpha*gam*exp(-gammaK*time*k/nt)/c;
        q[nx-1] += alpha*gam*exp(-gammaK*time*k/nt)/c;
        double[] alf = new double[nx + 1];
        double[] bet = new double[nx + 1];
        alf[1] = -r/p;
        bet[1] = q[1]/p;
        for (int j = 2; j <= nx - 2; j++) {
            alf[j] = -r/(r*alf[j-1] + p);
            bet[j] = (q[j] - r*bet[j-1])/(r*alf[j-1] + p);
        }
        nextCol[nx-1] = (q[nx-1] - r*bet[nx - 2])/(r*alf[nx-2] + p);
        for (int m = nx - 2; m > 0; m--) {
            nextCol[m] = alf[m]*nextCol[m+1] + bet[m];
        }
        return nextCol;
    }

    public double[][] composeRunChartArray(){
        double[][] result = new double[nx+1][nt+1];
        double[] q = new double [nx + 1];
        double gam = (time/nt)/(Math.pow(length/nx, 2.0));
        double r = -alpha*gam/c;
        double p = 1 + (D/c)*time/nt + 2*alpha*gam/c;
        for(int i = 0; i <= nx; i++) {
            result[i][0] = 0;
            q[i] = 0;
        }
        for(int k = 0; k < nt; k++) {
            q = getNextKStep(q, k, gam, r, p);
            for(int i = 0; i <= nx; i++) {
                result[i][k+1] = q[i];
            }
        }
        for (int j = 0; j <= nt; j++) {
            result[0][j] = exp(-gammaK*time*j/nt);
            result[nx][j] = result[0][j];
        }
        return result;
    }

    public double ant(int i, double t) {
        double alphCPiNL2;
        double divKDC = gammaK - D/c;
        alphCPiNL2 = (alpha/c)*pow(PI*i/length, 2.0);
        double an = divKDC*exp(-gammaK*t)/(alphCPiNL2 - divKDC);
        an += -divKDC*exp(-(alphCPiNL2 + D/c)*t)/(alphCPiNL2 - divKDC);
        an += -exp(-(alphCPiNL2 + D/c)*t);
        return an;
    }



    public double decompositionOfOne(int i, double x) { //базис
        double fx = 2/(PI*i)*(1 - pow(-1, (double)i));
        double sinPiNXL = sin(PI*i*x/length);
        return fx*sinPiNXL;
    }

    public double getSum(double x, double t) {
        double result = 0;
        for (int i = 1; i <= nSum; i++) {
            result += (ant(i, t) /*+ exp(-gammaK*t)*/)*decompositionOfOne(i, x) ;
        }
        return result + exp(-gammaK*t);
    }

    public double[][] composeSeparChartArray(){
        double[][] result = new double[nx+1][nt+1];
        for(int i = 1; i < nx; i++) {
            for (int j = 0; j <= nt; j++) {
                result[i][j] = getSum(length*i/nx, time*j/nt);
            }
        }
        for (int j = 0; j <= nt; j++) {
            result[0][j] = exp(-gammaK*time*j/nt);
            result[nx][j] = result[0][j];
        }
        return result;
    }

    /*private int calcN(double eps) {
        int startN = 50;
        int step = 10;
        double ans1, ans2;
        do {
            ans1 = 0;
            for(int i = 1; i <= startN; i++) {
                ans1 += ant(i, 0) * decompositionOfOne(i, length/2*startN);
            }
            ans2 = 0;
            for(int i = 1; i <= startN + step; i++) {
                ans1 += ant(i, 0) * decompositionOfOne(i, length/2*(startN + step));
            }
            startN += step;
        } while (abs(ans1 - ans2) > eps);
        return startN;
    }*/

    /*private int calcNx(double errT){
        int startN = 2;
        ArrayList<Double> ans1;// = new ArrayList<>();
        double vx0;
        double step;
        do {
            step = length/startN;
            ans1 = new ArrayList<>();
            for (int j = 0; j <= startN; j++) {
                vx0 = 0;
                for (int i = 1; i <= nSum; i++) {
                    vx0 += ant(i, 0) * decompositionOfOne(i, startN*step);
                }
                ans1.add(vx0);
            }

            for (int j = 0; j < startN; j++) {
                vx0 = 0;
                for (int i = 1; i <= nSum; i++) {
                    vx0 += ant(i, 0) * decompositionOfOne(i, startN*step);
                }
                ans1.add(vx0);
            }

            ans2 = 0;
            for(int i = 1; i <= startN + step; i++) {
                ans1 += ant(i, 0) * decompositionOfOne(i, length/2*(startN + step));
            }
        } while (abs(ans1 - ans2) > errT);
        return startN;
    }*/

}
