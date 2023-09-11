import java.util.stream.IntStream;
import java.lang.ProcessHandle;

class Bar2 {
    private int[] p = new int[8000000];
}

class Foo2 {
    private int[] p = new int[1000000];
    private Bar2 bar = new Bar2();
}

public class Hi {
    public static void main(String[] args) throws Exception {
        Foo2 foo = new Foo2();

        long pid = ProcessHandle.current().pid();

        System.out.printf("I'll be sleeeping for 100 seconds");
        System.out.printf("\n\njcmd %d GC.heap_dump /tmp/Hi.hprof\n", pid);

        Thread.sleep(100 * 1000);

        System.out.println(foo);
    }
}
