import java.io.Serializable;
import java.util.function.Consumer;

public class ConsumerUI implements Consumer, Serializable {
    @Override
    public void accept(Object o) {
        System.out.println(o.toString());
    }
}
