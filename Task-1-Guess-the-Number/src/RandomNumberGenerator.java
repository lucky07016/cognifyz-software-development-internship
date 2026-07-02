import java.util.Random;
public class RandomNumberGenerator {

    private final int min;
    private final int max;
    private final Random random;

    public RandomNumberGenerator(int min, int max) {
        this.min = min;
        this.max = max;
        this.random = new Random();
    }
    public int generate() {
        return random.nextInt((max - min) + 1) + min;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}