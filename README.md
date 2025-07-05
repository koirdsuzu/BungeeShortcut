# BungeeShortcut

**BungeeShortcut** は、BungeeCord 環境でショートカットコマンドを簡単に登録・使用できるプラグインです。  
設定ファイル（`config.yml`）で指定したショートカットを実際のコマンドにマッピングし、サーバー管理を効率化します。

## ✅ 特徴

- 任意のショートカットコマンドを `config.yml` で定義
- `/bungeeshortcut reload` によるリロード対応
- ショートカットごとに権限（permission）設定可能
- `/bungeeshortcut` コマンドで現在のショートカットを一覧表示

## 📦 インストール方法

1. `BungeeShortcut.jar` を BungeeCord サーバーの `plugins/` フォルダに入れます。
2. サーバーを再起動、または `/bungeeshortcut reload` を実行します。
3. `plugins/BungeeShortcut/config.yml` を編集して、ショートカットコマンドを追加します。

## ⚙️ config.yml の例

```yaml
shortcuts:
  hub: "server lobby"
  p: "server prison"
  gmsg: "msg"
````

この例では：

* `/hub` を実行すると `/server lobby` が実行されます。
* `/p` を実行すると `/server prison` が実行されます。
* `/gmsg <player> <message>` は `/msg <player> <message>` として動作します。

## 🧪 コマンド

| コマンド                     | 説明                  |
| ------------------------ | ------------------- |
| `/shortcut名 [引数...]`     | 設定されたショートカットを実行します。 |
| `/bungeeshortcut help`   | ヘルプとショートカット一覧を表示    |
| `/bungeeshortcut reload` | config.yml をリロードします |

## 🔐 権限

| 権限名                                   | 説明                               |
| ------------------------------------- | -------------------------------- |
| `bungeeshortcut.shortcut.<shortcut名>` | 特定のショートカットコマンドの実行権限              |
| `bungeeshortcut.commands.reload`      | `/bungeeshortcut reload` を実行する権限 |

## 🧑‍💻 作者

* Developed by [Koirdsuzu](https://github.com/koirdsuzu)

```

---

必要であれば以下を追加で対応できます：

- 英語版 `README.md`
- `config.yml` のサンプルファイル

ご希望があればお知らせください。
```
