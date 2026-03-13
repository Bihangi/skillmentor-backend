# SkillMentor — Online Mentoring Platform

SkillMentor is a full-stack online mentoring platform that connects students with expert mentors for specialised subjects. Students can browse mentors, view expertise and courses, book 1-on-1 sessions, submit payments, and track learning progress.

## Features

### Student Features
- Browse and search mentors by name, skill, or profession
- View detailed mentor profiles with subjects, stats, and reviews
- Book sessions with date, time, and duration selection
- Upload payment proof (bank slip image)
- Track session status (pending → confirmed → completed)
- Cancel or reschedule sessions
- Write reviews for completed sessions

### Mentor Features
- Dedicated mentor dashboard
- View upcoming and completed sessions
- Add meeting links for confirmed sessions
- Track student engagement metrics

### Admin Features
- Role-protected admin dashboard (`/admin`)
- Create/edit/delete mentors with live card preview
- Create/edit/delete subjects assigned to mentors
- Manage all bookings: confirm payments, mark complete, add meeting links
- Approve/reject reschedule requests
- Platform analytics overview
- Searchable, sortable, paginated data tables

### Authentication & Roles
- Clerk-based authentication (sign up / sign in)
- Role selection during onboarding (Student or Mentor)
- Admin role via Clerk public metadata
- JWT-based backend authorization
- Role guard redirects for protected routes

## Tech Stack

| Layer          | Technology                                          |
|----------------|-----------------------------------------------------|
| Frontend       | React 18, TypeScript, Vite, Tailwind CSS, shadcn/ui |
| Backend        | Spring Boot 3, Java 17, Spring Security             |
| Database       | PostgreSQL (Supabase)                               |
| Authentication | Clerk (JWT + public metadata)                       |
| State Mgmt     | React Query (TanStack Query)                        |
| Animations     | Framer Motion                                       |
| Forms          | React Hook Form + Zod validation                    |
| Deployment     | Vercel (Frontend) + Render (Backend)                |

## Project Structure

```
skillmentor-platform/
├── src/                          # React frontend
│   ├── components/               # Reusable UI components
│   │   ├── admin/                # Admin sidebar
│   │   ├── layouts/              # MainLayout, AdminLayout
│   │   └── ui/                   # shadcn/ui components
│   ├── contexts/                 # AuthContext (Clerk integration)
│   ├── hooks/                    # useApi hooks (React Query)
│   ├── pages/                    # Route pages
│   │   └── admin/                # Admin pages
│   ├── services/                 # API client (api.ts)
│   └── data/                     # Mock data fallbacks
├── backend/                      # Spring Boot backend
│   └── src/main/java/com/skillmentor/
│       ├── config/               # Security, CORS, JWT config
│       ├── controller/           # REST controllers
│       ├── dto/                  # Data transfer objects
│       ├── entity/               # JPA entities
│       ├── repository/           # Spring Data repositories
│       ├── service/              # Business logic
│       └── exception/            # Global error handling
└── README.md
```

## API Documentation

### Public Endpoints (No Auth)

| Method | Endpoint                    | Description              |
|--------|-----------------------------|--------------------------|
| GET    | `/api/v1/mentors`           | List all mentors         |
| GET    | `/api/v1/mentors/{id}`      | Get mentor by ID         |
| GET    | `/api/v1/subjects`          | List all subjects        |
| GET    | `/api/v1/reviews/mentor/{id}` | Get reviews for mentor |

### Student Endpoints (Auth Required)

| Method | Endpoint                           | Description                |
|--------|------------------------------------|----------------------------|
| POST   | `/api/v1/sessions/enroll`          | Book a new session         |
| GET    | `/api/v1/sessions/my-sessions`     | Get student's sessions     |
| PUT    | `/api/v1/sessions/{id}/status`     | Cancel a session           |
| POST   | `/api/v1/sessions/{id}/reschedule` | Request reschedule         |
| POST   | `/api/v1/sessions/{id}/payment-proof` | Upload payment proof    |
| POST   | `/api/v1/reviews`                  | Submit a review            |
| PUT    | `/api/v1/users/role`               | Set user role (onboarding) |

### Admin Endpoints (Admin Auth Required)

| Method | Endpoint                                | Description              |
|--------|-----------------------------------------|--------------------------|
| GET    | `/api/v1/sessions/all`                  | List all sessions        |
| PUT    | `/api/v1/sessions/{id}/payment`         | Confirm payment          |
| PUT    | `/api/v1/sessions/{id}/status`          | Update session status    |
| PUT    | `/api/v1/sessions/{id}/meeting-link`    | Add meeting link         |
| PUT    | `/api/v1/sessions/{id}/reschedule/approve` | Approve reschedule    |
| PUT    | `/api/v1/sessions/{id}/reschedule/reject`  | Reject reschedule     |
| POST   | `/api/v1/mentors`                       | Create mentor            |
| PUT    | `/api/v1/mentors/{id}`                  | Update mentor            |
| DELETE | `/api/v1/mentors/{id}`                  | Delete mentor            |
| POST   | `/api/v1/subjects`                      | Create subject           |
| PUT    | `/api/v1/subjects/{id}`                 | Update subject           |
| DELETE | `/api/v1/subjects/{id}`                 | Delete subject           |
| GET    | `/api/v1/admin/overview`                | Platform statistics      |

## Getting Started (Local Development)

### Prerequisites
- Node.js 18+ & npm
- Java 17+ & Maven
- PostgreSQL database (or Supabase)
- Clerk account

### Frontend Setup

```bash
cd frontend   # or project root if monorepo
npm install
npm run dev   # Starts at http://localhost:5173
```

### Backend Setup

```bash
cd backend
mvn spring-boot:run
```

See `backend/SETUP_GUIDE.md` for detailed backend configuration.

## Environment Variables

### Frontend
| Variable                        | Description                |
|---------------------------------|----------------------------|
| `VITE_API_BASE_URL`             | Backend API URL            |
| `VITE_CLERK_PUBLISHABLE_KEY`    | Clerk publishable key      |

### Backend
| Variable                | Description                          |
|-------------------------|--------------------------------------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL            |
| `SPRING_DATASOURCE_USERNAME` | Database username               |
| `SPRING_DATASOURCE_PASSWORD` | Database password               |
| `CLERK_ISSUER_URL`      | Clerk JWKS issuer)                   |
| `CLERK_SECRET_KEY`      | Clerk secret key (for metadata API)  |

## Deployment

- **Frontend**: Deployed on Vercel with environment variables configured
- **Backend**: Deployed on Render connected to Supabase PostgreSQL
- **Database**: Supabase PostgreSQL with seeded data (5 mentors, 12 subjects)
- **Auth**: Clerk JWT template with `public_metadata` claim

## Deployed Links

- **Frontend**: _[Add your Vercel URL here]_
- **Backend API**: (https://skillmentor-backend-l0gf.onrender.com)

## Repository Links

- **Frontend**: _[Add your Vercel URL here]_
- **Backend**: (https://github.com/Bihangi/skillmentor-backend/)
