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

*[Section à compléter après exécution des tests]*

## 7. Difficultés rencontrées

*[Section à compléter]*

## 8. Conclusion

*[Section à compléter après finalisation du projet]*
