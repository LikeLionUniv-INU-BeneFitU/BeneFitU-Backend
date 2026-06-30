# 14th-BeneFitU-Backend : 대학생 맞춤 혜택 통합 플랫폼
[멋쟁이사자처럼] 대학생 맞춤 혜택 통합 플랫폼 (14기 픽유 팀)
> ![Project Status](https://img.shields.io/badge/Status-Active-brightgreen) ![Java Version](https://img.shields.io/badge/Java-21-orange) ![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-3.5.13-green)

---

## 1. 개발 환경 및 주요 기술 스택 (Tech Stack)

### 🚀 Environment & Framework
![Java](https://img.shields.io/badge/Java_21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5.13-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
* **Java 21**
  * *이유: LTS(Long-Term Support) 버전의 안정성과 최신 모던 자바 문법 활용*
* **Spring Boot `3.5.13`**
  * **Packaging:** `JAR`
  * **Configuration:** `YAML`

### 📦 Dependencies (Libraries)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate/JPA](https://img.shields.io/badge/Hibernate_JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger_OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
* **Web & Security:** Spring Web, Spring Security, Validation
* **Data & ORM:** Spring Data JPA, MySQL Driver
* **Tooling:** Lombok, Springdoc-openapi (Swagger)

---

## 2. 프로젝트 패키지 구조

```text
com.example.project
├── 📂 domain
│   ├── 📂 ???
│   │   ├── 📂 controller
│   │   ├── 📂 service
│   │   ├── 📂 repository
│   │   ├── 📂 dto
│   │   └── 📂 entity
│   └── 📂 ???
│       ├── 📂 controller
│       └── ...
└── 📂 global
    ├── 📂 common (공통 응답 등)
    ├── 📂 error (예외 처리 관련)
    └── 📂 config (보안, Swagger 등 전역 설정)
```
© 2026 LikeLion INU. All rights reserved.
