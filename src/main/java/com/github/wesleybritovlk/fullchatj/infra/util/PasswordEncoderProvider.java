package com.github.wesleybritovlk.fullchatj.infra.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public interface PasswordEncoderProvider {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

    static PasswordEncoderProvider of(EncoderType encoderType) {
        return switch (encoderType) {
            case BCRYPT -> new BCryptPasswordEncoderProvider();
            case SCRYPT -> new SCryptPasswordEncoderProvider();
        };
    }

    enum EncoderType {
        BCRYPT, SCRYPT
    }
}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SCryptPasswordEncoderProvider implements PasswordEncoderProvider {
    private static final int DEFAULT_CPU_COST = 65536;
    private static final int DEFAULT_MEMORY_COST = 8;
    private static final int DEFAULT_PARALLELISM = 1;
    private static final int DEFAULT_KEY_LENGTH = 32;
    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        var salt = new byte[DEFAULT_SALT_LENGTH];
        RANDOM.nextBytes(salt);
        var generatedHash = getHash(rawPassword, salt);
        return "%s$%s".formatted(Hex.toHexString(salt), Hex.toHexString(generatedHash));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var parts = encodedPassword.split("\\$");
        var salt = Hex.decode(parts[0]);
        var storedHashBytes = Hex.decode(parts[1]);
        var computedHash = this.getHash(rawPassword, salt);
        return Arrays.equals(storedHashBytes, computedHash);
    }

    private byte[] getHash(CharSequence rawPassword, byte[] salt) {
        var passwordBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        return SCrypt.generate(passwordBytes, salt, DEFAULT_CPU_COST, DEFAULT_MEMORY_COST,
                DEFAULT_PARALLELISM, DEFAULT_KEY_LENGTH);
    }
}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
class BCryptPasswordEncoderProvider implements PasswordEncoderProvider {
    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final int DEFAULT_COST = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        var salt = new byte[DEFAULT_SALT_LENGTH];
        RANDOM.nextBytes(salt);
        var generatedHash = getHash(rawPassword, salt);
        return "%s$%s".formatted(Hex.toHexString(salt), Hex.toHexString(generatedHash));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var parts = encodedPassword.split("\\$");
        var salt = Hex.decode(parts[0]);
        var storedHashBytes = Hex.decode(parts[1]);
        var computedHash = this.getHash(rawPassword, salt);
        return Arrays.equals(storedHashBytes, computedHash);
    }

    private byte[] getHash(CharSequence rawPassword, byte[] salt) {
        var passwordBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        return BCrypt.generate(passwordBytes, salt, DEFAULT_COST);
    }

}
