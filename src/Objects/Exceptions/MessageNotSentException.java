package Objects.Exceptions;

/**
 * Created by Myles on 7/31/17.
 */
public class MessageNotSentException extends Throwable {

    public MessageNotSentException() {
        super("Message was not able to be sent");
    }
}
