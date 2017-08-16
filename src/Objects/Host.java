package Objects;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Myles on 8/6/17.
 */
public class Host implements Serializable {

    private String ipAddress;
    private String username;

    public Host() {
        getComputerIpAddress();
    }

    public String getComputerIpAddress()
    {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();
            this.ipAddress = ipAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return this.ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
