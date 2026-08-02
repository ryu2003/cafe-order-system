# ☕ Cafe Order & Inventory System（カフェ注文・リアルタイム在庫管理API）

## 📌 1. プロジェクト概要
カフェのモバイル注文および店舗側のリアルタイム在庫管理を行うWebアプリケーションのバックエンドAPIです。  
店舗のモーニングラッシュ時における「同時注文による在庫不整合（マイナス在庫）」を防ぐ排他制御機構を、Spring BootとPostgreSQLを用いて堅牢に設計しています。

---

## 🎯 2. 背景・要件定義（課題と解決策）

### 業務課題（要件）
- **モーニングラッシュ時の集中アクセス:** 朝の通勤時間帯に限定商品へ同時にアクセスが集中し、在庫管理が崩れるリスクがある。
- **店員と利用者の操作分離:** 注文を行う客画面と、注文状況・在庫を変更する店員画面で権限を明確に分ける必要がある。

### システムでの解決策
- **楽観的ロック（Optimistic Lock）の導入:** JPAの `@Version` アノテーションを活用し、DB更新時の競合を検知・防衛。
- **データ整合性の担保:** トランザクション管理（`@Transactional`）を徹底し、注文失敗時はロールバック。

---

## 🛠 3. 使用技術（Tech Stack）

- **バックエンド:** Java 17 / Spring Boot 3.x / Spring Data JPA
- **フロントエンド:** React (JavaScript / Vite) ※画面操作確認用
- **データベース:** PostgreSQL 15
- **環境構築:** Docker / Docker Compose
- **テスト:** JUnit 5 / Mockito
- **バージョン管理:** Git / GitHub

---

## 🔥 4. 技術的なこだわり・工夫したポイント

### ① 三層アーキテクチャによる堅牢な責務分離
Controller・Service・Repository の各レイヤーの責務を明確にし、コンストラクタインジェクション（`private final`）を活用して保守性とテスト容易性を高めたモダンな設計にしています。

### ② 楽観的ロック（@Version）による排他制御
複数ユーザーが「残り1個」の限定メニューを同時に購入リクエストした際、後から到達したリクエストに対して例外を発生させ、データ破壊を物理的に防止する設計にしています。

### ③ Docker Compose によるワンコマンド環境構築
開発者がローカル環境で即座に動作確認できるよう、PostgreSQL データベースの起動を `docker-compose up -d` 1回で完結させています。

---

## 📐 5. システム構成図 & データベース設計

### システム構成
`React (フロント)` ──[ JSON / REST API ]──> `Spring Boot (バックエンド)` ──[ JPA ]──> `PostgreSQL (Docker)`

### 主要テーブル構造
- **products**（商品ID, 商品名, 価格, 在庫数, **version**）
- **orders**（注文ID, 注文日時, 合計金額, ステータス）
- **order_details**（注文詳細ID, 注文ID, 商品ID, 数量）

---

## 🚀 6. ローカル環境での起動手順

### 1. リポジトリのクローンと移動
git clone [https://github.com/ryu2003/cafe-order-system.git](https://github.com/ryu2003/cafe-order-system.git)
cd cafe-order-system

### 2. データベース（PostgreSQL）の起動
Dockerを使用してローカルにDBコンテナを立ち上げます。
docker-compose up -d

### 3. アプリケーションの起動
Spring Bootを起動し、データベースとの接続およびテーブルの自動生成を行います。
.\mvnw spring-boot:run

### 4. 動作確認（APIのエンドポイント）
ブラウザまたはAPIクライアントで以下のURLにアクセスし、商品一覧がJSON形式で返却されることを確認できます。
- URL: http://localhost:8080/api/products

[
  {"price":450,"productId":1,"productName":"ブレンドコーヒー","stock":20,"version":0},
  {"price":520,"productId":2,"productName":"カフェラテ","stock":15,"version":0},
  {"price":780,"productId":3,"productName":"特製モーニングセット","stock":10,"version":0}
]