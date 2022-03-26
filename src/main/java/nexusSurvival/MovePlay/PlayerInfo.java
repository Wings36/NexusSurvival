package nexusSurvival.MovePlay;

public class PlayerInfo {
    public double height;
    public double rateAscent;
    public PlayerInfo (double height) {
        this.height = height;
        rateAscent= Math.abs((256.0 - height) / 30);
    }

}
