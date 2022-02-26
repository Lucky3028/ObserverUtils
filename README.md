# ObserverUtils

## 機能

1. 以下の内容について、予め指定されたRedmineにチケットを作成する。その際にいくつかの項目を自動で取得する。
  * 修繕依頼
    * サーバー
    * ワールド
    * 座標
    * コメント
  * 不要保護報告
    * サーバー
    * ワールド
    * 座標
    * 保護名
    * 保護Owner
    * 保護Member
    * 重複保護かどうか
    * コメント

## コマンド

1. Redmineへのチケット作成
* 修繕依頼
  * `/obs fix <コメント>`
  * コメントは半角スペースで区切ると改行される。
* 不要保護報告
  * `/obs rg <コメント>`
  * コメントは半角スペースで区切ると改行される。

## 開発

1. `git clone`
2. `chmod +x ./gradlew`
3. `./gradlew jar`
    * Jarを生成する。生成先は`./build/libs/ObserverUtils-<Ver.>.jar`。
4. `./gradlew proguard`
    * `jar`タスクで生成されたJarをminimizeする。生成先は`./build/libs/ObserverUtils-<Ver.>.opt.jar`。
5. Spigotサーバーのpluginsフォルダに追加し起動する

## 依存

### サーバー上で作動させる上で必要な依存

* Spigot 1.12.2
* WorldGuard 6.2
* WorldEdit 6.1.5

### 開発を行う上で必要な依存

* Java 1.8
* Kotlin 1.6.10
* [サーバー上で作動させる上で必要な依存]に記述されているもの
* その他、build.gradleに記述されているもの

## ライセンス

[GPL v3](./LICENSE)
