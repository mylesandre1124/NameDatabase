package Objects;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * Created by Myles on 8/6/17.
 */
public class Host implements Serializable {

    private String ipAddress;
    private String computerName;
    private String username;

    public Host() {
        getComputerIpAddress();
        getComputerName();
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

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getComputerName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("win"))
        {
            this.computerName = System.getenv("HOSTNAME");
        }
        else if(osName.startsWith("mac"))
        {
            this.computerName = System.getProperty("user.name");
        }
        return this.computerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public static void main(String[] args) throws IOException {

    }
}
