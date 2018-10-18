import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.postString()); // [10, 9, 8, 7, 7, 6, 4, 4, 4, 2, 1]

        System.out.println(Arrays.toString(tre.grener()));

    }
}
