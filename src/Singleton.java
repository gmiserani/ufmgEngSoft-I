public class Singleton {

    private static Singleton uniqueInstance;
    
    private Singleton(){}


    public synchronized static Singleton getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }
    return uniqueInstance;
    }
}