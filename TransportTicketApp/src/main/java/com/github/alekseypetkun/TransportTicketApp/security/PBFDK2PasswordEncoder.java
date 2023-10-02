package com.github.alekseypetkun.TransportTicketApp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Способ кодирования паролей
 */
@Component
public class PBFDK2PasswordEncoder implements PasswordEncoder {

    @Value("${jwt.password.encoder.secret}")
    private String secret; // Параметр, на основании которого будет кодирование
    @Value("${jwt.password.encoder.iteration}")
    private Integer iteration; // Кол-во итераций энкодинга
    @Value("${jwt.password.encoder.keylength}")
    private Integer keyLength; // Длина ключа

    /**
     * Сущность для генерации пароля
     */
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

    /**
     * Кодируем входящий пароль
     *
     * @param rawPassword входящий пароль
     * @return результат кодирования в виде строки
     */
    @Override
    public String encode(CharSequence rawPassword) {

        try {
            byte[] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(), iteration, keyLength))
                    .getEncoded();
            return Base64.getEncoder()
                    .encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка входящего пароля с уже сохраненным и закодированным паролем в БД
     *
     * @param rawPassword     необработанный пароль для кодирования и сопоставления
     * @param encodedPassword закодированный пароль из хранилища для сравнения
     * @return true or false
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return encode(rawPassword).equals(encodedPassword);
    }
}
