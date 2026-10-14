# LifeMatch

**Making tomorrow possible**

LifeMatch is an academic information system for the **simulated management of organ transplant waiting lists and organ matching**.

The application manages candidates, organ types, available organs, correspondence proposals and users. It ranks eligible candidates using simulated priority criteria and supports the confirmation or cancellation of matching proposals.

---

## Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Jakarta EE 10 | Enterprise application framework |
| Jakarta REST | RESTful API |
| Apache TomEE | Application server |
| Maven | Build and dependency management |
| JPA / OpenJPA | Object-relational persistence |
| MySQL | Relational database |
| BCrypt | Password hashing |
| Bruno | API testing |
| Git & GitHub | Version control |

---

## Features

- Candidate registration, management and automatic priority calculation
- Organ type management with configurable priority points
- Available organ registration and management
- Candidate eligibility filtering and ranking
- Automatic correspondence proposal generation
- Proposal confirmation and cancellation with justification
- Correspondence history
- User management with ADMIN and USER roles
- HTTP session authentication and BCrypt password hashing
- Persistent data storage using JPA and MySQL

---

## Business Rules

### Candidate Eligibility

A candidate is eligible for a simulated organ match when:

1. The required organ type matches the available organ.
2. The candidate and organ have the same blood type.
3. The candidate has the `WAITING` status.
4. The organ has the `AVAILABLE` status.


### Automatic Priority

Candidate priority is calculated using:

- Candidate age
- Priority points of the required organ type
- Urgency level

The calculated priority ranges from **1 to 4**, with 4 representing the highest priority.

### Candidate Ranking

Eligible candidates receive a simulated score:

```text
Compatibility Score = (Candidate Priority × 10) + Completed Waiting Months
```

Candidates are ranked by descending score. In the event of a tie, the earlier waiting-list entry date takes precedence.

### Correspondence Workflow

```text
Available Organ
      |
      v
Find Eligible Candidates
      |
      v
Calculate Scores and Ranking
      |
      v
Create PENDING Proposal
      |
      +---- Confirm ----> SELECTED
      |                    |
      |                    +--> Candidate: MATCHED
      |                    +--> Organ: MATCHED
      |
      +---- Cancel -----> CANCELLED
                           |
                           +--> Store Cancellation Reason
                           +--> Try Next Eligible Candidate
```

The system avoids proposing the same organ again to candidates whose previous proposals for that organ were cancelled.

A confirmed match does **not** represent a completed transplant.

---

## Authentication and Authorization

LifeMatch uses HTTP sessions and BCrypt password hashing.

| Role | Permissions |
|---|---|
| ADMIN | User management and administrative operations |
| USER | Operations permitted to authenticated users |

The application protects against deleting the currently authenticated administrator and removing the last administrator.

An initial administrator is created automatically if the `admin` account does not already exist.

The administrator password is supplied through the `LIFEMATCH_ADMIN_PASSWORD` environment variable.

Passwords are never included in user response DTOs.

---

## REST API

**Base URL**

```text
http://localhost:8080/lifematch/api
```

### Candidates

| Method | Endpoint | Description |
|---|---|---|
| POST | `/candidates` | Create a candidate |
| GET | `/candidates` | List or filter candidates by name |
| GET | `/candidates/{id}` | Get candidate by ID |
| PUT | `/candidates/{id}` | Update candidate |
| DELETE | `/candidates/{id}` | Delete candidate |

### Organs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/organs` | Register an organ |
| GET | `/organs` | List organs |
| GET | `/organs/{id}` | Get organ by ID |
| PUT | `/organs/{id}` | Update organ |
| DELETE | `/organs/{id}` | Delete organ |

### Correspondences

| Method | Endpoint | Description |
|---|---|---|
| GET | `/correspondences` | List correspondence summaries |
| GET | `/correspondences/history` | View full history (ADMIN) |
| GET | `/correspondences/organ/{organId}/ranking` | View candidate ranking |
| POST | `/correspondences/organ/{organId}/proposal` | Generate proposal |
| POST | `/correspondences/{id}/confirm` | Confirm proposal |
| POST | `/correspondences/{id}/cancel` | Cancel proposal with reason |

### Users

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users` | Create user (ADMIN) |
| GET | `/users` | List users (ADMIN) |
| GET | `/users/{id}` | Get user (ADMIN) |
| PUT | `/users/{id}` | Update user (ADMIN) |
| DELETE | `/users/{id}` | Delete user (ADMIN) |

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Log in |
| GET | `/auth/me` | View current session |
| POST | `/auth/logout` | Log out |

Organ type management is also supported by the REST API.

---

## Project Status

The following components have been implemented and tested:

- [x] Candidate CRUD
- [x] Organ type CRUD
- [x] Organ CRUD
- [x] Correspondence proposals and history
- [x] Automatic candidate priority and ranking
- [x] Proposal confirmation and cancellation
- [x] User CRUD
- [x] Authentication and authorization
- [x] BCrypt password protection
- [x] JPA and MySQL persistence
- [x] Persistence after application restart

### Future Improvements

- [ ] Automated tests
- [ ] Improved validation and error handling
- [ ] Stronger session and authorization controls
- [ ] Enhanced database constraints and relationships
- [ ] Graphical user interface
- [ ] More detailed simulated matching criteria

---

**LifeMatch — Making tomorrow possible.**
