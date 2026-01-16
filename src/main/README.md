# Machine Events Ingestion Service

## 📌 Overview
Machine Events Ingestion Service is a Spring Boot backend application designed to ingest machine event data in batches, validate inputs, deduplicate identical events using hashing, update newer events when applicable, and provide analytical statistics over a given time range.

This project focuses on correctness, performance, and clean backend design.

---

## 🚀 Features

### ✅ Batch Event Ingestion
- Accepts multiple machine events in a single request
- Supports partial success with detailed rejection reasons

### ✅ Validation
- Rejects events with invalid duration values
- Prevents ingestion of events far in the future

### ✅ Deduplication
- Uses **SHA-256 payload hashing**
- Prevents duplicate records without field-by-field comparison
- Ensures idempotent ingestion

### ✅ Update Logic
- If an event with the same `eventId` already exists:
  - Identical payload → **deduped**
  - Different payload & newer received time → **updated**

### ✅ Analytics API
- Total number of events in a time range
- Total defects across events
- Average event duration

---

## 🛠 Tech Stack
- Java 21  
- Spring Boot  
- Spring Data JPA  
- MySQL  
- Lombok  
- Postman  

---

## 📂 API Endpoints

### 🔹 Ingest Events (Batch)
```
POST /events/batch
```

#### Request Body
```json
[
  {
    "eventId": "EVT-002",
    "machineId": "M-102",
    "eventTime": "2026-01-15T09:35:00Z",
    "durationMs": 3000,
    "defectCount": 0
  }
]
```

#### Response
```json
{
  "accepted": 1,
  "deduped": 0,
  "updated": 0,
  "rejected": 0,
  "rejections": []
}
```

---

### 🔹 Get Event Statistics
```
GET /events/stats?start=ISO_DATE_TIME&end=ISO_DATE_TIME
```

#### Example
```
GET /events/stats?start=2026-01-15T09:00:00Z&end=2026-01-15T10:00:00Z
```

#### Response
```json
{
  "totalEvents": 2,
  "totalDefects": 7,
  "avgDurationMs": 4000
}
```

---

## 🔐 Deduplication Strategy
To efficiently detect duplicate events:
1. Relevant event fields are combined into a single string
2. A SHA-256 hash is generated
3. Matching hashes → deduplicated
4. Different hash + newer received time → updated

This avoids expensive field-by-field comparison and improves performance.

---

## 🧠 Design Decisions
- Used `Instant` for timezone-safe time handling
- Database-level aggregation for better performance
- Transactional batch processing
- Clean Controller → Service → Repository separation

---

## 🧪 Testing
- APIs tested using Postman
- Verified ingestion, deduplication, updates, validation, and analytics

---

## 📌 Conclusion
This project demonstrates real-world backend development concepts using Spring Boot and JPA.
