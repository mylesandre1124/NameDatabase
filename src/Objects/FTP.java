package Objects;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;

/**
 * Created by Myles on 8/6/17.
 */
public class FTP {

    private FTPClient client = new FTPClient();
    private String hostname;
    private int port;
    private String username;
    private String password;

    public FTP(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public FTP() {
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login(String username, String password)
    {
        setUsername(username);
        setPassword(password);
    }

    public void connect() throws IOException {
        client.connect(hostname, port);
        client.login(username, password);
        client.changeWorkingDirectory("public_html");
    }

    public boolean uploadFile(String file)
    {
        boolean success = false;
        try {
            ObjectIO objectIO = new ObjectIO(new File(file));
            InputStream out = new FileInputStream(objectIO.getObjectFile());
            success = client.storeFile(file, out);
            if(success)
            {
                System.out.println("Upload Complete");
            }
            client.disconnect();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean downloadFile(String file)
    {
        boolean success = false;
        try {
            if(checkIfFileExistsOnServer(file)) {
                ObjectIO objectIO = new ObjectIO(new File(file));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(objectIO.getObjectFile()));
                success = client.retrieveFile(file, out);
                out.close();
            }
            if (success)
            {
                System.out.println("Download Complete");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean checkIfFileExistsOnServer(String fileName) throws IOException {
        FTPFile[] filesInFTPServerFolder = client.listFiles();
        for (int i = 0; i < filesInFTPServerFolder.length; i++) {
            if(filesInFTPServerFolder[i].getName().equals(fileName))
            {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        FTP ftp = new FTP("files.000webhost.com", 21);
        ftp.login("studentemail", "megamacman11");
        ftp.connect();
        ftp.downloadFile("students.sts");
    }

}
