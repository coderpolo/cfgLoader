package com.test.cfgLoader.util;

import com.google.gson.Gson;
import com.test.cfgLoader.entity.Command;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Created by kaisheng1 on 2017/12/13.
 */
@Component
@Data
public class ScheduledTasks
{
    private String userPath;
    Runtime runtime;
    int version;
    Command lastCommand;

    public ScheduledTasks() {
        this.userPath = FileUtils.getUserDirectoryPath();
        this.runtime = Runtime.getRuntime();
        this.lastCommand = new Command();
    }

    //从txt中取出数据
    public String getData(String fileName)
    {
        File localFile = new File(fileName);
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(localFile,"GBK");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return fileContent;
    }


    //获取远程数据
    public void syncFolder()
    {
        String server = "";
        int port = 21;
        String user = "";
        String pass = "";

        FTPClient ftpClient = new FTPClient();

        try {
            // connect and login to the server
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            System.out.println("Connected");

            String remoteDirPath = "/data/test";
            String saveDirath = userPath;

            File file = new File(userPath + remoteDirPath);

            boolean res = false;
            if (!file.exists())
            {
                res = file.mkdirs();
            }
            else
            {
                //清空文件夹
                FileUtils.cleanDirectory(file);
            }
            MFtp.downloadDirectory(ftpClient, remoteDirPath, "", saveDirath);

            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("Disconnected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public Command ParseCmd()
    {
        Gson gson = new Gson();

        String json = getData(userPath + File.separator + "data" + File.separator + "test" +File.separator+ "command.json");

        json=json.trim();

        return gson.fromJson(json,Command.class);
    }

    public void ExecuteCmd() throws IOException, InterruptedException {

        Command currentCommand = ParseCmd();

        if (lastCommand.getVersion() >=  currentCommand.getVersion())
        {
            System.out.println("版本过低");
            return;
        }
        else
        {
            lastCommand = currentCommand;
        }

        String killStr = "";

        if (isWindows())
        {
            killStr = "taskkill /F /IM " + currentCommand.getProcess();
        }
        else
        {
            killStr = "pkill " + currentCommand.getProcess();
        }

        runtime.exec(killStr);
        System.out.println(currentCommand.getProcess() + "被杀掉");
        //执行指令
        runtime.exec(currentCommand.getScript());
        System.out.println(currentCommand.getScript() + "启动");
    }

    private boolean isWindows()
    {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows"))
        {
            return true;
        }
        return false;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void runTask() throws IOException, InterruptedException {
        syncFolder();
        ExecuteCmd();
    }

}
