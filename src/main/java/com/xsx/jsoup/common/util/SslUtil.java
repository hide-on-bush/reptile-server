package com.xsx.jsoup.common.util;

//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//
//import javax.net.ssl.*;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;

import lombok.extern.slf4j.Slf4j;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Author:夏世雄
 * @Date: 2022/07/20/17:17
 * @Version: 1.0
 * @E-mail: xiashixiongtoxx@163.com
 * @Discription:
 **/
@Slf4j
public class SslUtil {

//    public static void ignoreSsl() {
//        HostnameVerifier hv = new HostnameVerifier() {
//            @Override
//            public boolean verify(String urlHostName, SSLSession session) {
//                return true;
//            }
//        };
//        trustAllHttpsCertificates();
//        HttpsURLConnection.setDefaultHostnameVerifier(hv);
//    }
//
//    private static void trustAllHttpsCertificates() {
//        TrustManager[] trustAllCerts = new TrustManager[] {
//                new X509TrustManager() {
//                    @Override
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//                    @Override
//                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
//
//                    @Override
//                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
//                }
//        };
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (Exception e) {
//            log.error(e.getMessage() , e);
//        }
//
//    }

//    private static void trustAllHttpsCertificates() throws Exception {
//        TrustManager[] trustAllCerts = new TrustManager[1];
//        TrustManager tm = new miTM();
//        trustAllCerts[0] = tm;
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, null);
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//    }
//
//    static class miTM implements TrustManager,X509TrustManager {
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//
//        public boolean isServerTrusted(X509Certificate[] certs) {
//            return true;
//        }
//
//        public boolean isClientTrusted(X509Certificate[] certs) {
//            return true;
//        }
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
//            return;
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//            return;
//        }
//
//
//    }
//
//    /**
//     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
//     * @throws Exception
//     */
//    public static void ignoreSsl() throws Exception{
//        HostnameVerifier hv = new HostnameVerifier() {
//            @Override
//            public boolean verify(String urlHostName, SSLSession session) {
//                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
//                return true;
//            }
//        };
//        trustAllHttpsCertificates();
//        HttpsURLConnection.setDefaultHostnameVerifier(hv);
//    }

    public static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     *
     * @throws Exception
     */
    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

}
