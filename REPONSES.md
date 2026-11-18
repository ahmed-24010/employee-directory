# REPONSES — TP05 REST CRUD en 3 couches

## Exercice 5.10 — Spring Data JPA vs DAO écrit à la main
Avec `EmployeeRepository extends JpaRepository<Employee, Integer>` on supprime :
- l'interface `EmployeeDAO` et la classe `EmployeeDAOJpaImpl` (ex. 5.2) : plus d'`EntityManager`, de `createQuery`, de `find`, `merge`, `remove` écrits à la main ;
- le code répétitif de CRUD : `findAll`, `findById` (renvoie un `Optional`), `save`, `deleteById` sont générés par Spring Data ;
- dans le service (ex. 5.3), le constructeur reste mais injecte `EmployeeRepository` et les méthodes ne font que déléguer ; on gère l'`Optional` de `findById`.
Le service `EmployeeService` garde son rôle (frontière `@Transactional`).

## Questions de compréhension
1. **Rôle des couches.** Contrôleur : reçoit/renvoie HTTP-JSON. Service : logique métier et transactions. DAO : accès à la base via `EntityManager`. Le contrôleur ne doit pas utiliser l'`EntityManager` pour respecter la responsabilité unique, rester indépendant de Hibernate et être testable (mock d'une couche à la fois).
2. **`@Transactional` sur le service.** Le service peut orchestrer plusieurs DAO dans une seule transaction. Ex. transfert d'argent : débit du compte A et crédit du compte B doivent réussir ou échouer ensemble ; si la transaction était sur chaque DAO, un débit pourrait être validé sans le crédit.
3. **`merge`.** Si `id == 0` (pas d'identité existante) → `INSERT` ; sinon → `UPDATE`. Dans le POST on force `id = 0` pour qu'un client qui enverrait un id ne déclenche pas une mise à jour déguisée d'un employé existant.
4. **PUT vs PATCH.** PUT remplace la ressource entière (champs absents écrasés à `null`). PATCH ne modifie que les champs envoyés. Pour changer seulement l'email : PATCH `{"email":"new@test.com"}` conserve prénom et nom ; avec PUT il faudrait renvoyer tous les champs sinon ils sont mis à `null`.
5. **`Map<String,Object>` pour PATCH.** Avec un `Employee`, les champs absents seraient `null` (ou `0` pour l'id) et on ne saurait pas distinguer « absent » de « mis à null ». La Map ne contient que les clés réellement envoyées.
6. **`updateValue`.** `jsonMapper.updateValue(existant, map)` applique sur l'objet existant uniquement les propriétés présentes dans la map et conserve les autres. Jackson 3 : package `tools.jackson.databind.json.JsonMapper` (au lieu de `com.fasterxml.jackson.databind.ObjectMapper` en Jackson 2).
7. **Garde-fou `containsKey("id")`.** Il interdit de modifier l'identifiant. Sans lui, un PATCH `{"id":99}` changerait l'id de l'objet et `merge` pourrait modifier/insérer un autre enregistrement.
8. **`@Repository`.** En plus du marquage de bean, il active la traduction des exceptions JDBC/JPA en `DataAccessException` de Spring.
9. **Avec `JpaRepository` disparaissent** : `EmployeeDAO` et `EmployeeDAOJpaImpl` (remplacés par l'interface `EmployeeRepository`).
