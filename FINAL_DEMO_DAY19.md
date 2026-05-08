# Final Demo - Day 19

**Date**: May 8, 2026  
**Duration**: 8 minutes  
**Objective**: Demonstrate vendor risk assessment system end-to-end  

## Demo Script

### 1. Opening (30 seconds)
**Problem Statement (1 sentence):**  
"Organizations need an automated way to assess and monitor third-party vendor risks to ensure compliance and mitigate potential security threats."

### 2. Architecture Overview (2 minutes)
Show architecture slide/diagram:

```
                                        +--------------------+
                                        |      Frontend      |
                                        |   React / Nginx    |
                                        |    (port 3000)     |
                                        +---------+----------+
                                                  |
                                                  |
                                                  v
                                      +--------------------------+
                                      |      Backend API        |
                                      |  Spring Boot + JWT      |
                                      |  (port 8080)           |
                                      +-----------+--------------+
                                                  |
         +---------------------+---------------------+                     |
         |                     |                     |                     |
         v                     v                     v                     v
+----------------+   +-------------------------+   +----------------+   +----------------+
| PostgreSQL DB  |   |     Redis Cache        |   |   SMTP Server  |   |   AI Service    |
| (vendors/users) |   |  (caching & session)   |   |  (email sender) |   | (risk analysis)|
+----------------+   +-------------------------+   +----------------+   +----------------+
```

**Key Components:**
- Backend: Spring Boot with security, JPA, Redis caching, email notifications
- Database: PostgreSQL with Flyway migrations
- Cache: Redis for performance
- AI Service: Python-based risk analysis
- Frontend: React scaffolding
- Orchestration: Docker Compose

### 3. Launch Live Tool (2 minutes)
**Commands to run:**
```bash
docker-compose up --build
```

**Wait for services to start:**
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- Backend: http://localhost:8080
- AI Service: localhost:8000
- Frontend: localhost:3000

**Verify Swagger UI:**  
http://localhost:8080/swagger-ui/index.html

### 4. Create Vendor Record (2 minutes)
**Steps:**
1. Open Swagger UI
2. Authenticate: POST /auth/login
   - username: admin
   - password: admin123
3. Create vendor: POST /api/vendors/create
   ```json
   {
     "name": "Demo Vendor Inc",
     "contactPerson": "John Doe",
     "email": "john@demovendor.com",
     "phone": "+1-555-0123",
     "description": "Technology services provider",
     "reviewDate": "2026-06-01"
   }
   ```
4. Verify creation: GET /api/vendors

### 5. Watch AI Respond (1.5 minutes)
**Trigger AI Analysis:**
1. Generate risk report: GET /api/vendors/{id}/risk
2. AI Service analyzes vendor data
3. Response includes risk score (0-100) and status
4. Watch AI categorize and provide recommendations

**Expected AI Response:**
```json
{
  "vendorId": 1,
  "riskScore": 75,
  "status": "MEDIUM",
  "recommendations": [
    "Conduct security audit",
    "Review data handling practices"
  ],
  "aiAnalysis": "Vendor shows moderate risk due to limited security documentation"
}
```

### 6. Additional Features Demo (30 seconds)
- File upload: POST /files/upload
- Audit logs: Automatic logging of all actions
- Email notifications: Scheduled vendor reminders
- Data seeder: 30 demo records loaded

### 7. Q&A (1 minute)
- Open for questions
- Highlight key achievements: 80% test coverage, Swagger docs, Docker deployment

## Demo Checklist

### Pre-Demo Setup
- [ ] Ensure Docker Desktop running
- [ ] .env file configured
- [ ] All services healthy
- [ ] Swagger UI accessible
- [ ] AI service responding

### During Demo
- [ ] Problem statement clear
- [ ] Architecture diagram visible
- [ ] Docker Compose starts successfully
- [ ] Vendor creation works
- [ ] AI analysis returns valid response
- [ ] No technical failures

### Post-Demo
- [ ] Note any issues for fixes
- [ ] Gather feedback
- [ ] Document demo success

## Backup Plan
- If Docker fails: Run backend locally with `mvn spring-boot:run`
- If AI service down: Show static response example
- If network issues: Use localhost URLs

**Demo Completed**: [ ] Yes / [ ] No  
**Issues Encountered**: ____________________________  
**Feedback**: ____________________________