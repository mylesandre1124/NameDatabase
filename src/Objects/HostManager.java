package Objects;

import java.io.File;
import java.io.IOException;
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
        File hostfile = new File(objectIO.getApplicationDataFolder() + "hosts.hts");
        if(hostfile.exists()) {
            objectIO.setObjectFile(hostfile);
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

    public boolean checkIfLocalHostExists()
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
        if(checkIfLocalHostExists())
        {
            ObjectIO hostsFile = new ObjectIO(new File("hosts.hts"));
            TreeMap<String, Host> hosts = (TreeMap<String, Host>) hostsFile.readObject();
            //hosts.get();
        }
        return true;
    }

    public String getIpByUsername(String username) throws IOException {
        getHosts();
        return this.hosts.get(username).getComputerIpAddress();
    }

    public static void main(String[] args) throws IOException {
        HostManager hostManager = new HostManager();
        /*Host host = new Host();
        host.setUsername("Hello");
        hostManager.updateHosts(host);
        ArrayList<Host> hosts1 = new ArrayList<>(hostManager.getHosts().values());
        for (int i = 0; i < hosts1.size(); i++) {
            System.out.println(hosts1.get(i).getComputerIpAddress());
        }*/

        //hostManager.getHosts();
    }
}
