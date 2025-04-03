import java.util.Arrays;

public class ArgDebug {
    public static void main(String[] args) {
        System.out.println("args.length = " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Arg[" + i + "] = '" + args[i] + "'");
        }
        System.out.println("Full args: " + Arrays.toString(args));
    }
}
