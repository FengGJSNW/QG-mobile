package item;

public class test {
    public static void main(String[] args) {
        String numStringList[] = {"34", "45", "56"};
        int ans = 0;
        for(String x : numStringList) {
            ans += Integer.parseInt(x);
        }
        System.out.println(ans);
    }
}