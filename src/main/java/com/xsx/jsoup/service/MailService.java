package com.xsx.jsoup.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

/**
 * @Author:夏世雄
 * @Date: 2022/09/05/11:22
 * @Version: 1.0
 * @Discription:
 **/
@Service
@Slf4j
public class MailService {


    public static void main(String[] args) throws Exception{
       // emailDemo();
        recipientMail();
    }


    public static String receive() throws Exception{
        // 准备连接服务器的会话信息
        Properties prop = new Properties();
        prop.setProperty("mail.debug", "true");
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", "pop.proton.me");
        // 1、创建session
        Session session = Session.getInstance(prop);
        // 2、通过session得到Store对象
        Store store = session.getStore();
        // 3、连上邮件服务器
        store.connect("pop.proton.me", "hyper_bush@proton.me", "xsx123456");
        // 4、获得邮箱内的邮件夹
        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        // 获得邮件夹Folder内的所有邮件Message对象
        Message[] messages = folder.getMessages();
        for (int i = 0; i < messages.length; i++) {
            String subject = messages[i].getSubject();
            String from = (messages[i].getFrom()[0]).toString();
            System.out.println("第 " + (i + 1) + "封邮件的主题：" + subject);
            System.out.println("第 " + (i + 1) + "封邮件的发件人地址：" + from);
        }
        // 5、关闭
        folder.close(false);
        store.close();
        return  null;
    }


    public static void receive1() throws Exception {



        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

// 取得pop3协议的邮件服务器

        Store store = session.getStore("pop3");

// 连接pop.163.com邮件服务器

        store.connect("pop.proton.me.com", "hyper_bush@proton.me", "xsx123456");

// 返回文件夹对象

        Folder folder = store.getFolder("INBOX");

// 设置仅读

        folder.open(Folder.READ_ONLY);

// 获取信息

        Message message[] = folder.getMessages();



      folder.close(true);

     store.close();

    }



    //public static void emailDemmo(String protocol, String port, String host, String user, String psw) {
    public static void emailDemo(){
        Properties props = new Properties();
        //pop3
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.host", "imap.gmail.com");
//        props.setProperty("mail.store.protocol", "smtp");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.host", "smtp.proton.me");
        Folder folder = null;
        Store store = null;
        try {
            // 创建Session实例对象
            Session session = Session.getInstance(props);
            store = session.getStore();
            store.connect("hyperbush123@gmail.com", "xsx123456");

            // 获得收件箱
            folder = store.getFolder("INBOX");
            //打开收件箱
            folder.open(Folder.READ_WRITE);
            log.debug("收件箱已打开");

            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                //逐个处理邮件
            }
        } catch (MessagingException  e) {
            log.error("初始化邮箱失败！！！", e);
        } finally {
            try {
                if (folder != null) {
                    folder.close(true);
                }
            } catch (MessagingException e) {
                log.error("文件目录关闭失败！！！", e);
            }
            try {
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                log.error("邮箱连接关闭失败！！！", e);
            }
        }
    }



    public static void test1() throws Exception{

    }



    public static Properties getProperties() {
        Properties properties = new Properties();
        // 默认的邮件传输协议
        properties.setProperty("mail.transport.protocol", "imap");
        // 默认的存储邮件协议
        properties.setProperty("mail.store.protocol", "imap");
        // 设置邮件服务器主机名
        properties.put("mail.host", "imap-mail.outlook.com");
        properties.put("mail.port", "993");
        // 设置是否安全验证,默认为false,一般情况都设置为true
        properties.put("mail.imap.auth", false);
        return properties;
    }


    public static boolean recipientMail() {
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session emailSession = Session.getDefaultInstance(getProperties());

        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        emailSession.setDebug(false);
        try {

            Store emailStore = emailSession.getStore("imaps");
            emailStore.connect("hyperbush123@outlook.com", "xsx211027");

            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            System.out.println("未读邮件数: " + emailFolder.getUnreadMessageCount());

            // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
            System.out.println("删除邮件数: " + emailFolder.getDeletedMessageCount());
            System.out.println("新邮件: " + emailFolder.getNewMessageCount());

            // 获得收件箱中的邮件总数
            System.out.println("邮件总数: " + emailFolder.getMessageCount());

            // 获取收件箱中的所有邮件并解析
            Message[] messages = emailFolder.getMessages();
            for (Message message:messages) {
                System.out.println("------------------解析第" + message.getMessageNumber() + "封邮件-------------------- ");
                System.out.println("Email Number " + message.getMessageNumber());
                System.out.println("主题: " + MimeUtility.decodeText(message.getSubject()));
                System.out.println("发件人: " + InternetAddress.toString(message.getFrom()));
                System.out.println("邮件正文: " + message.getContent().toString());
                //System.out.println("接收时间："+new DateTime(message.getReceivedDate()).toString("yyyy-MM-dd HH:mm:ss"));
                //System.out.println("发送时间："+new DateTime(message.getSentDate()).toString("yyyy-MM-dd HH:mm:ss"));
                System.out.println("是否已读：" + message.getFlags().contains(Flags.Flag.SEEN));
                String[] headers = message.getHeader("X-Priority");
                if(ArrayUtils.isNotEmpty(headers)){
                    System.out.println("邮件优先级：" + headers[0]);
                }
                String[] replySign = message.getHeader("Disposition-Notification-To");
                System.out.println("是否需要回执：" + ArrayUtils.isNotEmpty(replySign));
            }
            //释放资源
            emailFolder.close(false);
            emailStore.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }


        return false;
    }



}


