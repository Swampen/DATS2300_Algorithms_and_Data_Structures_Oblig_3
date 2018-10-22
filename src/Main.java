import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        ObligSBinTre<Character> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        char[] verdier = "IATBHJCRSOFELKGDMPQN".toCharArray();
        for (char c : verdier) tre.leggInn(c);

        String[] s = tre.grener();
        for (String gren : s) System.out.println(gren);

    }
}
