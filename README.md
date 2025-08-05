# TrustCare – Peer-to-Peer Home Care Platform

**TrustCare** is a full-stack home care web application designed for Pakistan. It connects families with verified caregivers (for children, elders, or home help) while ensuring trust, transparency, and safety.

---

## Core Features

### User & Caregiver Profiles
- Role-based registration: User, Caregiver, Admin
- CNIC-based identity verification
- Criminal background check uploads
- Caregiver portfolio: skills, certifications, work history

### Search & Discovery
- Search caregivers by location, skills, availability
- Filters: gender, experience, hourly rate, verified badge

### Booking & Scheduling
- Real-time availability calendar (visible on caregiver profile)
- Session-based booking (hourly or daily)
- Request & acceptance workflow

### Payments & Invoicing
- Simulated secure payments (JazzCash, Easypaisa, Cards)
- Automatic post-session billing
- Viewable invoices & payment history

### In-App Messaging
- Secure user-caregiver chat
- Admin-visible moderation support
- File/document attachments

### Reviews & Complaints
- 5-star post-session feedback
- Report abuse, no-show, or misconduct

### Trust, Safety & Legal
- NADRA API-ready CNIC/address validation
- e-Contract uploads & session history
- GPS check-in/out logs
- Misconduct and damage report logs
- Admin dispute resolution tools

---

##  Technologies Used

| Category              | Stack / Tools                                    |
|-----------------------|--------------------------------------------------|
| **Backend**           | Java, JDBC, MySQL                                |
| **Frontend**          | HTML, CSS, JavaScript (JSP or plain web UI)     |
| **Authentication**    | JWT (JSON Web Token)                             |
| **Asynchronous**      | Amazon SQS (Queue), Amazon SNS (Notifications)  |
| **Database**          | MySQL (local)                                    |
| **Logging/Monitoring**| SLF4J + Logback (JSON structured logging)        |
| **Dev Tools**         | IntelliJ, Postman, Git, GitHub                   |

---
