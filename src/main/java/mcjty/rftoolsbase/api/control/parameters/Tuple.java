package mcjty.rftoolsbase.api.control.parameters;

public record Tuple(int x, int y) implements Comparable<Tuple> {

    @Override
    public int compareTo(Tuple tuple) {
        if (x < tuple.x) {
            return -1;
        } else if (x > tuple.x) {
            return 1;
        } else {
            if (y < tuple.y) {
                return -1;
            } else if (y > tuple.y) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
