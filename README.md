## **Cursed Artifact Registry**   


This is full-stack web app, in which backend made on Spring Boot.   
**The main idea** is: A registry for tracking cursed artifacts — their origins, curse types, danger levels, current owners, and logged symptoms.

## **This system does exactly that:**  

- Stores information about cursed artifacts (name, origin, type of curse, danger level)   
- Tracks who currently owns each artifact  
- Keeps a full history of every person who ever owned it  
- Automatically warns all previous owners the moment a new person takes the artifact  


## **Microservices**:
- artifact.service  
- owner.service  
- notification.service  
- auth.service  


## All API endpoints

### auth-service — localhost:8084

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register new user, get JWT token |
| POST | `/api/auth/login` | Public | Login, get JWT token |
| GET | `/api/me` | JWT required | Get current user profile |
| GET | `/api/users` | JWT required | Get all users |

### artifact-service — localhost:8081

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/artifacts` | ADMIN | Create new cursed artifact |
| GET | `/api/artifacts` | Public | List all artifacts |
| GET | `/api/artifacts/{id}` | Public | Get one artifact |
| PUT | `/api/artifacts/{id}` | Public | Change one artifact |
| DELETE | `/api/artifacts/{id}` | Public | Delete one artifact |
| POST | `/api/artifacts/{id}/assign-owner` | ADMIN | Assign owner → fires Kafka |
| POST | `/api/artifacts/{id}/symptoms` | ADMIN | Log a curse symptom |
| GET | `/api/artifacts/{id}/symptoms` | Public | Get all symptoms |

Swagger UI: `http://localhost:8081/swagger-ui.html`

### owner-service — localhost:8082

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/owners` | ADMIN | Register a new owner |
| GET | `/api/owners` | JWT required | List all owners |
| POST | `/api/owners/assign` | ADMIN | Assign artifact to owner via Feign |

### notification-service — localhost:8083

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/notifications/subscribe?email=` | Public | SSE stream for real-time push |
| GET | `/api/notifications?email=` | Public | All notifications for a user |
| GET | `/api/notifications/unread?email=` | Public | Unread notifications only |
| PUT | `/api/notifications/{id}/read` | Public | Mark one as read |
| PUT | `/api/notifications/read-all?email=` | Public | Mark all as read |

---

