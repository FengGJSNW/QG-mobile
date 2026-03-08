import java.util.ArrayList;
import java.util.Collection;

public class program {
    public static void main(String[] args) {
        
    }
}


// =================== Cat  ========================= //
abstract class Cat {
    private int id;
    private Boolean status;
    private Boolean inBlackList;
    private String name;
    private Collection moreLiked = new ArrayList<String>();

    public abstract void Cat(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public abstract void eat();
} 

class OrangeCat extends Cat {
    
}


// =================== Food ======================= //
abstract class Food {
    private String name;
    private Collection allLiked = new ArrayList<String>();
    
    
}

class screen {

}