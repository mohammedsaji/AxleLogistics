Logistic Operations Management System
A comprehensive logistics operations platform designed to streamline shipment management, operator coordination, and workforce administration across multi-modal transportation networks. The system facilitates end-to-end shipment lifecycle management—from creation and assignment through real-time tracking and status updates—while maintaining strict role-based access control across stakeholders.
Core Capabilities:

Operator & Transportation Management: Multi-modal support (air, sea, rail, road) with operator registration, vehicle inventory management, and transportation mode configuration
Workforce Management: Hierarchical manager and driver assignment under operators; real-time employee account creation and management with reporting structure
Federated Account System: Partner account registration and management for external stakeholders with role-based provisioning (Admin, Manager, Driver)
Shipment Lifecycle: End-to-end shipment creation, driver/vehicle assignment, delivery tracking, and real-time status monitoring with reassignment capabilities
Dynamic Reassignment: In-transit operator, driver, and vehicle reassignment with audit-ready status tracking
Search & Pagination: Intelligent list views with local filtering and API-driven search fallback for scalable data retrieval

Tech Stack:
Backend:

Java with Spring Boot framework
Spring Security for authentication and role-based authorization
RESTful API layer with standardized pagination and response handling

Frontend:

Vanilla JavaScript with static HTML templates and URL-based routing
CSS3 for responsive styling

Database:

PostgreSQL for persistent data storage and relational integrity

Architecture: Stateless REST API with role-based UI rendering (Admin, Manager, Driver profiles) and URL parameter-driven application state management
