# Demo Rehearsal 1 - Day 17

**Date**: May 8, 2026  
**Team**: Full team  
**Duration**: 8 minutes (timed with stopwatch)  
**Objective**: Complete presentation of vendor risk assessment system, identify issues for fixes  

## Presentation Outline (8 minutes)

### 1. Introduction (1 minute)
- Project overview: Vendor Risk Assessment System
- Key features: Vendor management, risk scoring, reports, authentication, email notifications
- Tech stack: Spring Boot, PostgreSQL, Redis, Docker, Swagger

### 2. Architecture Overview (2 minutes)
- ASCII diagram presentation
- Component breakdown: Backend, Frontend, AI Service, Database, Cache
- Docker Compose setup

### 3. Demo Walkthrough (3 minutes)
- Start services: `docker-compose up --build`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Key endpoints: Auth, Vendor CRUD, Risk reports, File uploads
- Data seeder: 30 demo records loaded on startup

### 4. Code Quality & Testing (1 minute)
- Test coverage: 80%+ with @DataJpaTest, controller tests, JwtUtil, AuditService
- Swagger documentation: @Operation, @ApiResponse on all controllers
- Code review: No hardcoded secrets, SLF4J logging, externalized config

### 5. Conclusion (1 minute)
- Summary of achievements
- Future improvements
- Q&A

## Issues Noted During Rehearsal

### Timing Issues
- [ ] Introduction ran over by 30 seconds
- [ ] Demo walkthrough took 4 minutes instead of 3

### Technical Issues
- [ ] Docker Compose build failed on first run (network issue)
- [ ] Swagger UI not loading immediately after startup
- [ ] Data seeder not visible in database immediately

### Presentation Issues
- [ ] ASCII diagram not clear on screen
- [ ] Forgot to mention .env.example setup
- [ ] Q&A section cut short

### Content Gaps
- [ ] Missing explanation of risk scoring algorithm
- [ ] No mention of audit logging features
- [ ] Frontend not demonstrated (scaffolding only)

## Fixes to Implement

### Pre-Demo Fixes
- [ ] Optimize Docker Compose for faster startup
- [ ] Add health checks to ensure services are ready
- [ ] Prepare better visual aids for architecture diagram

### Presentation Fixes
- [ ] Practice timing with stopwatch
- [ ] Add slides or better formatting for key points
- [ ] Include live demo of risk calculation

### Content Additions
- [ ] Document risk scoring formula in README
- [ ] Add audit log demo endpoint
- [ ] Develop basic frontend demo page

## Next Steps
- Implement fixes before final demo
- Schedule Rehearsal 2 with updated content
- Prepare backup plan for technical failures

**Rehearsal Completed**: [ ] Yes / [ ] No  
**Total Time**: ______ minutes  
**Team Feedback**: ____________________________