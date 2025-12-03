

---

# 🟦 **XENO – Shopify Analytics Dashboard (Full-Stack)**

A full-stack **Shopify Analytics & Insights Platform** built with **Vite + React + Tailwind** on the frontend and **Spring Boot + PostgreSQL** on the backend.
Supports **JWT Authentication**, **multi-tenant Shopify ingestion**, **scheduled sync**, and a full analytics dashboard deployed on **Netlify (Frontend)** and **Railway (Backend + PostgreSQL)**.

---

# 🚀 Features

### **Frontend**

* ⚡ Vite + React UI (Super fast)
* 🎨 TailwindCSS premium dashboard styling
* 🔐 JWT Auth (Login + Protected Routes)
* 📊 Real-time charts: Revenue / Orders / Customers / Countries
* 🌍 Multi-tenant support (Tenant picker from JWT)
* 📨 Axios-powered API communication
* 🌐 Netlify Deployment (with Custom Domain)

### **Backend**

* 🛠 Spring Boot 3 (production ready)
* 🗄 PostgreSQL hosted on Railway
* 🏷 Multi-Tenant Architecture (TenantContext)
* 🔑 JWT Security (Custom filter + token validation)
* 🌐 Shopify REST Admin API integration
* 🔁 **Scheduled Sync** (Auto fetch every X hours)
* 🎛 Manual Sync Endpoints (`/shopify/sync/orders`, `/shopify/sync/customers`)
* 📈 Analytics API (Orders, Revenue, Top Customers, Country Stats)
* 🌍 CORS configured for Netlify

---

# 🧠 System Architecture
<img width="1800" height="1313" alt="diagram-export-12-2-2025-7_08_47-AM" src="https://github.com/user-attachments/assets/addd1b09-fb46-49a4-ac5e-ba184fdf2bb0" />
```
                ┌──────────────────────────┐
                │        Shopify Store     │
                │   (Orders, Customers)    │
                └─────────────┬────────────┘
                              │ Shopify API
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                     XENO Backend (Spring Boot)               │
│                                                              │
│  • ShopifyClient → fetches data                              │
│  • OrderSyncService / CustomerSyncService                    │
│  • Scheduled Sync (cron)                                     │
│  • Multi-Tenant Engine (TenantContext)                       │
│  • JWT Security                                              │
│  • Analytics Controller (/analytics/*)                       │
└───────────────┬──────────────────────────────────────────────┘
                │ JDBC
                ▼
        ┌──────────────────────┐
        │ Railway PostgreSQL   │
        │ (orders, products,   │
        │  customers, tenants) │
        └──────────────────────┘

                ▲ REST API
                │
┌──────────────────────────────────────────────────────────────┐
│                XENO Frontend (Vite + React)                  │
│   Netlify Deploy → Custom Domain                             │
│   • Login + JWT                                              │
│   • Dashboard analytics charts                               │
│   • Axios → Backend API                                      │
└──────────────────────────────────────────────────────────────┘
```

---

# 📂 Project Structure

```
/Xeno
 ├── frontend/        # Vite + React + Tailwind
 └── backend/         # Spring Boot + PostgreSQL
```

---

# ⚙️ Backend – Setup (Spring Boot)

## **1. Clone Repo**

```bash
git clone https://github.com/SidBotVit/Xeno.git
cd Xeno/backend
```

## **2. Configure Environment Variables**

Create a file:

```
src/main/resources/application.properties
```

Use:

```
spring.datasource.url=jdbc:postgresql://postgres.railway.internal:5432/railway
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}
server.port=${PORT:8080}

JWT_SECRET=your-secret
SHOPIFY_ACCESS_TOKEN=shpat_xxxxx
SHOPIFY_STORE_URL=your-shop.myshopify.com
```

Railway → Variables:

```
PGUSER=postgres
PGPASSWORD=xxxx
SPRING_DATASOURCE_URL=jdbc:${{ Postgres.DATABASE_URL }}
```

## **3. Install & Run**

```bash
./mvnw spring-boot:run
```

---

# 🧩 Backend API Endpoints

### **Auth**

```
POST /auth/login
```

### **Shopify Sync**

```
POST /shopify/sync/orders       (Header: X-Tenant-ID)
POST /shopify/sync/customers    (Header: X-Tenant-ID)
```

### **Analytics**

```
GET /analytics/summary
GET /analytics/orders
GET /analytics/top-customers
GET /analytics/revenue-by-country
GET /analytics/customers-by-country
GET /analytics/new-vs-returning
```

---

# ⏱ Scheduled Sync

A scheduled cron job automatically syncs Shopify orders & customers:

```java
@Scheduled(fixedRate = 3600000) // example: 1 hour
public void scheduledSync() { ... }
```

---

# 🎨 Frontend – Setup (Vite + React + Tailwind + JWT)

## **1. Move to frontend**

```bash
cd frontend
```

## **2. Install**

```bash
npm install
```

## **3. Create `.env`**

```
VITE_API_URL=https://your-backend.up.railway.app
```

## **4. Run**

```bash
npm run dev
```

---

# 🌐 Deployment

## **Backend (Railway)**

1. Create new Railway Service → Deploy Spring Boot
2. Add PostgreSQL service
3. Add environment variables
4. Deploy

## **Frontend (Netlify)**

1. Drag-and-drop `dist/` or connect GitHub repo
2. Add environment variable:

```
VITE_API_URL=https://your-railway-backend.up.railway.app
```

3. Deploy with custom domain

---

# 🗄 Database Schema (Simplified)

### **customers**

* id, first_name, last_name, email, city, state, verified_email…

### **orders**

* id, total_price, currency, status, shopify_order_id, customer_id…

### **products**

* id, title, vendor, sku, price…

### **tenants**

* id, name, shop_domain, access_token

---

# 📸 Screenshots

(Add your UI screenshots here)

```
/screenshots
   dashboard.png
   login.png
   analytics.png
```

---

# 🚀 Future Improvements

* Add Webhooks for real-time Shopify sync
* Add Shopify OAuth for automatic tenant onboarding
* Add product analytics & cohort tracking
* Add multi-user roles (admin, staff, viewer)

---

# 🤝 Contributing

Pull requests are welcome.
For major changes, open an issue first.

---

# 📜 License

MIT License


Just tell me **“add badges”** or **“add logo”**.
