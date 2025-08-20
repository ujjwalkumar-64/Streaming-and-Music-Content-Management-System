# 🎵 Streaming & Music Content Management System (Backend)

A **production-ready backend** for a Streaming & Music Content Management System (CMS), built during my internship at **ENIL (Gaana)**.  
This backend provides **secure, role-based, and scalable APIs** to manage music and podcast content, supporting multiple stakeholders such as **artists, labels, podcasters, admins, super admins, and end-users**.

---

## 📌 Table of Contents

1. [Introduction](#introduction)
2. [Problem Statement](#problem-statement)
3. [Features](#features)
4. [System Architecture](#system-architecture)
5. [Database Design](#database-design)
6. [Authentication & Authorization](#authentication--authorization)
7. [User Roles & Permissions](#user-roles--permissions)
8. [Security Considerations](#security-considerations)
9. [Challenges Faced](#challenges-faced)
10. [Future Improvements](#future-improvements)
11. [Conclusion](#conclusion)
12. [Setup Instructions](#️setup-instructions)
13. [References](#references)
14. [Author](#author)

---

## 🎯 Introduction

This backend system powers a **Music Content Management Platform** designed for scalable content handling and secure collaboration.

Key backend capabilities:

* Secure onboarding with **OTP + JWT authentication**
* **Role-Based Access Control (RBAC)** + fine-grained permissions
* **Stream logging** after 30 seconds for accurate analytics
* Playlist and track APIs with like/favorite tracking
* Redis-based caching and rate limiting for performance & security
* Cloudinary integration for media uploads

---

## ❗ Problem Statement

Music distribution platforms often lack **flexible, secure backend systems** with:

* Role-specific content control
* Scalable metadata handling
* Stream analytics with real user engagement tracking
* Secure collaboration between artists, labels, and admins

This backend CMS solves these by providing a **modular, secure, and production-ready service layer**.

---

## 📌 Features

### 🔑 Authentication & Authorization

* OTP verification during signup (temporary → permanent user migration)
* JWT authentication with refresh support
* RBAC with fine-grained permissions (`MANAGE_CONTENT`, `ASSIGN_ROLE`, `BROWSE_MUSIC`)
* Redis-based rate limiting for OTP & login attempts

### 🎶 Music & Media Management

* APIs for tracks, albums, playlists
* Public/private playlist sharing via unique links
* Track liking & user favorites
* Cloudinary integration for media uploads (audio, artwork)

### 📊 Analytics & Engagement

* Valid streams counted only after 30 seconds playback
* Trending detection with Redis caching + optimized SQL
* Stream logs for per-user/per-track activity

### ⚡ Performance & Scalability

* Redis caching for trending songs, likes, stream stats
* Optimized SQL queries for aggregation (JdbcTemplate)
* Stream spam prevention via Redis throttling

### 🔒 Security

* JWT-based stateless security
* BCrypt password hashing
* Custom filters: Authentication, Validation, Refresh, Logging
* Audit logging for sensitive actions (role changes, deletions)
* Fine-grained per-route access control with Spring Security

---

## 🏗 System Architecture

### 🔹 Backend Tech Stack

* **Spring Boot (3.5.3, Java 17)** – Core REST API framework
* **Spring Security + JWT (0.12.6)** – Authentication & authorization
* **MySQL (8.4.0)** – Relational database
* **Hibernate/JPA** – ORM
* **Redis (6.3.1)** – Caching & rate limiting
* **Cloudinary (2.0.0)** – Media storage & delivery

### 🔹 Architecture Flow

* Clients consume REST APIs from this backend
* **MySQL** persists users, roles, permissions, tracks, playlists, stream logs
* **Redis** caches trending content, like counts, and enforces API rate limits
* **Cloudinary** stores media files and returns secure URLs

---

## 🗄 Database Design

**Entities:**

* `users`, `roles`, `permissions`, `user_permissions`, `role_permissions`
* `tracks`, `albums`, `playlists`, `playlist_tracks`
* `likes`, `track_stream_log`

**Relationships:**

* Artist → Albums → Tracks
* Playlist ↔ Tracks (many-to-many)

---

## 🔐 Authentication & Authorization

* **Signup** → temp user created, OTP sent via email
* **OTP Verification** → migrates to permanent user table with default `ROLE_USER`
* **Login** → JWT issued with roles/permissions
* **Access Control** → enforced via Spring Security filters & custom authorities

---

## 👥 User Roles & Permissions

* **Super Admin** – Full access (assign roles, delete content)
* **Admin** – Manage users & content
* **Artist / Label** – Upload & manage own content
* **User** – Browse, stream, like, create playlists

---

## 🔒 Security Considerations

* JWT-based stateless security model
* BCrypt password hashing
* Redis rate limiting (OTP, login, stream spam prevention)
* CSRF disabled (safe for stateless JWT APIs)
* Request/Action logging for audit readiness

---

## ⚠️ Challenges Faced

1. Designing scalable role-permission architecture
2. Preventing unauthorized edits across multiple roles
3. Accurate stream logging after 30s with Redis throttling
4. High-traffic trending APIs optimization
5. Secure OTP system with Redis-based rate limiting
6. Sync between temp and permanent user DBs
7. Reliable large file uploads with Cloudinary
8. Guarding every API with proper access checks
9. Redis–MySQL consistency management

---

## 🔮 Future Improvements

* HLS-based adaptive streaming
* Advanced analytics dashboards
* AI-powered recommendations
* Commenting & feedback system
* Bulk uploads for artists/labels
* Mobile app integration
* Extended audit logs
* Two-Factor Authentication (2FA)

---

## ✅ Conclusion

This backend provides a **scalable, secure, and production-ready foundation** for music platforms like Gaana.  
It demonstrates real-world implementation of:

* **RBAC & permission-driven access**
* **Spring Boot + Spring Security modular backend**
* **Redis caching & rate limiting**
* **Stream analytics with 30s validation**
* **Cloudinary-based media management**

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/ujjwalkumar-64/music-cms-backend.git
cd music-cms-backend
```

### 2. Backend Setup

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Configure Environment Variables

```env
DB_URL=
DB_USER=
DB_PASS=
REDIS_URL=
CLOUDINARY_API_KEY=
CLOUDINARY_SECRET=
JWT_SECRET=
```

---

## 📖 References

* [Spring Security](https://spring.io/projects/spring-security)
* [JWT.io](https://jwt.io/)
* [Redis Docs](https://redis.io/docs)
* [Cloudinary Docs](https://cloudinary.com/documentation)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

---

## 👨‍💻 Author

**Ujjwal Kumar**

* Internship Project @ **ENIL (Gaana)**
* Mentor: **Abhishek Gupta**
* Supervisor: **Rohit Mittal**

---
