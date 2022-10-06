package com.example.service;




import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Jasypt用于在配置文件中写加密后的密码，只要配上秘钥，读入系统中的就是解密后的正确密码。
 * 这个工具类主要是用于实验Jasypt的加密和解密.
 * 把yml中对Jasypt的配置写到代码里而已。
 *
 * 实际使用Jasypt时，只需pom引入依赖，yml中配置相关项（秘钥等），然后把加密后的密码写入你需要配置的地方（yml文件。。。）
 * 程序启动后，会自动解密（如果程序不能正常解密，那你的系统启动就有问题了，比如数据库连不上，redis连不上等，都是密码不正确的错）
 * 我们也可以写个controller把yml配置文件中的密码打印出来，这个打印的肯定不是你写的加密的字符串而是解密后的正确密码。
 * 整个解密的过程是交给Jasypt做的。加密的过程是我们提前加密得到密文，写到yml配置文件中的。但是必须有ENC()这个标识。
 *
 *
 * @author lsy
 *
 */
public class JasyptUtil   {

public static void main(final String[] args) {

        String miyao = "9nXn(D7NeF";// 秘钥字符串
        String pass = "DZqsRin_cdS2h+)g%U";// 待加密的明文密码
        try {
        StringEncryptor stringEncryptor = JasyptUtil.getInstance(miyao);
        String mima = stringEncryptor.encrypt(pass);
        System.out.println("【" + pass + "】被加密成【" + mima + "】");

        String jiemi = stringEncryptor.decrypt(mima);
        System.out.println("【" + mima + "】被解密成【" + jiemi + "】");

        } catch (Exception e) {
        e.printStackTrace();
        }
        }

private static StringEncryptor stringEncryptor = null;//org.jasypt.encryption.StringEncryptor对象

public static StringEncryptor getInstance(String miyao) throws Exception {
        if(miyao==null||miyao.trim().equals("")) {
        System.out.println("秘钥不能为空！");
        throw new Exception("org.jasypt.encryption.StringEncryptor秘钥不能为空！");
        }
        if (stringEncryptor == null) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("9nXn(D7NeF");// 这个秘钥必须是我们自己定义
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        stringEncryptor = encryptor;
        }
        return stringEncryptor;
        }

        }