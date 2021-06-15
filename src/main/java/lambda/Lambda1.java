package lambda;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Lambda1 implements RequestHandler<Map<String, String>, String> {
    private final String clearText = "Test for Encrypt text by RSA_4096";
    private final byte[] EXAMPLE_DATA = clearText.getBytes(StandardCharsets.UTF_8);

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("This is Encrypt text:");
        logger.log(clearText);
        logger.log("After convert to Byte:" + Arrays.toString(EXAMPLE_DATA));
        String KEY_ARN = "arn:aws:kms:ap-southeast-2:160071257600:key/8d468822-9303-4150-bfaa-5be96d164449";
        encryptAndDecrypt(KEY_ARN, logger);
        return "";
    }
    public void encryptAndDecrypt(final String keyArn, LambdaLogger logger) {
        // 1. Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();
        logger.log("1. Instantiate the SDK");

        // 2. Instantiate a KMS master key provider
        final KmsMasterKeyProvider masterKeyProvider = KmsMasterKeyProvider.builder().withKeysForEncryption(keyArn).build();
        logger.log("2. Instantiate a KMS master key provider");

        // 3. Create an encryption context
        //
        // Most encrypted data should have an associated encryption context
        // to protect integrity. This sample uses placeholder values.
        //
        // For more information see:
        // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
        final Map<String, String> encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");
        logger.log("3. Create an encryption context");

        // 4. Encrypt the data
        final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(masterKeyProvider, EXAMPLE_DATA, encryptionContext);
        final byte[] ciphertext = encryptResult.getResult();
        logger.log("4. Encrypt the data");

        // 5. Decrypt the data
        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(masterKeyProvider, ciphertext);
        logger.log(" 5. Decrypt the data");

        // 6. Before verifying the plaintext, verify that the customer master key that
        // was used in the encryption operation was the one supplied to the master key provider.
        if (!decryptResult.getMasterKeyIds().get(0).equals(keyArn)) {
            throw new IllegalStateException("Wrong key ID!");
        }
        logger.log("6. Before verifying the plaintext, verify that the customer master key that");

        // 7. Also, verify that the encryption context in the result contains the
        // encryption context supplied to the encryptData method. Because the
        // SDK can add values to the encryption context, don't require that
        // the entire context matches.
        if (!encryptionContext.entrySet().stream()
                .allMatch(e -> e.getValue().equals(decryptResult.getEncryptionContext().get(e.getKey())))) {
            throw new IllegalStateException("Wrong Encryption Context!");
        }
        logger.log("7. Also, verify that the encryption context in the result");

        // 8. Verify that the decrypted plaintext matches the original plaintext
        byte[] result = decryptResult.getResult();
        logger.log("8. Verify that the decrypted plaintext matches the original plaintext");
        logger.log("After decrypt, the text is:" + Arrays.toString(result));
    }
}
