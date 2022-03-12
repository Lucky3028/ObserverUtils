# ObserverUtils

ギガンティック☆整地鯖[^1]において、サポーター[^2]の1種類であるObserver[^3]の業務を補助するためのプラグイン。

## 機能

1. 以下の内容について、予め指定されたRedmine[^4]にチケットを作成する。その際にいくつかの項目を自動で取得する。
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

1. Redmine[^4]へのチケット作成
   * 修繕依頼
     * `/obs fix <コメント>`
     * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     * プレイヤーのみ実行可能。
     * WorldEditで範囲が（pos1、pos2の両方）指定されていないと実行不可。
   * 不要保護報告
     * `/obs rg <コメント>`
     * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     * プレイヤーのみ実行可能。
     * 現在座標に1つ以上WorldGuardの保護がないと実行不可。

### 権限

初期設定ではOPのみが所持している。`observerutils.obs`を付与することで実行可能。

## 設定

すべて`config.yml`に記載。

* `server-name`
  * サーバーの識別に使用する。Redmineに記載される。
* `redmine-api-key`
  * RedmineにアクセスするためのAPIキー。

## 開発

### デバッグ手順

1. `git clone`
1. `chmod +x ./gradlew`
1. `./gradlew jar`
    * Jarを生成する。生成先は`./build/libs/ObserverUtils-<Ver.>.jar`。
1. Spigotサーバーのpluginsフォルダに追加し起動する

### コーディング規約

Kotlin公式コーディング規約[^5]に従う。

#### Nullable

使用してもよいが、`Result`を使用するなどして必要最小限に抑えること。`!!`演算子の使用も必要最小限で。

### コミット

1コミットあたりの情報は最小限としてください。Conventional Commits[^6]を推奨します。

### デプロイ

運営チームによる手動デプロイ。  
デバッグ環境は整地鯖デバッグサーバーを利用。ただし、デバッグサーバーへのデプロイも手動なので、必要な場合は運営チームに依頼すること。

## 依存

### サーバー上で作動させる上で必要な依存

* Spigot 1.12.2
* WorldGuard 6.2
* WorldEdit 6.1.5

### 開発を行う上で必要な依存

* Java 1.8
* Kotlin 1.6.10
* [サーバー上で作動させる上で必要な依存](#サーバー上で作動させる上で必要な依存)に記述されているもの
* その他、[build.gradle](./build.gradle)に記述されているもの

## ライセンス

[GPL v3](./LICENSE)

[^1]: https://www.seichi.network/gigantic
[^2]: https://redmine.seichi.click/projects/public/wiki/Supporter_Guide
[^3]: https://redmine.seichi.click/projects/public/wiki/Supporter_Guide#1%E7%AB%A0Observer%E6%A8%A9%E9%99%90
[^4]: https://redmine.seichi.click
[^5]: https://kotlinlang.org/docs/coding-conventions.html
[^6]: https://www.conventionalcommits.org/ja/v1.0.0/
