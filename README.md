# ObserverUtils

ギガンティック☆整地鯖[^1]において、サポーター[^2]の1種類であるObserver[^3]の業務を補助するためのプラグイン。

## 機能

1. 以下の内容について、予め指定されたRedmine[^4]にチケットを作成する。その際にいくつかの項目を自動で取得する。
   * 修繕依頼
     * サーバー
     * ワールド
     * 座標
     * 依頼の内容
     * コメント
   * 不要保護報告
     * サーバー
     * ワールド
     * 座標
     * 保護名
     * 保護Owner
     * 保護Member
     * 重複保護があるかどうか
       * あるならば重複している保護名
     * 不要だと判断した理由
     * コメント

## コマンド

1. Redmine[^4]へのチケット作成
   * 修繕依頼
     * `/obs fix <依頼の内容(コンマ区切り)> [...コメント]`
     * 依頼内容は半角数字である必要があり、コンマで区切って複数指定することができる。入力必須。
     * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     * プレイヤーのみ実行可能。
     * WorldEditで範囲が（pos1、pos2の両方）指定されていないと実行不可。
     * 例
       * `/obs fix 0,1 大規模のため複数人での作業推奨 凝固スキルがあると便利`
   * 不要保護報告
     * `/obs rg <判断理由の番号(コンマ区切り)> [...コメント]`
     * その保護が不要だと判断した理由は半角数字である必要があり、コンマで区切って複数指定することができる。入力必須。
     * コメントは半角スペースで区切ると改行される。入力しなくてもよい。
     * プレイヤーのみ実行可能。
     * 現在座標に1つ以上WorldGuardの保護がないと実行不可。
     * 例
       * `/obs rg 0,1,2 lastquit:2022/01/05`

### 各指定項目

#### 不要保護報告の判断理由

| ID  | 内容                                |
|-----|-----------------------------------|
| 0   | 未建築または建築途中で、全Ownerのlastquitが7日以上前 |
| 1   | 全Ownerが永久BANを受けている                |
| 2   | 同一箇所に異常なほど重なっている                  |
| 3   | 1マスのみである                          |
| 4   | 極端に長方形である                         |
| 5   | 活用済みの土地が著しく少ない                    |
| 6   | その他                               |

#### 修繕依頼の内容

| ID  | 内容     |
|-----|--------|
| 0   | 空中ブロック |
| 1   | マグマ放置  |
| 2   | 水放置    |
| 3   | トンネル状  |
| 4   | その他    |

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

# ライセンス

[GPL v3](./LICENSE)

[^1]: https://www.seichi.network/gigantic
[^2]: https://redmine.seichi.click/projects/public/wiki/Supporter_Guide
[^3]: https://redmine.seichi.click/projects/public/wiki/Supporter_Guide#1%E7%AB%A0Observer%E6%A8%A9%E9%99%90
[^4]: https://redmine.seichi.click
[^5]: https://kotlinlang.org/docs/coding-conventions.html
[^6]: https://www.conventionalcommits.org/ja/v1.0.0/
