📌 Business Evolution Engine
🎯 Description du projet

La gestion de projet peut être complexe : coordination, répartition des tâches et suivi de l’avancement représentent souvent un défi pour les équipes, ce qui peut ralentir le travail et affecter la réussite globale.

Pour répondre à ces enjeux, nous avons développé une application de gestion collaborative permettant d’organiser, d’attribuer et de suivre le travail de manière structurée et efficace.

Notre plateforme sécurisée et hiérarchisée offre aux utilisateurs un accès adapté à leur rôle, leur permettant de consulter, réaliser ou mettre à jour leurs tâches.

Les principales actions possibles pour un entrepreneur ou un manager sont :

Créer et gérer des projets

Définir et attribuer des tâches aux membres de l’équipe

Suivre l’avancement des tâches

Gérer les utilisateurs, les rôles et les équipes

🚀 Fonctionnalités principales
🔐 Gestion des utilisateurs et des rôles

Créer et gérer les comptes utilisateurs

Attribuer des rôles spécifiques : Manager (Admin), Chef d’équipe, Membre

Authentification sécurisée via JWT pour garantir que chaque utilisateur accède uniquement aux fonctionnalités autorisées

📁 Gestion des projets

Création de projets par les Managers ou Chefs d’équipe

Définir : titre, description, dates de début et de fin, objectifs

Structurer clairement les tâches et responsabilités

📝 Gestion des tâches

Créer des tâches avec titre, description, priorité, date limite et statut (En attente, En cours, Terminée)

Attribution des tâches par les Managers ou Chefs d’équipe

Mise à jour du statut par les utilisateurs pour faciliter le suivi

Ajouter des ressources non relationnelles (images, documents, photos)

👥 Gestion des équipes et affectation des membres

Affecter les membres aux projets selon leurs rôles et compétences

Consultation et contribution aux tâches assignées

🎯 Objectif global

Cette application offre une solution collaborative et efficace pour les entreprises et équipes souhaitant :

Organiser leurs projets

Répartir les responsabilités

Assurer un suivi structuré et contrôlé des tâches de chaque membre

🔒 Rôles et autorisations
1️⃣ Manager

Le Manager a tous les droits sur l’application et peut administrer utilisateurs, rôles, projets, tâches et équipes.

Endpoints et actions autorisées :

/api/users/** → créer, lire, mettre à jour, supprimer les utilisateurs

/api/roles/** → créer, lire, mettre à jour, supprimer les rôles

/api/projects/** → créer, lire, mettre à jour, supprimer les projets

/api/tasks/** → créer, lire, mettre à jour, supprimer toutes les tâches

/api/teams/** → créer, lire, mettre à jour, supprimer toutes les équipes et voir tous les membres

2️⃣ Chef d’équipe

Le Chef d’équipe peut superviser son équipe et ses projets.

Endpoints et actions autorisées :

/api/projects/** → créer, lire et mettre à jour les projets de son équipe

/api/tasks/** → créer, lire et mettre à jour les tâches de son équipe

/api/teams/** → créer et mettre à jour son équipe, consulter les membres

Limitations :
- Ne peut pas créer ou supprimer des utilisateurs ou des rôles


3️⃣ Membre

Le Membre peut contribuer et suivre ses tâches.

Endpoints et actions autorisées :

/api/projects/** → lire les projets auxquels il est assigné

/api/tasks/** → créer ses propres tâches (si autorisé), lire ses tâches, mettre à jour uniquement ses propres tâches

/api/teams/my-team → consulter sa propre équipe

Limitations :

Ne peut pas modifier d’autres utilisateurs, rôles, projets ou tâches


# 🔧 Tester l’application avec Docker

## 1️⃣ Prérequis

Avant de commencer, assurez-vous d’avoir :  

- **Docker** et **Docker Compose** installés  
- **Git** pour cloner le projet  
- **Postman** ou un outil similaire pour tester les API  

---

## 2️⃣ Récupérer le projet et les images Docker

### Étape 1 : Cloner le projet

Ouvrez un terminal (CMD ou PowerShell) et exécutez :

- git clone https://github.com/chayma-BS20/ProjetduDevloppementAuDeploiment.git
- cd business-evolution-engine

Vous êtes maintenant dans le dossier du projet.

### Étape 2 : Télécharger les images Docker

Toujours dans le terminal, exécutez :

- docker pull chayma2012/bee-backend:latest
- docker pull chayma2012/bee-mysql:latest

Cela télécharge les images backend et base de données depuis Docker Hub.


3️⃣ Lancer l’application avec Docker Compose

### Étape 3 : Démarrer les services

Dans le terminal, exécutez :

- docker-compose up --build

La base de données MySQL sera accessible sur localhost:3307 (BDD : bee_db)

L’API backend sera accessible sur localhost:8081

⚡ Pour lancer uniquement le backend sans Docker Compose :

- docker run -p 8081:8081 chayma2012/bee-backend:latest

4️⃣ Se connecter et tester avec Postman
### Étape 4 : Authentification du Manager

1) Ouvrez Postman

2) Créez une requête POST vers :

POST http://localhost:8081/api/auth/login
Content-Type: application/json
Dans le corps (body) de la requête, mettez :
{
  "email": "manager@bee.com",
  "password": "admin123"
}

3) Envoyez la requête

Vous recevrez un JWT token dans la réponse

Ce token vous permettra d’accéder aux endpoints sécurisés.
###  !! Attention : ce token expire après 30 minutes. !!


### Étape 5 : Ajouter le token à Postman

Dans l’onglet Headers de vos requêtes sécurisées :

Key: Authorization
Value: Bearer <JWT_TOKEN>

Remplacez <JWT_TOKEN> par le token obtenu lors de la connexion.


5️⃣ Tester les endpoints selon les rôles


| Rôle          | Exemple d’action                                                    |
| ------------- | ------------------------------------------------------------------- |
| Manager       | `POST /api/users/addUser` → créer un utilisateur                    |
| Chef d’équipe | `POST /api/projects?teamId=1` → créer un projet pour son équipe     |
| Membre        | `GET /api/tasks/project/1` → voir les tâches assignées à son projet |


Pour vérifier les restrictions :
Essayez d’accéder à un endpoint non autorisé pour un rôle → vous devriez obtenir 403 Forbidden.

6️⃣ Créer d’autres utilisateurs
Utilisez l’endpoint POST /api/users/addUser
Remplissez tous les champs requis dans le body de la requête
N’oubliez pas d’inclure le token du Manager dans l’en-tête Authorization
Cela permet de créer des utilisateurs avec différents rôles (Manager, Chef d’équipe, Membre).

### Exemple de création d’une équipe  via POST `/api/teams`
POST http://localhost:8081/api/teams
Content-Type: application/json

{
    "name": "Team Orion",
    "description": "Équipe chargée du projet Orion"
}

### Exemple de création d’un utilisateur via POST `/api/users`
Roles existants par defaut
#### Rôle des `roleId` :
- `roleId: 1` → MANAGER  
- `roleId: 2` → CHEF D'ÉQUIPE  
- `roleId: 3` → MEMBRE

#### Exemple : création d’un membre fictif

**Request Body :**

JSON
{
  "username": "test_membre",
  "email": "test@equipe2.com",
  "password": "password123",
  "phoneNumber": "+3312457896",
  "address": "Paris",
  "role": { "roleId": 3 }, // MEMBRE
  "team": { "teamId": 1 } // Remplacer par l'id de l'equipe que vous venzez de créer 
}


7️⃣ Liens Docker Hub

- Backend : chayma2012/bee-backend
- MySQL : chayma2012/bee-mysql


✅ Procédure rapide pour tester l’application
Cloner le repo GitHub
Télécharger les images Docker (docker pull ...)
Lancer les services avec docker-compose up --build
Se connecter avec Postman (Manager)
Tester les endpoints selon le rôle
