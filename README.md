## 항해플러스 2주차 - TDD & Clean Architecture

- 아키텍처 준수를 위한 애플리케이션 패키지 설계
- 특강 도메인 테이블 설계 (ERD) 및 목록/신청 등 기본 기능 구현
- 각 기능에 대한 **단위 테스트** 작성

수정중..
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
![스크린샷 2024-12-24 오후 10 14 45](https://github.com/user-attachments/assets/193dc1b3-249f-400d-ade4-036c72af4480)

![스크린샷 2024-12-24 오후 10 22 50](https://github.com/user-attachments/assets/52014336-6037-4a4a-a7c7-f440b7fe46b7)

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
 
> #### lecture_registration 의 userId, lectureId를 Unique로 두어 Lock과 별개로 중복 신청을 방지합니다.

### user(사용자)

- **`id`** - 사용자 고유 식별자 (Primary Key)

- **`name`** - 사용자 이름


## 클린 + 레이어드 아키텍처 구조
```plaintext
io.hhplus.cleanarchitecture
┣━ interfaces
┃   ┣━ controller
┃   ┣━ request
┃   ┗━ response
┣━ application
┃   ┣━ facade
┃   ┗━ dto
┣━ domain
┃   ┣━ entity
┃   ┗━ repository
┣━ infra
┃   ┣━ repository
┃   ┗━ config
┗━ common
    ┣━ exception
    ┣━ util
    ┗━ advice
        ┗━ ControllerAdvice.java
```
# 수정중...

```
1. 인터페이스 레이어 (Interfaces Layer): 외부와의 상호작용을 담당합니다.
2. 애플리케이션 레이어 (Application Layer): 비즈니스 유스케이스를 구현합니다.
3. 도메인 레이어 (Domain Layer)**: 핵심 비즈니스 로직과 엔티티를 포함합니다.
4. 인프라 레이어 (Infrastructure Layer): 데이터베이스 및 외부 시스템과의 연동을 담당합니다.
5. 커먼 레이어 (Common Layer)**: 공통적으로 사용되는 유틸리티 및 예외 처리를 포함합니다.
```

이번 프로젝트에서, 구현되는 도메인이 한정되어있고 도메인 개별 설정은 없을 것이라 판단해 도메인 별로 layer를 두기보다는 각 layer안에서 구분을 두었습니다.

Interfaces -> application -> domain <- infra 로 표현한 Clean Architecture 구조에서, 의존성의 방향은 항상 내부 계층(Domain)을 향해야 합니다.

하지만 완벽한 의존성 제거보다는 적절한 수준의 타협과 실용적인 설계를 고려하여, domain에서 jpa를 의존하게 된다면 DIP를 위반하는 것이지만 의존하도록 했습니다.


1. View에 Service가 종속적이게 된다. 
   View의 요청 정보가 변경되면 Service 계층까지 영향을 줄 수 있다.
   그래서, Controller와 Service가 강한 의존을 가질 수 있다.

   DTO를 하나만 정의해서 공유
DTO를 interfaces 계층에 위치시키고 이를 그대로 application 계층에서도 사용하겠습니다.
이유는 아래와 같습니다:
필드가 같다면 중복 정의는 불필요합니다.
추가적인 객체 변환 로직이 필요 없어 간결합니다.
과제이므로 지나친 설계는 오버엔지니어링일 가능성이 큽니다.

계층별 책임 분리가 과제의 채점 기준에 포함된 경우:
예: 과제에서 "계층 간 명확한 역할 분리"를 요구한다면, DTO와 Command를 분리하고 Mapper를 작성합니다.

도메인 주도 설계(DDD) 관점에서,
	•	Lecture를 애그리거트 루트로 보고,
	•	LectureRegistration을 애그리거트 내의 엔티티로 볼 수 있습니다.
	•	이 경우, LectureRegistration이 어디에 속하는가? → “강의” 애그리거트 안에 속한다고 보는 것이 일반적입니다.
