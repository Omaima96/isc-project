package com.isc.authentication.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@PropertySource("classpath:/application.yml")
public class KeyUtils {

    @Value("${jwt.key.public}")
    private String publicKeyString;

    public KeyPair getKeyPair() {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            File file = ResourceUtils.getFile("classpath:certificate/keystore.jks");
            InputStream inputStream = new FileInputStream(file);
            ks.load(inputStream, "keypassword".toCharArray());
            Certificate certificate = ks.getCertificate("keylabel");
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) ks.getKey("keylabel", "keypassword".toCharArray());
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            return null;
        }
    }

    public PublicKey getPublicKey() {
        try {
            byte[] publicKeyByteServer = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
            return publicKey;
        } catch (Exception ex) {
            return null;
        }
    }

}
