import java.util.stream.IntStream;

class Bar {
    private Object[] p = IntStream.range(0, 100 * 1000).mapToObj(i -> new Object()).toArray();
}

class Foo {
    private Object[] p = IntStream.range(0, 10).mapToObj(i -> new Object()).toArray();
    private Bar bar = new Bar();
}

public class Hello {
    public static void main(String[] args) throws Exception {
        Foo foo = new Foo();

        long pid = ProcessHandle.current().pid();

        System.out.printf("I'll be sleeeping for 100 seconds");
        System.out.printf("\n\njcmd %d GC.heap_dump /tmp/foobar2.hprof\n", pid);

        Thread.sleep(100 * 1000);

        System.out.println(foo);
    }
}
