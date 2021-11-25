import java.util.function.Consumer;

public class ConsumerUI implements Consumer{
    @Override
    public void accept(Object o) {
        System.out.println(o.toString());
    }
}
