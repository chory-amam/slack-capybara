slack-capybara
====

[![Build Status](https://travis-ci.org/chory-amam/slack-capybara.svg?branch=master)](https://travis-ci.org/chory-amam/slack-capybara)
[![GitHub release](https://img.shields.io/github/release/chory-amam/slack-capybara.svg)][release]
[![Coverage Status](https://coveralls.io/repos/chory-amam/slack-capybara/badge.svg?branch=master&service=github)](https://coveralls.io/github/chory-amam/slack-capybara?branch=master)

[release]: https://github.com/chory-amam/slack-capybara/releases

## このツールは何？
##### slack用多機能Bot「capybara」です。
このBotはChannelやダイレクトメッセージで入力された文章を学習し、それっぽく返信するコミュニケーション機能や、
地図表示、lgtm、定期通知などの、あったらちょっと便利な機能を提供します。

Javaで動作するので、Javaが動作する環境であれば簡単に導入することが可能です。

## デモ

![optimized](https://raw.githubusercontent.com/chory-amam/chory-amam.github.io/master/images/capybara_demo.gif)

## 使い方

capybaraには、会話で反応する機能とコマンド機能の2つがあります。

### 会話で反応する機能

slackで特定のChannelに話しかけることで反応します。

|       会話      |                 概要                        |
|:---------------:|:-------------------------------------------:|
| 文章            |  文章を学習しそれっぽい言葉を返します。     |
| 5-7-5を含む文章 |  ここで一句とカピバラが俳句を読み上げます。 |

### コマンド機能

```@bot名 コマンド```で以下のように動作します。

|     コマンド    |             概要               |
|:---------------:|:------------------------------:|
| help            | コマンド一覧を表示します。     |
| ping            | pongと返します。               |
| time            | 時間を表示します。             |
| lgtm            | lgtmをランダムで表示します。   |
| misawa          | misawaをランダムで表示します。 |
| map {住所}      | google static mapを表示します。|
| nomurish {言葉} | 言葉をノムリッシュ翻訳します。 |

定期通知機能は、以下のように使います。

|               コマンド                |                    概要                    |
|:-------------------------------------:|:------------------------------------------:|
| job add {スケジュール} 発言内容       | 定期通知ジョブを登録します。               |
| job ls                                | 登録済みジョブとスケジュールを表示します。 |
| job del {ジョブID}                    | 登録済みジョブを削除します。               |

スケジュールの指定方法は、以下のように指定します。

| 設定項目 | 数値                                            |
|:--------:|:-----------------------------------------------:|
| 分       | 0 ~ 59                                          |
| 時       | 0 ~ 23                                          |
| 日       | 1 ~ 31                                          |
| 月       | 1 ~ 12                                          |
| 曜日     | 0 ~ 7 (0:日,1:月,2:火,3:水,4:木,5:金,6:土,7:日) |

各項目ですべての値を指定する場合はアスタリスク(*)を入力します。

##### 例:毎週月曜日の9時にミーティングを通知する場合

```
@bot名 botan job add "0 9 * * 1" ミーティングの時間かっぴ！
```

## 動作環境

###  Oracle Java8以降
Java8の動作環境について詳しくは、[こちらをご覧ください。](http://www.oracle.com/technetwork/java/javase/certconfig-2095354.html)

## インストール

### 動かすまで
1. slackにhubot integrationを登録し、tokenをメモ
2. 動作させるマシンにOracle Java8のjreをインストール
3. 動作させるマシンにcapybaraパッケージをダウンロードし特定の場所に展開する
4. conf内にある「capybara.yaml」に1.でメモしたtokenを入力します。
```
"slack.api.token": "{1.でメモしたtoken}"
```
5. 起動する

##### Winの場合

```
./startup.bat
```

##### Mac・Linuxの場合

```
$ chmod a+x startup.sh
$ ./startup.sh
```

### 特定のChannelで動作させたり止める方法

 特定のChannelで動作させる場合は、管理者ユーザーがcapybara用に登録したhubot integrationを招待(invite)してください。デフォルトではgeneralに所属します。
特定のChannelから追い出す場合は、管理者ユーザーが以下のコマンドを実行します。
```
/remove {bot名}
```

## ライセンス

[Apache License, Version 2.0](https://github.com/chory-amam/slack-capybara/blob/master/LICENSE.txt)

## リンク

Powered by 牡丹(botan)[https://github.com/masahitojp/botan-core]`

## Author

[chory-amam](https://github.com/chory-amam)  

[Masato Nakamura](https://github.com/masahitojp)
