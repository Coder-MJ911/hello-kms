*This is aws kms learning repo*

*hello kms*

Basic
注意⚠️⚠️⚠️：尽量使用一个 KMS 进行练习

```html
AC:
回答下列问题
KMS 是什么服务？能解决什么问题？

使用 KMS key 的方式有哪些？

可以对 KMS key 进行哪些操作？（至少 5 个）

使用 CLI 进行练习

创建对称加密的 KMS key
加密一段字符串

使用同一个 Key 重新加密同一段字符串，观察结果

将加密后的字符串进行解密
```



Advanced
注意⚠️⚠️⚠️：尽量使用一个 KMS 进行练习

```html
AC:
练习
使用 CloudFormation 创建 KMS RSA_4096 的 key

加密并解密一段字符串

将 KMS Key 与其他服务一同使用（Basic 里的对称加密 Key），如

加密 S3 文件
加密 parameter store 里的值

回答下列问题
如何进行 Key rotation？
AWS 如何进行自动 Key rotation？
自动 Key rotation 之后收费有什么变化？

```
