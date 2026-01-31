# 🤫 Buddy Secret Project

스프링 부트(Spring Boot)와 Vue.js를 활용한 개인 프로젝트 **Buddy Secret**입니다.  
백엔드 아키텍처 설계와 효율적인 데이터베이스 관리에 중점을 두고 개발하고 있습니다.

## 🛠 Tech Stack

### Backend
- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **ORM:** Spring Data JPA (Hibernate)
- **Database:** MySQL
- **Build Tool:** Gradle
- **Infra:** Docker (Local Development)

### Frontend
- **Framework:** Vue.js 3
- **State Management:** Pinia / Vuex

---

## 🚀 Key Features (Planned)
- [ ] 사용자 인증 및 인가 (Spring Security)
- [ ] JPA를 활용한 도메인 모델링 및 데이터 자동화
- [ ] Docker를 활용한 독립적인 개발 환경 구축
- [ ] 효율적인 API 설계를 통한 프론트엔드-백엔드 통신

---

## ⚙️ Installation & Setup

### 1. Environment Variables
보안을 위해 데이터베이스 계정 정보는 환경 변수로 관리합니다. 실행 시 다음 변수들을 설정해야 합니다.
- `DB_URL`: MySQL 접속 주소
- `DB_USERNAME`: 데이터베이스 계정명
- `DB_PASSWORD`: 데이터베이스 비밀번호

### 2. Backend Setup
```bash
./gradlew build
./gradlew bootRun
```

### 3. Frontend Setup
```bash
cd frontend-directory
npm install
npm run dev
```
