# PasswordCracker

## Introduction
Ce projet implémente un outil de cassage de mots de passe MD5 en Java, conçu autour du patron de conception Simple Factory.

## Présentation du problème
Les mots de passe sont souvent stockés sous forme de hash MD5. Ce projet permet de retrouver la valeur originale du mot de passe via deux stratégies : dictionnaire et force brute.

## Architecture
- `HashCracker` : interface commune pour toutes les stratégies.
- `DictionaryHashCracker` : cherche un hash dans un dictionnaire de mots.
- `BruteForceHashCracker` : génère toutes les combinaisons possibles jusqu'à une longueur donnée.
- `HashCrackerFactory` : crée la stratégie demandée à partir d'un argument.
- `Main` : interface en ligne de commande.

## Usage
1. Construire le projet avec Maven :
   ```bash
   mvn package
   ```
2. Lancer la cassure par dictionnaire :
   ```bash
   java -jar target/passwordcracker-1.0.0.jar -m DICO -h e7247759c1633c0f9f1485f3690294a9
   ```
3. Lancer la cassure par force brute :
   ```bash
   java -jar target/passwordcracker-1.0.0.jar -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6
   ```
4. Utiliser un dictionnaire personnalisé :
   ```bash
   java -jar target/passwordcracker-1.0.0.jar -m DICO -h <hash> -d path/to/dictionary.txt
   ```

## Résultats obtenus
- La stratégie dictionnaire retrouve rapidement les mots de passe présents dans le dictionnaire.
- La stratégie force brute teste toutes les combinaisons alphabetiques de longueur 1 à 4.

## Difficultés rencontrées
- Veiller à centraliser l'instanciation des stratégies dans la fabrique.
- Gérer l'encodage et l'accès aux ressources du dictionnaire.

## Conclusion
L'application respecte le patron Simple Factory et offre une base modulaire pour ajouter de nouvelles stratégies de cassage de hash.
