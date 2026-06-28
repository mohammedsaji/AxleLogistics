# Axle Logistics 🚛

> Enterprise-grade Logistics Operations Management System built with Java & Spring Boot.  
> Multi-role · JWT Secured · REST API Driven · PostgreSQL Backed

---

## What is Axle Logistics?

Axle Logistics is a full-stack logistics management platform designed to streamline shipment operations, operator coordination, and workforce administration across multi-modal transportation networks.

Built to handle real-world logistics complexity — from shipment creation to real-time tracking — with strict role-based access control across all stakeholders.

---

## Core Features

### 🚢 Multi-Modal Transport Management
Support for air, sea, rail, and road operations with operator registration, vehicle inventory, and transportation mode configuration.

### 👥 Workforce Management
Hierarchical manager and driver assignment under operators with real-time employee account creation and reporting structure.

### 🔐 Federated Account System
Partner account registration with role-based provisioning across three distinct roles — Admin, Manager, and Driver.

### 📦 Shipment Lifecycle
End-to-end shipment creation, driver and vehicle assignment, delivery tracking, and real-time status monitoring with reassignment capabilities.

### 🔄 Dynamic Reassignment
In-transit operator, driver, and vehicle reassignment with audit-ready status tracking.

### 🔍 Search & Pagination
Intelligent list views with local filtering and API-driven search fallback for scalable data retrieval.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 8+, Spring Boot, Spring MVC |
| Security | Spring Security, JWT, RBAC |
| Database | PostgreSQL |
| API | RESTful APIs, Standardized Pagination |
| Frontend | Vanilla JavaScript (ES6), HTML5, CSS3 |
| Tools | Maven, Git, Postman, IntelliJ IDEA |

---

## Architecture

```
Client (Browser)
      │
      ▼
REST API Layer (Spring Boot)
      │
      ├── Spring Security (JWT Auth + RBAC)
      │
      ├── Controller Layer
      │         │
      │         ▼
      │   Service Layer
      │         │
      │         ▼
      │   Repository Layer
      │         │
      │         ▼
      │    PostgreSQL
      │
      └── Role-Based UI Rendering
          (Admin · Manager · Driver)
```

Stateless REST API architecture with URL parameter-driven application state management and role-specific UI rendering per user profile.

---

## User Roles

| Role | Access |
|------|--------|
| **Admin** | Full system access — operators, workforce, shipments, accounts |
| **Manager** | Workforce management, shipment tracking, driver coordination |
| **Driver** | Assigned shipment view, delivery status updates |

---

## Status

> 🚧 **In Development** — Core modules built. Testing and final refinements in progress.

---

## Author

**Mohammed Shajith E** — Software Engineer  
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Mohammed%20Shajith-blue?style=flat&logo=linkedin)](https://linkedin.com/in/mohammed-shajith-e)
[![GitHub](https://img.shields.io/badge/GitHub-mohammedsaji-black?style=flat&logo=github)](https://github.com/mohammedsaji)
