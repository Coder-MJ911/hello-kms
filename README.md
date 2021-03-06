*This is aws kms learning repo*

*hello kms*

Basic
注意⚠️⚠️⚠️：尽量使用一个 KMS 进行练习

```html
AC:
回答下列问题
KMS 是什么服务？能解决什么问题？
答案：AWS Key Management Service (AWS KMS) 是一项托管服务，可让您轻松创建和控制客户主密钥(CMK)，即用于加密您的数据的加密密钥。
通过 AWS KMS，您可以更好地控制对加密数据的访问权限。您可以直接在应用程序中或通过与 AWS KMS 集成的 AWS 服务使用密钥管理和加密功能。无论您是在为 AWS 编写应用程序，还是在使用 AWS 服务，您都可以借助 AWS KMS 控制哪些人可以使用客户主密钥并访问您的加密数据。
AWS KMS 与 AWS CloudTrail 集成，后者是一项将日志文件传输到您指定的 Amazon S3 存储桶的服务。通过 CloudTrail，您可以监控和调查哪些人在何时通过何种方式使用了您的 CMK。

使用 KMS key 的方式有哪些？
答案：当您加密数据时，您需要保护您的加密密钥。如果您对密钥进行加密，则需要保护其加密密钥。最终，您必须保护层次结构中保护数据的最高级别的加密密钥（称为主密钥）。这就是 AWS KMS 的用武之地。

可以对 KMS key 进行哪些操作？（至少 5 个）
答案：使用对称或非对称 CMK 对数据进行加密、解密和重新加密；
使用非对称 CMK 对消息进行签名和验证；
生成可导出的对称数据密钥和非对称数据密钥对；
生成适用于加密应用程序的随机数；

使用 CLI 进行练习

创建对称加密的 KMS key
答案：
aws kms create-key \
--tags TagKey=Purpose,TagValue=Test \
--description "Development test key" \
--profile tw-aws-beach

alias:aws kms create-alias --target-key-id=742c058a-0791-4f00-9d3a-df4717c64301 --alias-name=alias/mjj-kms --profile=tw-aws-beach

加密一段字符串
aws kms encrypt --region ap-southeast-2 --key-id 742c058a-0791-4f00-9d3a-df4717c64301 --plaintext "This is text before encryption" --output text --query CiphertextBlob --cli-binary-format raw-in-base64-out --profile tw-aws-beach
加密后：AQICAHh4NGLC3W3o+zKJXVahF6v5N4u5/fht0zRdg5y8ezuA4AEfs0+2ADHaTx05wUQt4RmWAAAAfDB6BgkqhkiG9w0BBwagbTBrAgEAMGYGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMeOLf9jkscgN06uxcAgEQgDneR6kO+KcP/TMeOeebuP0KRpoDyhny19JIHXCKVxlrm2fXaOXajEq04Q+m0AgFCZw/s81/gMrLdnY=

解密：
aws kms decrypt --region ap-southeast-2 --key-id 742c058a-0791-4f00-9d3a-df4717c64301 --ciphertext-blob 'AQICAHh4NGLC3W3o+zKJXVahF6v5N4u5/fht0zRdg5y8ezuA4AEfs0+2ADHaTx05wUQt4RmWAAAAfDB6BgkqhkiG9w0BBwagbTBrAgEAMGYGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMeOLf9jkscgN06uxcAgEQgDneR6kO+KcP/TMeOeebuP0KRpoDyhny19JIHXCKVxlrm2fXaOXajEq04Q+m0AgFCZw/s81/gMrLdnY=' --output text --query Plaintext --profile tw-aws-beach | base64 -D

使用同一个 Key 重新加密同一段字符串，观察结果
答案：加密结果不同

将加密后的字符串进行解密
解密：
aws kms decrypt --region ap-southeast-2 --key-id 742c058a-0791-4f00-9d3a-df4717c64301 --ciphertext-blob 'AQICAHh4NGLC3W3o+zKJXVahF6v5N4u5/fht0zRdg5y8ezuA4AEfs0+2ADHaTx05wUQt4RmWAAAAfDB6BgkqhkiG9w0BBwagbTBrAgEAMGYGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQMeOLf9jkscgN06uxcAgEQgDneR6kO+KcP/TMeOeebuP0KRpoDyhny19JIHXCKVxlrm2fXaOXajEq04Q+m0AgFCZw/s81/gMrLdnY=' --output text --query Plaintext --profile tw-aws-beach | base64 -D

```



Advanced
注意⚠️⚠️⚠️：尽量使用一个 KMS 进行练习

```html
AC:
练习
使用 CloudFormation 创建 KMS RSA_4096 的 key
key-id:
8d468822-9303-4150-bfaa-5be96d164449

加密并解密一段字符串
加密：
aws kms encrypt --region ap-southeast-2 --key-id 8d468822-9303-4150-bfaa-5be96d164449 --encryption-context contextName=service1 --plaintext "This is text before encryption" --output text --query CiphertextBlob --cli-binary-format raw-in-base64-out --profile tw-aws-beach --encryption-algorithm RSAES_OAEP_SHA_256

解密：
aws kms decrypt --region ap-southeast-2 --key-id 8d468822-9303-4150-bfaa-5be96d164449 --ciphertext-blob 'QlHbocxGwgj1g+ERZLhKkjy2CMqUBOUL/O+y8FYF1efE7mSDpKqx3pDsptFb7ts/EsOEV4k0dpjR8H0gh9nhPcB2H39rBOXwI6grQP07e9cowv2bnk0Z+8GVKnDU1b4u72be5emlAcKbyYDRSaPgHg+jGI/dB4qxoMvpqP/u8FICENXLV1mYV863OkOGiWU4O+mqUyWexND7N8IZEpD8hX9GH/oso/i1B/OlqwL2FFBMr793CsLkcQ7CT844hRQ7rtVyQ2nqRE+ai8Yi2/47YcXMaj4UIpTLp4M80aP7UNmr9+j8Tw/XNxf4kCNVWWM5GYmlPaIkzqr4GKQJU/c6T9gTMF1vFJEYj0U4I0aoc1SW3aMd2sR4Ed0vPM1tUCKf3HuHxucNAKUK4D+17OcYbPEndQQSJPab8Ib86nXTscZh4zixwscLwM79jJmnJx8Cne3mNpIuBI+WV/t9imKDsKdbt1/RfH4Z51krU5Blcx65r1nNaZyj2P8/AQJ6b53Pxc8Y8cfzwmN0ZoHOU6zZ+g+z80aJTIIOwGtUSbZWE/qbhG1iip7IxaXrwebF3ijbMO6hgjKM4JMYYAxPhuyqeWVFMhM2+OIz+UcSPV5oNcjzYuUBMZauivd6/wDkScTn/Ar04Pm5uUNky+DoE9HvqVVtYdqAOet4Uiif2QbG+4Y=' --output text --query Plaintext --profile tw-aws-beach --encryption-algorithm RSAES_OAEP_SHA_256 | base64 -D

将 KMS Key 与其他服务一同使用（Basic 里的对称加密 Key），如

加密 S3 文件
答案：见serverless.yml
download from console page or run this following cli:
aws s3 cp s3://encrypted-bucket-kms-learning/test ./test --profile tw-aws-beach --region ap-southeast-2

加密 parameter store 里的值
答案：
aws ssm put-parameter --name kmsTestKeyMjj --value "secret-value" --type SecureString --key-id 742c058a-0791-4f00-9d3a-df4717c64301 --profile tw-aws-beach --region ap-southeast-2
aws ssm get-parameter --name kmsTestKeyMjj --profile tw-aws-beach --region ap-southeast-2 //获取kmsTestKeyMjj参数，而不解密其值
aws ssm get-parameter --name kmsTestKeyMjj --profile tw-aws-beach --region ap-southeast-2 --with-decryption //获取kmsTestKeyMjj参数，并解密其值


回答下列问题
如何进行 Key rotation？
AWS 如何进行自动 Key rotation？答案：设置EnableKeyRotation为true即可。

自动 Key rotation 之后收费有什么变化？

```


How to deploy a stack:
```shell
brew install serverless
```

```shell
saml2aws login --profile tw-aws-beach
```

```shell
sls deploy
```