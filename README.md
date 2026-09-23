<div align="center">

# 🔱 Poseidon Capital Solutions

**Application de gestion pour établissement financier**

[![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JUnit5](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5&logoColor=white)](https://junit.org/junit5/)

[![Tests](https://img.shields.io/badge/tests-118%2F118%20passing-brightgreen?style=flat-square)](#-tests--qualité)
[![Coverage](https://img.shields.io/badge/coverage-99%25-brightgreen?style=flat-square)](#-tests--qualité)
[![Javadoc](https://img.shields.io/badge/javadoc-0%20warning-brightgreen?style=flat-square)](https://djerbiano.github.io/P7_Poseidon/)

</div>

<br/>

## 📋 Aperçu

Poseidon est une application web MVC construite avec **Spring Boot** et **Thymeleaf** pour le compte fictif de Poseidon Capital Solutions. Elle centralise la gestion de six types de données financières (offres, courbes de taux, notations, règles de trading, transactions, utilisateurs) derrière une authentification sécurisée par session.

L'accent a été mis sur une architecture propre et testée : séparation stricte Controller / Service / Repository, gestion d'erreurs centralisée, et une suite de tests couvrant la quasi-totalité du code.

<br/>

## ✨ Points clés

| | |
|---|---|
| 🏛️ **Architecture en couches** | Controller → Service → Repository, sans interfaces de service ni DTO — cohérent avec une app MVC Thymeleaf où les templates bindent directement sur les entités |
| 🔐 **Sécurité** | Spring Security par session, mots de passe hachés avec BCrypt (facteur 14), règles de robustesse (8+ caractères, majuscule, chiffre, symbole) |
| 🛡️ **Gestion d'erreurs centralisée** | `GlobalExceptionHandler`, pages 403/404 personnalisées |
| 🧪 **118 tests unitaires** | Couches isolées avec Mockito et MockMvc, 99% de couverture d'instructions et 100% de couverture de branches (JaCoCo) |
| 📝 **JavaDoc complète** | Documentation intégrale du code, 0 warning |

<br/>

## 🗂️ Modules gérés

| Entité | Description |
|---|---|
| **BidList** | Offres sur instruments financiers |
| **CurvePoint** | Points de courbe de taux |
| **Rating** | Notations d'agences (Moody's, S&P, Fitch) |
| **RuleName** | Règles de trading |
| **Trade** | Transactions |
| **User** | Comptes utilisateurs et rôles (ADMIN / USER) |

Chaque module expose les mêmes opérations CRUD (liste, ajout, modification, suppression) via des pages Thymeleaf, avec validation métier propre à chaque entité (ex. au moins une note d'agence requise pour un Rating).

<br/>

## 🏗️ Architecture

```
src/main/java/com/nnk/springboot/
├── domain/          → entités JPA (Lombok)
├── repositories/     → accès aux données (Spring Data JPA)
├── services/         → logique métier
├── controllers/       → pages MVC (Thymeleaf)
├── security/          → authentification, validation des mots de passe
├── exceptions/         → exceptions métier et gestion centralisée
└── config/             → configuration Spring
```

**Flux type**
```
Requête HTTP → Controller → Service → Repository → MySQL
                                 ↓
                          Vue Thymeleaf
```

<br/>

## 🔐 Sécurité

- Authentification par formulaire, session Spring Security
- Mots de passe hachés avec `BCryptPasswordEncoder` (facteur de coût 14)
- Validation de robustesse des mots de passe : 8 caractères minimum, une majuscule, un chiffre, un symbole
- Message d'erreur générique en cas d'échec de connexion, pour éviter l'énumération de comptes
- Pages d'erreur dédiées (403 accès refusé, 404 ressource introuvable)

<br/>

## 🧪 Tests & qualité

| Métrique | Résultat |
|---|---|
| Tests unitaires | **118 / 118** (100%) |
| Couverture des instructions | **99%** |
| Couverture des branches | **100%** |

Chaque couche est testée isolément :

```
ServiceTest       → mock du Repository (Mockito)
ControllerTest     → mock du Service (@MockitoBean) + MockMvc
SecurityTest        → PasswordValidator, CustomUserDetailsService
```

```bash
mvn clean verify
```

Génère les rapports :
- `target/reports/apidocs/index.html` — JavaDoc
- `target/reports/surefire-report/surefire-report.html` — résultats des tests
- `target/site/jacoco/index.html` — couverture de code

<br/>

## 📖 Documentation

JavaDoc complète et rapport de tests consultables en ligne :

**👉 [djerbiano.github.io/P7_Poseidon](https://djerbiano.github.io/P7_Poseidon/)**

<br/>

## 🚀 Installation

```bash
git clone https://github.com/djerbiano/P7_Poseidon.git
cd P7_Poseidon
```

Configure ta base MySQL et complète `application.properties` (non versionné) avec tes identifiants de connexion, puis :

```bash
mvn spring-boot:run
```

L'application est disponible sur `http://localhost:8080`.

<br/>

## 🛠️ Stack technique

**Backend** — Java 17 · Spring Boot 4.1.1 · Spring Security · Spring Data JPA · MySQL · Maven · Lombok

**Frontend** — Thymeleaf · Bootstrap

**Tests** — JUnit 5 · Mockito · MockMvc · JaCoCo · Surefire

<br/>
