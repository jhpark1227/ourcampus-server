# 지금 우리 학교는 - 교내 시설물 예약/관리 서비스
<img width="600" alt="image" src="https://github.com/user-attachments/assets/c370b701-28a5-4edd-a5fe-2b1fc2439492">
<br>
전국 연합 개발동아리 UMC 5기에서 진행한 프로젝트 '지금 우리 학교는'의 백엔드 리포지토리입니다.

## 🏫 프로젝트 소개

교내의 다양한 시설물을 더 효율적으로 활용할 수는 없을까?

시험 기간이 다가올수록, **그룹스터디실**을 이용 하고자 하는 학생의 수는 증가합니다. 그러나 이에 반해 그룹스터디실의 공급은 부족한 상황입니다. 더불어,  **체육관**을 이용하고자 하는 학생들 중 일부는 사용 방법을 모르는 등의 문제를 겪고 있습니다.
이러한 상황에서, 학생들과 관리자 모두에게 혜택을 제공하는 서비스를 기획하게 되었습니다.

**사용 가능한 시간과 예약 절차를 효과적으로 통합하여 학생들이 간편하게 이용할 수 있도록 하고, 관리자는 시설물을 보다 효율적으로 운영·관리 할 수 있습니다.**

### 🗓️ 기간
* 기획 및 디자인: 2023.10 ~ 2023.12
* 개발: 2023.12 ~ 2024.3

### 🧑‍💻 나의 역할
* 팀장
* DB 설계
* API 작성
    * Redis를 활용한 인기 검색어, 최근 검색어 조회
    * 크롤링을 통한 공지사항 및 열람실 현황 조회
    * 시설물 조회 (유형별 조회, 검색 등)
    * FAQ 조회
* 백엔드 배포
    * AWS EC2, RDS, ElastiCache

<br/><br/>

## 💡 주요 기능

| 기능                   | 내용                                               |
|:---------------------|:-------------------------------------------------|
| 교내 시설물 정보 확인         | 운영 시간, 사용가능한 물품 등 교내 시설물에 대한 상세한 정보를 확인할 수 있습니다. |
| 시설물 이용 가능 여부 확인 및 예약 | 시설물의 예약 현황을 확인하고 원하는 시간에 예약을 할 수 있습니다.           |
| 캠퍼스 맵                | 캠퍼스 맵을 통해 모르는 시설물에 대한 정확한 위치를 파악할 수 있습니다.        |
| 리뷰 작성 및 조회           | 시설물 이용 후에 리뷰를 남기거나 다른 사람이 남긴 리뷰를 열람할 수 있습니다.     |
| 시설물 반납 인증            | 원활한 시설물 관리를 위하여 퇴실 시에 인증 사진을 업로드합니다.             |

<br/><br/>

## 🛠️ 기술스택
<img width="740" alt="image" src="https://github.com/user-attachments/assets/0d1dae2e-f265-4a25-b364-961e43e6ba60">
<img width="740" alt="image" src="https://github.com/user-attachments/assets/1c2619a0-e0f7-4c7d-895c-80ea8f258afe">

<br/><br/>

## 📂 인프라
<img width="740" alt="image" src="https://github.com/user-attachments/assets/6e3f44f8-6fd2-4167-ac28-89a4c8e600fc">

<br/><br/>

## 📂 ERD
![지금 우리 학교는 erd (1)](https://github.com/user-attachments/assets/b6a4964a-0c8d-47c4-b7e4-7ca3c57734ad)

<br/><br/>

## 📂 API 명세서
https://spicy-lillipilli-407.notion.site/API-13d6a72d1937802ca4f4cba44697a17d

<br/><br/>
## 👨‍👩‍👧‍👦 팀 구성

<br/><br/>
## 📺 사용 화면
<img width="240" height="450" alt="image" src="https://github.com/user-attachments/assets/dad0d7a4-7c5a-42a6-b6a2-a82a9654d825">
<img width="240" height="450" alt="image" src="https://github.com/user-attachments/assets/e4d1709b-c153-4b27-a900-9e0c7d1f10be">
<img width="240" height="450" alt="image" src="https://github.com/user-attachments/assets/e630020d-d601-4cc2-a1e5-0222f85e9f65">
<img width="240" height="450" alt="image" src="https://github.com/user-attachments/assets/c386938f-4a1b-4e67-b6c3-ad4d69d964bf">
