# TODO App

勉強用のプロジェクト。angular8系  
play2.7系  

# Versions
scala 2.12  
play 2.7.1  
slick 3.3.1  
mysql 5.7  
angular 8

# 作成状況/予定
[Scala]  
- Error Handler
    - CustomErrorHandlerサンプル (完了)
    - JsonValidatorのエラーサンプル (未完了)
    - エラーメッセージ管理 (未完了)
- 認証処理
    - JWTのRSA認証追加 (完了)
    - session filter (完了)
- logging (未完了)
- Repositoryのreturnを調整。serviceでtransactionが切れるようにする
- DIの方針を決める
  
[Angular]  
- ngrxサンプル (完了)
- 認証処理 (未完了)
- Error Handler
    - インターセプト (完了)
    - 共通モーダル (一旦完了)
- Validator
    - CustomValidatorサンプル (完了)
    - Validatorのエラー表示 (未対応)
- logging (未完了)

# メモ

## create private key

```
openssl genrsa -out {keyname} 2048
openssl rsa -pubout < {keyname} > {pub_keyname}
openssl pkcs8 -in {keyname} -out {converted_keyname} -topk8 -nocrypt 
```
