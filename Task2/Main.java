import java.io.IOException;

public class Main {
    public static void main(String[] args){
        try {
            Parse p = new Parse(args[0], args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
