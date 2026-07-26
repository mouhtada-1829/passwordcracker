# PasswordCracker v1

## 1. Introduction

Ce projet a été réalisé dans le cadre du cours de Design Pattern, semestre 2 de la 3e année de génie logiciel à l'École Supérieure Polytechnique de Dakar. L'objectif était de développer un outil en Java capable de "cracker" des mots de passe hachés en MD5, en utilisant le patron de conception Simple Factory.

Le projet a été fait en groupe de 5 personnes, chacun responsable d'une partie bien précise du code. On a utilisé Maven pour la gestion du projet et Git pour le travail collaboratif.

## 2. Présentation du problème

Le MD5 est un algorithme de hachage qui prend une chaîne de caractères et produit une empreinte (hash) de 32 caractères hexadécimaux. C'est un hash dit "à sens unique" : théoriquement, on ne peut pas retrouver le mot d'origine à partir du hash. Mais en pratique, on peut essayer de deviner le mot en hashant un maximum de possibilités et en comparant les résultats.

Le principe est simple : on prend un mot, on calcule son MD5, on compare avec le hash recherché. Si ça correspond, on a trouvé. Sinon, on passe au suivant.

On a implémenté deux méthodes pour ça :
- **Attaque par dictionnaire** : on teste chaque mot d'un fichier dictionnaire
- **Attaque par brute force** : on génère toutes les combinaisons possibles sur un alphabet donné

## 3. Architecture

Le projet est structuré en packages Java organisés par responsabilité :

```
com.passwordcracker
├── core/
│   ├── HashCracker.java        → interface commune à toutes les stratégies
│   └── Md5Util.java            → utilitaire de calcul MD5
├── factory/
│   └── HashCrackerFactory.java → fabrique simple (Simple Factory)
├── strategies/
│   ├── dictionary/
│   │   └── DictionaryHashCracker.java
│   └── bruteforce/
│       └── BruteForceHashCracker.java
├── cli/
│   └── Main.java               → point d'entrée, parsing des arguments
└── resources/                  → fichiers dictionnaire
```

Le projet utilise Maven avec Java 23. Il n'y a aucune dépendance externe : le calcul MD5 se fait avec `java.security.MessageDigest` qui est inclus dans le JDK.

Le flux de fonctionnement est simple : l'utilisateur lance le programme avec une méthode (`-m DICO` ou `-m BRUTE`) et un hash (`-h <hash>`). La fabrique choisit la bonne stratégie, qui va générer des mots, les hasher avec `Md5Util`, et comparer jusqu'à trouver une correspondance.

## 4. Diagramme UML

Le diagramme ci-dessous illustre l'architecture du projet basée sur le pattern **Simple Factory**. L'interface `HashCracker` définit le contrat commun, les deux stratégies concrètes (`DictionaryHashCracker` et `BruteForceHashCracker`) l'implémentent, et la classe `HashCrackerFactory` centralise leur création selon le paramètre passé.

```
┌─────────────────────┐
│   <<interface>>     │
│   HashCracker       │
├─────────────────────┤
│ + crack(hash) : Str │
└──────┬──────────────┘
       │ implémente
       │
       ├────────────────────────────┐
       │                            │
┌──────┴──────────────┐  ┌──────────┴────────────────┐
│ DictionaryHash-     │  │ BruteForceHashCracker     │
│ Cracker             │  │                           │
├─────────────────────┤  ├───────────────────────────┤
│ - wordList : List   │ │ - alphabet : String       │
│ - dictFile : String │  │ - maxLength : int         │
├─────────────────────┤  ├───────────────────────────┤
│ + crack(hash) : Str │  │ + crack(hash) : String    │
└─────────────────────┘  └───────────────────────────┘
           ▲                          ▲
           │                          │
           └──────────┬───────────────┘
                      │
             ┌────────┴────────────┐
             │ HashCrackerFactory  │
             ├─────────────────────┤
             │ + create(method)    │
             │   : HashCracker     │
             └─────────────────────┘
```

## 5. Usage du Simple Factory

Le pattern Simple Factory est utilisé pour centraliser la création des objets `HashCracker`. Au lieu que le code client (le `Main`) instancie directement `DictionaryHashCracker` ou `BruteForceHashCracker`, on passe par une méthode statique `HashCrackerFactory.create()`.

Avantages :
- Si on ajoute une nouvelle stratégie plus tard, on modifie uniquement la fabrique, pas le client
- Le `Main` ne connaît pas les classes concrètes, seulement l'interface
- Le code est plus propre et plus facile à maintenir

Côté client, l'appel est simple :

```java
HashCracker cracker = HashCrackerFactory.create("DICO");
String result = cracker.crack(hash);
```

## 6. Résultats obtenus

### Compilation & exécution

```bash
mvn clean package -DskipTests
java -jar target/passwordcracker.jar -m DICO -h <hashMD5>
java -jar target/passwordcracker.jar -m BRUTE -h <hashMD5>
```

### Tests unitaires

```bash
mvn test
```

Tous les tests passent : validation du hachage MD5, recherche par dictionnaire sur des mots connus, recherche par force brute sur des mots de 1 à 4 caractères.

### Exemples réels (hash MD5 de "test" = `098f6bcd4621d373cade4e832627b4f6`)

```text
$ java -jar passwordcracker.jar -m DICO -h 098f6bcd4621d373cade4e832627b4f6
Mot de passe trouve : test
Tentatives : 6
Vitesse : 0,2 M hachages/sec
Temps : 31 ms

$ java -jar passwordcracker.jar -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6
Progres : [####################] 100%
Mot de passe trouve : test
Tentatives : 355414
Vitesse : 1,4 M hachages/sec
Temps : 251 ms
```

On observe l'écart d'efficacité entre les deux stratégies : le dictionnaire trouve le mot en 6 tentatives, la force brute en 355 414. La force brute est exhaustive mais coûteuse ; le dictionnaire est rapide mais limité aux mots qu'il contient.

### Métriques avancées (CLI)

Le programme affiche systématiquement :
- **Nombre de tentatives** : permet de comparer l'efficacité des stratégies
- **Temps d'exécution** : mesure la performance brute
- **Vitesse en M hachages/sec** : indicateur objectif indépendant de la taille de l'espace de recherche
- **Barre de progression** : visible en mode BRUTE, donne un feedback visuel à l'utilisateur

## 7. Difficultés rencontrées

- **Génération des combinaisons en force brute** : nous avons choisi une approche itérative (compteur base-26) plutôt que récursive, pour éviter les problèmes de profondeur d'appel et faciliter le suivi de progression.
- **Affichage de la progression** : le calcul du hash étant synchrone, il a fallu un thread séparé pour interroger le compteur de tentatives pendant l'exécution sans bloquer le cassage.
- **Respect de l'interface** : le compteur de tentatives a été ajouté via des méthodes `default` dans l'interface `HashCracker` plutôt qu'une classe abstraite, ce qui permet à chaque stratégie d'opter pour son propre suivi.
- **Validation des entrées** : le hash doit être validé avant le lancement pour éviter des erreurs silencieuses.

## 8. Conclusion

Le projet `PasswordCracker v1` atteint ses objectifs : un outil fonctionnel de cassage de mots de passe MD5, structuré autour du patron **Simple Factory**. L'architecture permet de changer de stratégie (DICO / BRUTE) sans modifier le code client, simplement en passant un paramètre à la fabrique.

La principale limitation est que la fabrique simple viole le principe Open/Closed : l'ajout d'une nouvelle stratégie nécessite de modifier `HashCrackerFactory.create()`. Cette limitation sera résolue dans la version 2 avec un patron Factory Method ou un registre de stratégies.
