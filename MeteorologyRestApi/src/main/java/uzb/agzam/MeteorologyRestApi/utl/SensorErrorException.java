package uzb.agzam.MeteorologyRestApi.utl;

public class SensorErrorException extends RuntimeException {
    public SensorErrorException(String msg){
        super(msg);
    }
}
