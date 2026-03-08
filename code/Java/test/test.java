import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class test {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = User.class;
        Object user = clazz.getDeclaredConstructor().newInstance();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(user, "Bob");

        Method sayHi = clazz.getDeclaredMethod("sayHi");
        sayHi.setAccessible(true);
        sayHi.invoke(user);
    }
}

class User {
    private String name = "Alice";

    private void sayHi() {
        System.out.println("Hi " + name);
    }

    public void sayNo() {
        System.out.println("No");
    }
}
