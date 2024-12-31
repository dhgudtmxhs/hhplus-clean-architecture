## 항해플러스 2주차 - TDD & Clean Architecture

- 아키텍처 준수를 위한 애플리케이션 패키지 설계
- 특강 도메인 테이블 설계 (ERD) 및 목록/신청 등 기본 기능 구현
- 각 기능에 대한 **단위 테스트** 작성

## 요구사항

1️⃣ **(핵심)** 특강 신청 **API**

- 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
- 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있습니다.
- 특강은 선착순 30명만 신청 가능합니다.
- 이미 신청자가 30명이 초과 되면 이후 신청자는 요청을 실패합니다.

**2️⃣ 특강 신청 가능 목록 API** 

- 날짜별로 현재 신청 가능한 특강 목록을 조회하는 API 를 작성합니다.
- 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기 전 목록을 조회해 볼 수 있어야 합니다.

3️⃣  **특강 신청 완료 목록 조회 API**

- 특정 userId 로 신청 완료된 특강 목록을 조회하는 API 를 작성합니다.
- 각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 합니다.

### **`STEP 3`**

- 설계한 테이블에 대한 **ERD** 및 이유를 설명하는 **README** 작성
- 선착순 30명 이후의 신청자의 경우 실패하도록 개선
- 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증하는 **통합 테스트** 작성

### **`STEP 4`**

- 같은 사용자가 동일한 특강에 대해 신청 성공하지 못하도록 개선
- 동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공하는 것을 검증하는 **통합 테스트** 작성

### `PLUS`
- 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려 하여 구현합니다.

## ERD
![스크린샷 2024-12-24 오후 10 14 45](https://github.com/user-attachments/assets/6441e9e7-8312-442e-8151-44a77b9057ca)


![스크린샷 2024-12-24 오후 10 22 50](https://github.com/user-attachments/assets/63912f52-f6bf-480f-b432-4b1d809b9040)

> #### Table default : id, delete_at, created_at, updated_at

### lecture(특강) 
- **`id`** - 특강의 고유 식별자 (Primary Key)

- **`title`** - 특강명

- **`lecturer`** - 강사명

- **`lecture_date`** - 특강일 - 날짜 기준으로 특강 조회 시 사용됩니다.

- **`start_time`** - 특강 시작 시간
  
- **`end_time`** - 특강 종료 시간

- **`remaining_capacity`** - 특강의 남은 자리 수. default = 30

> #### 특강은 대체로 단일 단위로 진행되므로 시작일과 종료일의 구분을 두지 않았습니다.

### lecture_registration(특강 신청 정보)
- **`id`** - 특강신청 고유 식별자 (Primary Key)

- **`lecture_id`** - 신청한 특강의 ID
  - **참조:** `lecture.id`(논리)

- **`user_id`**  - 신청한 사용자의 ID
  - **참조:** `user.id`(논리)
 
> #### lecture_registration 의 userId, lectureId를 Unique로 두었습니다.

### user(사용자)

- **`id`** - 사용자 고유 식별자 (Primary Key)

- **`name`** - 사용자 이름


## 클린 + 레이어드 아키텍처 구조
```plaintext
io.hhplus.cleanarchitecture
┣━ interfaces
┃   ┣━ lecture
┃   ┃   ┣━ LectureController
┣━ application
┃   ┣━ lecture
┃   ┃   ┣━ LectureFacade
┃   ┃   ┣━ LectureRegistrationRequest
┃   ┃   ┗━ LectureRegistrationResponse
┃   ┃   ┗━ LectureRegistrationMapper
┣━ domain
┃   ┣━ lecture
┃   ┃   ┣━ Lecture
┃   ┃   ┣━ LectureRegistration
┃   ┃   ┣━ LectureRepository
┃   ┃   ┗━ LectureService
┃   ┣━ user
┃   ┃   ┣━ User
┃   ┃   ┣━ UserRepository
┃   ┃   ┗━ UserService
┣━ infra
┃   ┣━ lecture
┃   ┃   ┣━ LectureJPARepository
┃   ┃   ┣━ LectureRegistrationJPARepository
┃   ┃   ┗━ LectureRepositoryImpl
┃   ┣━ user
┃   ┃   ┣━ UserJPARepository
┃   ┃   ┗━ UserRepositoryImpl
┣━ common
┃   ┣━ entity
┃   ┃   ┗━ BaseEntity
```

### 아키텍처 설계

이번 프로젝트에서는 구현해야 할 도메인이 제한적이고, 도메인별로 개별 설정이 필요하지 않다고 판단하여,  
도메인 단위의 레이어 분리 대신 **각 레이어 내에서 도메인별 구분**을 두는 방식으로 진행했습니다.

---

### 목표 구조: `Interfaces → Application → Domain ← Infra` 와 변화

- 의존성은 내부 계층(Domain)을 향하도록 설계하고자 했습니다.
- 하지만 완벽한 의존성 제거보다 실용적인 설계를 고려하여, domain에서 jpa를 의존하게 된다면 DIP를 위반하는 것이지만  
  domain 객체는 의존하도록 했습니다.
- interfaces와 application 영역에서는, 객체간 변환에 차이점이 없다고 판단하여 application에서만 dto를 생성했습니다.
---

### 도메인 주도 설계(DDD) 관점

- `Lecture`를 애그리거트 루트로 설정했습니다.
- `LectureRegistration`은 `Lecture` 애그리거트 내의 엔티티로 간주했습니다.
- 해서 패키지를 별도로 분리하지 않고, JPARepository를 제외한 클래스들을 `Lecture`와 동일한 패키지에서 공유했습니다.




