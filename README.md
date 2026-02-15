# Automation Trade (Spring Boot + Kite Connect)

Algorithmic intraday trading system using **Kite Connect (REST + WebSocket)** and **PostgreSQL**. The bot runs a conservative VWAP pullback strategy suitable for small capital (around ₹5,000), with built-in risk controls.

## Tech Stack
- Java 17
- Spring Boot 3 (Web, JPA, Scheduling)
- Zerodha Kite Connect Java SDK (`com.zerodhatech:kiteconnect`)
- Liquibase for schema migrations
- PostgreSQL
- JUnit + Mockito for unit testing

## Project Structure

```text
src/main/java/com/example/autotrade
├── config/                 # Spring and Kite bean configuration
├── controller/             # REST APIs (auth + trading controls)
├── dto/                    # Candle, signal, order result DTOs
├── entity/                 # JPA entities: users, strategy configs, trades, daily pnl
├── enums/                  # Trade side + status enums
├── kite/                   # Kite authentication and order gateway integration
├── repository/             # Spring Data repositories
├── service/                # Business services (risk, trading, market data)
├── strategy/               # VWAP pullback strategy implementation
└── worker/                 # Scheduled strategy engine loop

src/main/resources/db/changelog
├── db.changelog-master.yaml
└── 001-init-schema.yaml
```

## Environment Variables
Set these before starting:

- `API_KEY`: Kite API key
- `API_SECRET`: Kite API secret
- `ACCESS_TOKEN`: Kite access token (optional at startup; can be generated using `/api/auth/session`)
- `KITE_USER_ID`: Kite user id
- `DB_URL`: JDBC URL (default `jdbc:postgresql://localhost:5432/autotrade`)
- `DB_USERNAME`: DB username
- `DB_PASSWORD`: DB password
- `STRATEGY_POLL_MS`: loop delay in ms for strategy worker

## Database Schema
Liquibase migration creates these tables:

1. `users`
   - stores broker mapping and encrypted token material
2. `strategy_configs`
   - per-symbol config with capital, risk per trade, SL/target points, daily loss cap
3. `trades`
   - all order lifecycle records (pending/placed/executed/rejected/exited)
4. `daily_pnl`
   - day-level pnl and drawdown for loss-cap checks

## Kite Authentication Flow
1. Call `GET /api/auth/login-url`
2. Open returned URL and login to Kite
3. Collect `request_token` from callback URL
4. Call `GET /api/auth/session?requestToken=...`
5. Persist access token securely and pass as `ACCESS_TOKEN`

## Strategy: VWAP Pullback (Intraday)
- Build 1-minute candles from tick data.
- Compute VWAP from available intraday candles.
- Buy signal when:
  - last 4 candles were trending above VWAP,
  - current candle touches VWAP and closes at/above VWAP.
- Entry: market buy (MIS)
- Stop-loss: `entry - slPoints`
- Target: `entry + targetPoints`

## Position Sizing and Risk Management
- Capital baseline: ₹5,000
- Risk per trade: ₹100 (2% of capital)
- Quantity formula:
  - `qty_by_risk = floor(riskPerTrade / |entry - stopLoss|)`
  - `qty_by_capital = floor(capital / entry)`
  - `final_qty = min(qty_by_risk, qty_by_capital)`
- Daily loss cap check blocks new entries once drawdown exceeds configured limit.

## API Endpoints

### Authentication
- `GET /api/auth/login-url`
- `GET /api/auth/session?requestToken=...`

### Trading Controls
- `POST /api/trading/start`
- `POST /api/trading/stop`
- `GET /api/trading/positions`
- `GET /api/trading/trades/today`

## Example Trade Execution Flow
1. Start trading loop: `POST /api/trading/start`
2. WebSocket tick events call `MarketDataService.onTick(...)`
3. Worker runs every `STRATEGY_POLL_MS`
4. For enabled strategy config:
   - risk guard validates daily loss cap
   - strategy evaluates VWAP pullback signal
   - quantity computed from ₹100 risk rule
   - order placed via Kite REST API
   - trade record persisted with status and order id

## Local Run

```bash
# 1) start postgres and create db
createdb autotrade

# 2) build and test
mvn clean test

# 3) run app
mvn spring-boot:run
```

## Deployment Notes
- Build image with `mvn clean package`.
- Set all env vars in runtime (Kubernetes/VM/systemd).
- Ensure NTP-synced clock for market timestamps.
- Use secure secret manager for `API_SECRET` and tokens.
- Add production-grade retry/backoff and order reconciliation job before live trading.

## Unit Tests Included
- `VwapPullbackStrategyTest`: validates signal generation logic.
- `TradeServiceTest`: validates order placement result mapping using mocked Kite gateway.

## Important Safety Notes
- This code is a starter template and must be paper-traded before live deployment.
- Add exchange holidays, market-hours checks, slippage control, and reconciliation safeguards.
