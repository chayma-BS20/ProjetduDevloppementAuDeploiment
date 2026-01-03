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

Ne peut pas créer ou supprimer des utilisateurs ou des rôles

3️⃣ Membre

Le Membre peut contribuer et suivre ses tâches.

Endpoints et actions autorisées :

/api/projects/** → lire les projets auxquels il est assigné

/api/tasks/** → créer ses propres tâches (si autorisé), lire ses tâches, mettre à jour uniquement ses propres tâches

/api/teams/my-team → consulter sa propre équipe

Limitations :

Ne peut pas modifier d’autres utilisateurs, rôles, projets ou tâches

🔧 Tester l’application

Authentification avec Postman :

POST /api/auth/login
Content-Type: application/json

{
  "email": "manager@bee.com",
  "password": "password123"
}


Le token JWT obtenu doit être inclus dans l’en-tête Authorization pour tous les endpoints sécurisés :

Authorization: Bearer <JWT_TOKEN>


Exemples d’accès selon le rôle :

Rôle	Exemple d’action
Manager	POST /api/users/addUser → créer un utilisateur
Chef d’équipe	POST /api/projects?teamId=1 → créer un projet pour son équipe
Membre	GET /api/tasks/project/1 → voir les tâches de son projet

Tester les restrictions :

Essayer d’accéder à un endpoint interdit pour un rôle → réponse 403 Forbidden attendue.

🐳 Lancer l’application avec Docker

Docker Compose :

docker-compose up --build


MySQL : localhost:3307 (BDD : bee_db)

API Backend : localhost:8081

- Backend : https://hub.docker.com/r/chayma2012/bee-backend
- MySQL   : https://hub.docker.com/r/chayma2012/bee-mysql

Standalone :
docker run -p 8081:8081 chayma2012/bee-backend:latest



Procédure rapide :

Cloner le repo GitHub

Lancer docker-compose up --build

Tester les endpoints selon le rôle avec Postman