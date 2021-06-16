package lambda;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyWithoutPlaintextRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyWithoutPlaintextResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.nio.ByteBuffer;
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
        encryptAndDecryptOther(KEY_ARN, logger);
        return "";
    }
    public void encryptAndDecryptOther(final String keyArn, LambdaLogger logger) {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard().build();
        GenerateDataKeyWithoutPlaintextRequest request = new GenerateDataKeyWithoutPlaintextRequest()
                .withKeyId(keyArn).withKeySpec("RSA_4096");
        GenerateDataKeyWithoutPlaintextResult response = kmsClient.generateDataKeyWithoutPlaintext(request);
        logger.log(response.toString());


        EncryptRequest req = new EncryptRequest().withKeyId(keyArn).withPlaintext(ByteBuffer.wrap(EXAMPLE_DATA));
        ByteBuffer ciphertext = kmsClient.encrypt(req).getCiphertextBlob();
        logger.log("After encrypt ciphertext is: "+ciphertext);

        DecryptRequest req1 = new DecryptRequest().withCiphertextBlob(ciphertext);
        ByteBuffer plainText = kmsClient.decrypt(req1).getPlaintext();
    }
}
