package Objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Myles on 8/6/17.
 */
public class HostManager {

    private TreeMap<String, Host> hosts = new TreeMap<>();

    public void addHost(Host host)
    {
        this.hosts.put(host.getUsername(), host);
    }

    public TreeMap<String, Host> getHostsFromServer() throws IOException {
        FTP ftp = new FTP("files.000webhost.com", 21);
        ftp.login("studentemail", "megamacman11");
        ftp.connect();
        boolean success = ftp.downloadFile("hosts.hts");
        ObjectIO objectIO = new ObjectIO();
        File hostFile = new File(objectIO.getApplicationDataFolder() + File.separator + "hosts.hts");
        if(hostFile.exists()) {
            objectIO.setObjectFile(hostFile);
            this.hosts = (TreeMap<String, Host>) objectIO.readObject();
        }
        if(!success) {
            System.out.println("Could not find file online");
        }
        return this.hosts;
    }

    public void updateHosts(Host host) throws IOException {
        getHostsFromServer();
        addHost(host);
        ObjectIO objectIO = new ObjectIO(new File("hosts.hts"));
        objectIO.writeObject(this.hosts);
        try {
            FTP ftp = new FTP();
            ftp.connect();
            ftp.uploadFile("hosts.hts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfHostFileExists()
    {
        return new File(ObjectIO.getApplicationDataFolder() + File.separator + "hosts.hts").exists();
    }

    public boolean checkIfHostsExistOnServer() throws IOException {
        FTP ftp = new FTP("files.000webhost.com", 21);
        ftp.login("studentemail", "megamacman11");
        ftp.connect();
        return ftp.checkIfFileExistsOnServer("hosts.hts");
    }

    public boolean checkIfLocalIpHasChanged(){
        String localIPAddress = new Host().getComputerIpAddress();
        ObjectIO localHostObjectFile = new ObjectIO(new File("hosts.hts"));
        TreeMap<String, Host> hosts = (TreeMap<String, Host>) localHostObjectFile.readObject();
        Host localHost = getLocalHostFromHostArrayList(new ArrayList<>(hosts.values()));
        boolean ipAddressChanged = false;
        if(!localHost.getIpAddress().equals(localIPAddress) && localHost != null)
        {
            ipAddressChanged = true;
        }
        return ipAddressChanged;
    }

    public String getIpByUsername(String username) throws IOException {
        getHostsFromServer();
        return this.hosts.get(username).getComputerIpAddress();
    }

    public Host getLocalHostFromHostArrayList(ArrayList<Host> hostList)
    {
        for (int i = 0; i < hostList.size(); i++) {
            if(hostList.get(i).getComputerName().equals(new Host().getComputerName()));
            {
                return hostList.get(i);
            }
        }
        return null;
    }

    public boolean checkIfLocalHostIsCreated()  {
        boolean localHostExists = false;
        try {
            boolean server = checkIfHostsExistOnServer();
            boolean local = checkIfHostFileExists();
            if (server || local) {
                localHostExists = true;
            }
            if(local)
            {
                Host localHost = getLocalHostFromHostArrayList((ArrayList<Host>) getHostsFromServer().values());
                if(localHost == null)
                {
                    localHostExists = true;
                }
            }
            else if(server)
            {

            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return localHostExists;
    }

    public static void main(String[] args) throws IOException {
        HostManager hostManager = new HostManager();
        System.out.print(hostManager.checkIfLocalHostIsCreated());
    }
}
