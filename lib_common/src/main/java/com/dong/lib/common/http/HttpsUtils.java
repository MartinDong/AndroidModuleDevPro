package com.dong.lib.common.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * ================================================
 * https://github.com/hongyangAndroid/okhttputils
 * Copy from OkHttpUtils
 * <p>
 * Created by xiaoyulaoshi on 2018/3/23.
 * <p>
 * ================================================
 */

public class HttpsUtils {
    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    /**
     * 获得SSL参数
     *
     * @param certificates 证书
     * @param bksFile      bks证书
     * @param password     密码
     * @return SSL参数
     */
    public static SSLParams getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        //SSL参数
        SSLParams sslParams = new SSLParams();
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //初始化X509证书管理器
            X509TrustManager trustManager = null;

            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);

            //通过HttpsURLConnection设置链接
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 不安全的验证管理器
     */
    private class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 不安全的信任管理器
     */
    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    /**
     * 构建证书管理器
     *
     * @param certificates 证书
     * @return 证书管理器
     */
    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //使用默认证书
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //去掉系统默认证书
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                //设置自己的证书
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            //通过信任管理器获取一个默认的算法
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            //算法工厂创建
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
            trustManagerFactory.init(keyStore);

            //通过证书管理器工厂获取证书管理器
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 构建密钥管理器
     *
     * @param bksFile  bks文件
     * @param password 密码
     * @return 密钥管理器
     */
    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");

            //加载证书
            clientKeyStore.load(bksFile, password.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());

            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选择信任管理器
     *
     * @param trustManagers 证书管理器集合
     * @return 返回继承X509TrustManager的证书管理器
     */
    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }


    /**
     * 自定义管理了X509证书
     */
    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * PKCS12转BKS
     *
     * @param pkcs12Stream   pkcs12证书文件
     * @param pkcs12Password pkcs12证书密码
     * @return
     */
    public static InputStream pkcs12ToBks(InputStream pkcs12Stream, String pkcs12Password) {
        final char[] password = pkcs12Password.toCharArray();
        try {
            KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
            pkcs12.load(pkcs12Stream, password);
            Enumeration<String> aliases = pkcs12.aliases();
            String alias;
            if (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
            } else {
                throw new Exception("pkcs12 file not contain a alias");
            }
            Certificate certificate = pkcs12.getCertificate(alias);
            final Key key = pkcs12.getKey(alias, password);
            KeyStore bks = KeyStore.getInstance("BKS");
            bks.load(null, password);
            bks.setKeyEntry(alias, key, password, new Certificate[]{certificate});
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bks.store(out, password);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SSLSocketFactory getSocketFactory(InputStream stream) {
        SSLSocketFactory socketFactory = null;
        try {
            //获取证书
            SSLContext tls = SSLContext.getInstance("TLS");
            //使用默认证书
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            //去掉系统默认证书
            keystore.load(null);
            Certificate certificate =
                    CertificateFactory.getInstance("X.509").generateCertificate(stream);
            //设置自己的证书
            keystore.setCertificateEntry("skxy", certificate);
            //通过信任管理器获取一个默认的算法
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            //算法工厂创建
            TrustManagerFactory instance = TrustManagerFactory.getInstance(algorithm);
            instance.init(keystore);
            tls.init(null, instance.getTrustManagers(), null);
            socketFactory = tls.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return socketFactory;
    }
}
