package Objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
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

    public TreeMap<String, Host> getHosts() throws IOException {
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
        getHosts();
        addHost(host);
        ObjectIO objectIO = new ObjectIO(new File("hosts.hts"));
        objectIO.writeObject(this.hosts);
        try {
            FTP ftp = new FTP();
            //ftp.login();
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
        if(checkIfHostFileExists())
        {
            ObjectIO localHostObjectFile = new ObjectIO(new File("hosts.hts"));
            TreeMap<String, Host> hosts = (TreeMap<String, Host>) localHostObjectFile.readObject();
            Host localHost = getLocalHostFromHostArrayList(new ArrayList<>(hosts.values()));
            if(localHost.getIpAddress().equals(localIPAddress))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        else {
            return false;
        }
    }

    public String getIpByUsername(String username) throws IOException {
        getHosts();
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


    public static void main(String[] args) throws IOException {
        //HostManager hostManager = new HostManager();
        /*Host host = new Host();
        host.setUsername("Hello");
        hostManager.updateHosts(host);
        ArrayList<Host> hosts1 = new ArrayList<>(hostManager.getHosts().values());
        for (int i = 0; i < hosts1.size(); i++) {
            System.out.println(hosts1.get(i).getComputerIpAddress());
        }*/

        //hostManager.getHosts();
        Properties props = System.getProperties();
        System.out.println(props.get("user.name"));
    }
}
