import java.util.*;
import java.io.*;


class CPointComparator implements Comparator {
    public int compare(Object a, Object b) {

        double thetaY = ((CPoint)b).theta;
        double thetaX = ((CPoint)a).theta;


        if (thetaX < thetaY) return -1;
        if (thetaX > thetaY) return 1;

        return 0;
    }
}

class CPoint {

    public int d;
    public double theta;
    public int x;
    public int y;

    CPoint(int x, int y, int d) {

        this.d = d;
        this.x = x;
        this.y = y;
        theta = Math.atan2(y, x);
    }

    int r2() {
        return ((x * x) + (y * y));
    }


    boolean need_sub() {
        return this.r2() > (d * d);
    }


    boolean overlaps(CPoint o) {
        if (!this.need_sub() && !o.need_sub()) {
            return true;
        }

        int left_temp = (x * o.x) + (y * o.y) + (d * d);

        if (left_temp < 0) {
            return false;
        }

        int left_side = (left_temp * left_temp);
        int right_side = (this.r2() - (d * d)) * (o.r2() - (d*d));

        return left_side >= right_side;
    }
}

class SubSys {
    int[] a;
    int[] result;
    int n;

    boolean[][] overlaps;
    CPoint[] p;



    SubSys(int dim) {
        n = 0;
        a = new int[dim];
        result = new int[dim];
        p = new CPoint[dim];
        overlaps = new boolean[dim][dim];
    }



    void reset() {
        for (int i = 0; i < n; i++) {
            a[i] = -1;
        }
    }

    void add_p(CPoint point) {
        p[n++] = point;
    }

    void create_overlaps() {
        for (int i = 0; i < n; i++) {
            boolean overlapping = true;

            overlaps[i][i] = true;

            for (int j = (i + n - 1) % n; j != i; j = (j + n - 1) % n) {
                if (overlapping) {
                    overlapping = p[j].overlaps(p[i]);
                }

                overlaps[j][i] = overlapping;
            }
        }
    }

    int min_subs() {
        if (n == 0) {
            return 0;
        }

        int best = -1;

        Arrays.sort(p, 0, n, new CPointComparator());

        this.create_overlaps();

        for (int i = 0; i < n; i++) {
            this.reset();

            int neccessary_lines = this.get_lines(i,i);
            if (neccessary_lines < best || best == -1) {
                best = neccessary_lines;
            }
        }

        return best;
    }

    int get_lines(int starter, int current) {
        if (a[current] >= 0) {
            return a[current];
        }

        int i = current;
        int best, lastIncluded, candidate;

        if ((i + 1) % n == starter) {
            best = 1;
            lastIncluded = current;
        } else {
            lastIncluded = i;
            i = (i + 1) % n;
            best = this.get_lines(starter, i) + 1;

            while ((i != starter) && (overlaps[current][i])) {
                if ((i + 1) % n == starter) {
                    best = 1;
                    lastIncluded = i;

                    break;
                } else {
                    candidate = this.get_lines(starter, (i + 1) % n) + 1;
                    if (candidate < best) {
                        best = candidate;
                        lastIncluded = i;
                    }

                    i=(i + 1) % n;
                }
            }
        }

        a[current] = best;
        result[current] = (lastIncluded + 1) % n;

        return best;
    }


}

public class subwayplanning {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int N = s.nextInt();

        for (int i = 0; i < N; i++) {
            int n = s.nextInt();
            int d = s.nextInt();

            SubSys subway = new SubSys(n);

            for (int j = 0; j < n; j++) {
                CPoint possible_point = new CPoint(s.nextInt(), s.nextInt(), d);

                if (possible_point.need_sub()) {
                    subway.add_p(possible_point);
                }
            }

            System.out.println(subway.min_subs());
        }

        s.close();
    }
}