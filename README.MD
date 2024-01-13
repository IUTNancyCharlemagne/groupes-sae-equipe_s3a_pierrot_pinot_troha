
# SAE S3.01 - Développement d'une application

**S3A** Projet réalisé par : 

- [PIERROT Nathan](https://github.com/Ratz123323)
- [PINOT Gaëtan](https://https://github.com/gaetanpinot)
- [TROHA Stanislas](https://github.com/Stantrh)

## **Présentation de Tralleno** : 

Tralleno est un logiciel de gestion de tâches personnel. Il vous permet de créer des sections, et à l'intérieur de ces mêmes sections, de créer des tâches. Les tâches peuvent être reliées entre elles via des dépendances chronologiques et composites,  chaque tâche pouvant être composée d'autres tâches, et ce sans limites ! 

Vous retrouverez différents moyens d'afficher votre planning de tâches par sections :

- Une vue de base qui permet de voir les tâches sous forme d'un tableau de kanban.
- Une vue sous forme de liste.
- Une vue d'un diagramme de Gantt qui permet de visualiser une sélection de tâches et leurs dépendances.

Vous pouvez également archiver et restaurer des tâches et des sections, que vous retrouverez dans une vue spéciale dédiée à l'archivage.


## A propos du développement : 


### Choix de conception : 
**Archivage et restauration** :
Pour une tâche archivée plusieurs cas sont envisageables lors de sa restauration : 
Une tâche étant contenue dans une section, si on essaie de la restaurer et que : 
- La section a été supprimée entre temps, elle est recrée pour que la tâche restaurée puisse être contenue dedans.
- La section a été archivée également, alors lorsqu'on restaure la tâche, la section  est elle aussi restaurée.
- La section existe toujours, alors la tâche est tout simplement restaurée à l'intérieur de sa section parente.

Lorsqu'on archive une section, si elle contient des tâches, les tâches qu'elle contient sont également archivées.
Lorsqu'on restaure une tâche qui contenait des sous-tâches, ses sous-tâches sont également restaurées.
On ne peut pas archiver une sous-tâche, car selon notre conception, une sous-tâche n'est qu'un composant d'une réelle tâche.

**Suppression** : 
Lorsqu'on supprime une tâche ses sous-tâches sont également supprimées.
Lorsqu'on supprime une section, les tâches qu'elle contient sont également supprimées.

**Dépendances chronologiques** :
Nous avons décidé de n'accorder la possibilité de dépendance chronologique uniquement entre les tâches de plus haut niveau (les tâches directement contenues dans la section, et pas dans des tâches).
Lorsqu'une tâche du plus haut niveau devient sous-tâche d'une autre (drag and drop) alors elle perd toutes ses dépendances chronologiques et informations relatives aux dates de début et de fin.

### **Concernant les versions utilisées dans ce projet :**

- Java : [_version 17_](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- JUnit : [_version 5.8.1_](https://junit.org/junit5/docs/5.8.1/api/index.html)
- JavaFX Controls : [_version 17.0.6_](https://openjfx.io/javadoc/17/javafx.controls/module-summary.html)
- JavaFX Base : [_version 17.0.6_](https://openjfx.io/javadoc/17/javafx.base/module-summary.html)

### **Liens complémentaires :**

- Trello : https://trello.com/b/wHu9WCAP/sae-301
- Maquette Balsamiq : https://balsamiq.cloud/sdcu4x/pxdfi15/r047D
