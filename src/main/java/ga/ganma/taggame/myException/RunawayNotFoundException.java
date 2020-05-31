package ga.ganma.taggame.taggame.myException;

public class RunawayNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public RunawayNotFoundException(){
        super("ゲーム開始しましたが、逃げる人が一人もいません！");
    }
}
