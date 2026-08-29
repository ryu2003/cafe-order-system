# Cafe Order & Inventory System（カフェ注文・リアルタイム在庫管理システム）

## 1. プロジェクト概要
カフェのモバイル注文および店舗側のリアルタイム在庫管理を行うフルスタックWebアプリケーションです。
店舗のモーニングラッシュ時における「同時注文による在庫不整合（マイナス在庫）」を防ぐ排他制御機構を、Spring BootとPostgreSQLを用いて堅牢に設計しています。
単なる「動くアプリ」の枠を超え、実務のエンタープライズ要件（データ整合性の保証、カプセル化、エラーハンドリングの網羅性、テストコードによる品質担保）を満たす要塞設計を取り入れています。

---

## 2. 背景・要件定義（課題と解決策）

### 業務課題（要件）
* **モーニングラッシュ時の集中アクセス**: 朝の通勤時間帯に限定商品へ同時にアクセスが集中し、マイナス在庫が発生するリスクがある。
* **データ競合時のUX低下**: 注文が競合した際にエラー画面で止まらず、最新の在庫状態へスムーズにリカバリさせる必要がある。

### システムでの解決策
* **楽観的ロック（Optimistic Lock）の導入**: JPAの `@Version` アノテーションを活用し、DB更新時の競合を検知・防衛（409 Conflict 返却）。
* **DTOパターンによる完全分離**: エンティティを直接露出させず、Java 17の `record` を用いたリクエスト/レスポンス用DTOを採用。
* **UIガードと自動リカバリ**: 在庫上限・下限制御、ボタン非活性化、409検知時の在庫自動再フェッチ。

---

## 3. 使用技術（Tech Stack）

* **バックエンド**: Java 17 / Spring Boot 3.x / Spring Data JPA
* **フロントエンド**: React (JavaScript) / Vite / Axios / CSS (Flexbox & Grid)
* **データベース**: PostgreSQL 15 (Docker Composeによるコンテナ環境)
* **品質・テスト**: JUnit 5 / Mockito / MockMvc / AssertJ / ESLint
* **バージョン管理**: Git / GitHub（モノレポ構造）

---

## 4. 技術的なこだわり・工夫したポイント（エンタープライズ設計）

### 1. DTOパターンとエンティティの完全分離・責務の集約
データベースの内部構造（JPAエンティティ）がそのまま外部へ露出するのを防ぐため、リクエスト・レスポンス専用の DTO (Java 17 `record`) を採用しています。
DTO自身に静的ファクトリメソッド（`ProductResponse.from(...)` / `OrderResponse.from(...)`）を持たせ、Service層でDTO変換まで完結させることで、Controllerを薄い委譲レイヤーとして維持しています。

### 2. 楽観的ロック（`@Version`）による排他制御
複数ユーザーが「残り1個」の限定メニューを同時に購入リクエストした際、後から到達したリクエストに対して例外を発生させ、在庫数の不整合（ロストアップデート）を物理的に防止します。

### 3. DDD思想に基づくカプセル化（貧血ドメインモデルの回避）
`@Data` アノテーションを禁止し、`@Getter` とアクセス制限付きコンストラクタでイミュータビリティを確保しています。在庫減算ロジックなどのビジネスルールをエンティティ自身に振る舞いとして集約しています。

### 4. 堅牢なグローバル例外ハンドリング（`@RestControllerAdvice`）
エラーレスポンスを統一DTOに集約しています。
* **400 Bad Request**: 単一・複合バリデーションエラー詳細やドメイン例外
* **409 Conflict**: 楽観的ロック競合検知
* **500 Internal Server Error**: 予期せぬシステム例外

### 5. 型安全なステータス管理
注文ステータスを `Enum` で厳格に定義しています。PostgreSQL の仕様に対応し、昇順の固定ソートを実装して画面表示順を保証しています。

---

## 5. 単体テストの実装状況と品質担保

システムの堅牢性を担保するため、JUnit 5 および Mockito を用いたレイヤー別テストを構築し、全18ケースのオールグリーンを達成しています。

```text
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 現在のテスト実装ステータス
* **例外ハンドリング層（`GlobalExceptionHandlerTest`）**: MockMvcを用いたスタンドアロンテストにより、全例外パターンに対して適切なHTTPステータスとエラーJSONが返却されることを検証済み。
* **サービス層（`ProductServiceTest`, `OrderServiceTest`）**: Mockitoを用いたモック検証により、正常系および在庫不足例外、楽観的ロック競合時の例外伝播を検証済み。
* **コントローラー層（`ProductControllerTest`, `OrderControllerTest`）**: `@WebMvcTest` を用いたWebスライステストにより、エンドポイントのルーティング、リクエストバリデーション、およびレスポンスDTOのマッピングを網羅的に検証済み。

---

## 6. システム構成図 & データベース設計

### システム構成（モノレポ構造）
```text
cafe-order-system/
 ├── backend/                     # Spring Boot (Java 17) バックエンドAPI
 ├── frontend/                    # React / Vite フロントエンドUI
 ├── docker-compose.yml           # PostgreSQL 15 コンテナ設定
 └── README.md
```

### 主要テーブル構造
* **products**（`product_id` [PK], `product_name`, `price`, `stock`, `version`）
* **orders**（`order_id` [PK], `order_date_time`, `total_amount`, `order_status`）
* **order_details**（`order_detail_id` [PK], `order_id` [FK], `product_id` [FK], `quantity`）

---

## 7. ローカル環境での起動手順

### 1. リポジトリのクローンと移動
```bash
git clone [https://github.com/ryu2003/cafe-order-system.git](https://github.com/ryu2003/cafe-order-system.git)
cd cafe-order-system
```

### 2. データベース（PostgreSQL）の起動
```bash
docker compose up -d
```

### 3. バックエンド（Spring Boot）の起動
```bash
cd backend
./mvnw spring-boot:run
```

### 4. フロントエンド（React）の起動
別ターミナルを開き、`frontend` ディレクトリへ移動して起動します。
```bash
cd frontend
npm install
npm run dev
```

### 5. テストの実行
```bash
cd backend
./mvnw test
```

### 6. API単体での動作確認例
* **URL**: `http://localhost:8080/api/products`
```json
[
  {
    "productId": 1,
    "productName": "ブレンドコーヒー",
    "price": 450,
    "stock": 20
  },
  {
    "productId": 2,
    "productName": "カフェラテ",
    "price": 520,
    "stock": 15
  },
  {
    "productId": 3,
    "productName": "特製モーニングセット",
    "price": 780,
    "stock": 10
  }
]
```