🎵 Music Content Management System (CMS)

A scalable and secure Music Content Management System (CMS) built during my internship at ENIL (Gaana).
This platform empowers artists, labels, podcasters, admins, super admins, and general users to manage and consume music content with role-based permissions.

🚀 Features

🔑 Authentication & Authorization

Email OTP verification during signup

JWT-based authentication

Role-Based Access Control (RBAC) + Fine-Grained Permissions

Redis-based rate limiting for OTP & login

🎶 Music & Media Management

Upload & manage tracks, albums, and playlists

Multi-role dashboards (User, Artist, Label, Admin, Super Admin)

Cloudinary integration for audio files and album artwork

Playlist sharing via public links

📊 Analytics & Engagement

Stream logging after 30 seconds

Trending track detection using Redis + SQL optimizations

Like/favorite tracking

⚡ Performance & Security

Redis caching for high-traffic endpoints

API rate limiting per user/IP

BCrypt password hashing

Secure role/permission enforcement with Spring Security

🎨 Frontend

React + Tailwind CSS SPA

Dynamic role-based routing & dashboards

Playlist manager and track explorer

🏗️ System Architecture

Tech Stack

Component	Purpose
Spring Boot (v3.5.3)	Backend framework (REST APIs)
Spring Security + JWT	Authentication & fine-grained authorization
MySQL (v8.4.0)	Relational data storage
Hibernate/JPA	ORM for database interactions
Redis (v6.3.1)	Caching, rate limiting, lightweight metadata
Cloudinary (v2.0.0)	Media storage & delivery
React + Tailwind CSS	Frontend SPA
Axios	API communication
Postman	API testing
IntelliJ IDEA / VS Code	Development IDEs

High-Level Flow:

Frontend (React) communicates with backend REST APIs.

Backend (Spring Boot) handles business logic, validation, security.

MySQL stores core entities (users, roles, tracks, playlists, stream logs).

Redis caches trending songs, likes, stream stats, and enforces rate limits.

Cloudinary manages audio files and album artwork.

🔐 User Roles & Permissions

Super Admin – Full control (role assignment, content deletion, user management)

Admin – Manage users, moderate content

Artist / Label – Upload/manage tracks & albums

General User – Browse, stream, like, share playlists

Permissions include:

MANAGE_CONTENT – Add/update tracks & albums

MANAGE_USER – Manage users & roles

ASSIGN_ROLE – Assign permissions/roles

BROWSE_MUSIC – Access and play music

🗄️ Database (Simplified Schema)

users – id, name, email, password, role_id, status

roles – id, role_name

permissions – id, permission_name

tracks – id, title, artist_id, album_id, genre_id, url

albums – id, name, label_id

playlists – id, name, user_id, is_public

track_stream_log – id, user_id, track_id, timestamp

⚙️ Setup Instructions
1. Clone the Repository
git clone https://github.com/ujjwalkumar-64/music-cms.git
cd music-cms

2. Backend Setup
cd backend
mvn clean install
mvn spring-boot:run

3. Frontend Setup
cd frontend
npm install
npm start

4. Environment Variables

Configure the following:

DB_URL, DB_USER, DB_PASS → MySQL connection

REDIS_URL → Redis instance

CLOUDINARY_API_KEY, CLOUDINARY_SECRET → Cloudinary credentials

JWT_SECRET → Token signing secret

📌 Future Improvements

HLS-based streaming for adaptive playback

Advanced analytics dashboards (artist/label insights)

AI-powered recommendations

Comment & feedback system

Bulk uploads for labels/artists

Mobile app integration

Audit logs & 2FA support

📖 References

Spring Security Docs

JWT.io

Redis Documentation

Cloudinary Docs

Spring Data JPA

👨‍💻 Author

Ujjwal Kumar
Developed during internship at ENIL (Gaana) under the guidance of Abhishek Gupta (Mentor) and Rohit Mittal (Supervisor).
