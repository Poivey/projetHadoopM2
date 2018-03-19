# projetHadoopM2

Website : https://hadoop-poivey.herokuapp.com/

## Introduction
Le but de ce projet était d'exploiter un gros volume de donnée fournis par la police de Chicago, pour pouvoir créer des graphiques représentants différentes statistiques de 2001 à aujourd'hui

Projet réalisé avec Hadoop 2.6.2 (windows) et 2.9 (linux et sur le cluster de l'université)

### Partie graphique

Les résultats graphiques ont été réalisés avec la librairie Google chart, les cartes avec Leaflet, Leaflet.Heat et Mapbox

### Développement

Site-rapport de présentation réalisé à l'aide du framework Bulma et hébergé sur Heroku
Le développement des tâches Hadoop a été fait en premier lieu en mode local, et ensuite en mode distribué sur le cluster. Le projet a été réalisé entièrement avec Hadoop et non Spark. Ce choix a été fait, car je me sentais plus à l'aise dans cette technologies car les travaux de map/reduce y sont plus séparés et explicites. Toutefois, ces programmes gagneraient beaucoup à être développés avec Spark, surtout le numéro 3 pour la partie kmeans, comme on a pu le constater en comparant l'exécution de ce projet avec celui d'autre groupes. 

## Question 1
Donnez le classement décroissant des catégories de crimes
Données techniques

Cette exécution a été réalisée sur le cluster Hadoop de l'UFR

### Temps de traitement

Total time spent by all map tasks (ms)=285160
Total time spent by all reduce tasks (ms)=16209

Map input records=6486072
Map output records=6486071

Reduce input records=6486071
Reduce output records=35

### Mapper

Mapper très classique en Hadoop.
Il parcourt toutes les lignes, extrait les crimes, et écrit dans le context sous la forme (crime, 1) pour pouvoir par la suite compter le nombre de crimes du même nom.

### Reducer

Le reducer instancie une Map dans sa phase de setup()
Dans la partie reduce() il compte le nombre d'éléments associé à chaque key (ici le nombre de 1), et ajoute à la Map une entrée (key, count), count est le nombre d'occurrences de la clé
Enfin dans la partie cleanUp(), les entrés de la Map sont triées et on écrit dans le context le nom du crime avec son nombre d'occurrences

### Analyse

Certains crimes composent la majeure partie des crimes de Chicago notamment les différents types de vols, d'aggresions et ensuite de stupéfiants.
Dans les types de crimes les plus rares, on peut voir certains doublons qu'il serait bon de rassembler pour améliorer l'analyse, mais ils sont tellement rares par rapport aux plus communs que la grande tendance ne changerait pas.

## Question 2
Donnez le nombre de crimes en fonction de 6 plages horaires (0-4, 4-8, 8-12, 12-16, 16-20, 20-24)
Données techniques

Cette exécution a été réalisée sur le cluster Hadoop de l'UFR

### Temps de traitement

Total time spent by all map tasks (ms)=280378
Total time spent by all reduce tasks (ms)=19099

Map input records=6486072
Map output records=6486071

Reduce input records=6486071
Reduce output records=6

### Mapper

Le mapper récupère le l'heure de chaque ligne et l'associe à un String ("0-4", "4-8"...)
Il écrit ensuite dans le context sous la forme (String, 1). Le String est une plage horaire, pour que le reducer puisse ensuite compter le nombre de crimes pour chaque plage.

### Reducer

Ce reducer est très similaire à celui de la question 1
Il instancie une Map dans sa phase de setup()
Dans la partie reduce() il compte le nombre d'éléments associé à chaque plage horaire (ici le nombre de 1), et ajoute à la Map une entrée (key, count)
Enfin dans la partie cleanUp(), les entrés de la Map sont triées et on écrit dans le context la plage horaire avec son nombre d'Question 2 Donnez le nombre de crimes en fonction de 6 plages horaires (0-4, 4-8, 8-12, 12-16, 16-20, 20-24) Données techniques Cette éxécution a été réalisée sur le cluster hadoop de l'ufr Temps de traitement Total time spent by all map tasks (ms)=280378 Total time spent by all reduce tasks (ms)=19099 Map input records=6486072 Map output records=6486071 Reduce input records=6486071 Reduce output records=6 Mapper Le mapper récupère le l'heure de chaque ligne et l'associe à un String ("0-4", "4-8"...) Il écrit ensuite dans le context sous la forme (String, 1). Le String est une plage horaire, pour que le reducer puisse ensuite compter le nombre de crime pour chaque plage. Reducer Ce reducer est très similaire à celui de la question 1 Il instancie une Map dans sa phase de setup() Dans la partie reduce() il compte le nombre d'éléments associé à chaque plage horaire (ici le nombre de 1), et ajoute à la Map une entrée (key, count) Enfin dans la partie cleanUp(), les entrés de la Map sont triées et on écrit dans le context la plage horaire avec son nombre d'occurrences

### Analyse

Plus de 60% des crimes ont lieu entre 12h et 00h avec une répartition homogène.
La nuit et le matin sont plus calmes, surtout entre 4h et 8h.

## Question 3
Donnez les 3 zones les plus dangereuses et les zones les moins dangereuses (rayon de 2 kms)
Données techniques

Cette exécution a été réalisée ma machine personnelle. En effet, le programme réalisé était trop demandeur en mémoire vive et une exception apparaissait : "Java heap space" lors de la phase de mapping

La classe principale définie des centroids en itérant sur le Job 1 (maximum : 25), nous avons choisi d'implémenter un algorithme de kmeans avec 10 centroids puis passe les centroids au Job 2 pour déterminer le top 3 des centroids les plus et moins criminels

Cette implémentation de l'algorithme de kmeans avec Hadoop a été pour beaucoup inspiré par la solution de Andrea Iacono

### Temps de traitement

N'ayant pas exécuté le programme dans un environnement distribué, nous n'avons pas pu récupérer de données précises à propos du temps d'exécution
Nous avons mesuré en moyenne un temps à la seconde près via le temps système d'une itération du Job 1 avec notre gros jeu de données : 42 secondes
Avec ce jeu de données, le Job 1 atteint le maximum d'itération avant de ne plus être rappelé : 25
On peut estimer alors le temps de déterminer les clusters à 17 minutes et 30 secondes

Le Job 2 est lui très rapide, mesurer son temps à la seconde près ne présente que peu d'intérêt

### Job 1 - Algorithme de kmeans

Map input records=6486072
Map output records=6398896

Reduce input records=6398896
Reduce output records=10

#### Mapper

Le mapper lit le jeu de donnée en input, et lit également un fichier de 10 centroids de départ dont le chemin est passé en cache
Il applique ensuite la première partie de l'algorithme des kmeans en associant à chaque localisation le centroid le plus proche
Pour chaque crime, il écrit ensuite dans le context le tuple clé valeur : (index du centroid, localisation)

#### Reducer

Le reducer reçoit donc 10 ensembles de localisation associés à chaque index de centroids (Par exemple, centroid 0 : [locA, locB, locC,...])
Ce reducer n'est composé que d'une fonction reduce()
Pour chaque index de centoid, il calcule la moyenne des localisations associées et défini alors des nouvelles coordonnées pour le centroid donné
Il calcule ensuite combien de localisation, parmi celles reçues pour chaque centroid, sont à moins de 2km des nouvelles coordonnées
Avec cette information, il écrit alors dans le context pour chaque index de centroid : (index, lattitude longitude nombreCrimeRayon2km)

Par la suite, le programme principale va comparer les centroids passés en cache au Job 1 à ceux produits par ce reducer. S'ils sont identiques alors le programme passe au Job suivant, sinon il met à jour le fichier de centroids de référence pour le mapper et rappel le Job 1. (Si le maximum d'itération est atteint alors on passe au Job suivant)

### Job 2 - Top 3 des zones les plus et moins dangereuses

Map input records=10
Map output records=10

Reduce input records=10
Reduce output records=6

#### Mapper

Ici le mapper prend les 10 centroids trouvés et les transmets touts au reducer en enlevant leur index (les centroids sont indéxés de 0 à 9 dans le fichier)

#### Reducer

Le reducer est simple, dans la partie setup() il instancie une Map. Dans la partie reduce() il ajoute chaque centroid dans la Map (localisation, nombre de crimes dans les 2km) Dans la partie cleanUp(), les entrées de la Map sont triées en fonction de leur valeur (le nombre de crimes), les 3 centroids les plus et moins criminels sont ensuite écrits dans le context (localisation, nombre de crimes)

### Analyse

Les zones les moins criminelles de Chicago ont en moyenne un peu plus du tiers du nombre de crimes des zones les plus criminelles. Ce résultat a été obtenu avec un algorithme de kmeans avec 10 groupes.

## Question 4
Donnez la répartition géographique des crimes commis/élucidés
Données techniques

Cette exécution a été réalisée sur le cluster Hadoop de l'UFR

### Temps de traitement

Total time spent by all map tasks (ms)=295905
Total time spent by all reduce tasks (ms)=35330

Map input records=6486072
Map output records=6398896

Reduce input records=6398896
Reduce output records=1174510

### Mapper

Pour cette tâche, le mapper est très simple, il se content d'extraire la localisation de chaque crime et son attribut "arrest"
Il vérifie ensuite l'intégrité de ces éléments, puis écrit dans le context sous la forme (localisation + " " + arrest, 1)

### Reducer

Le reducer compte le nombre d'occurrences de chaque clé (une clé est un lieu et un booléen arrest), afin d'avoir un nombre d'occurence pour chaque localisation en fonction du booléen d'arrestation

### Analyse

Ces deux cartes montrent une claire tendance à Chicago. L'immense majorité des crimes ne sont jamais élucidés, dans beaucoup de zones de la ville.

Le chargement des cartes peut être très long, car elles contiennent beaucoup de données (1 174 510 coordonnées). Pour pouvoir avoir une chargement plus rapide sans perdre beaucoup en précision il faudrait faire des groupes avec un kmeans ou simplement quadriller la ville et indiquer une tendance dans chaque groupe (pourcentage de crime non élucidé). Ainsi, il serait possible de faire environ 10 000 groupes, fichier qui serait bien plus rapide à charger, sans perdre en précision pour générer la heat-map.

## Question 5
Donnez le top 3 des mois les plus concernés par les cas de crimes
Données techniques

Cette exécution a été réalisée sur le cluster Hadoop de l'UFR

### Temps de traitement

Total time spent by all map tasks (ms)=253412
Total time spent by all reduce tasks (ms)=20481

Map input records=6486072
Map output records=144

Reduce input records=144
Reduce output records=3

### Mapper

Le mapper instancie une Map dans sa partie setup()
Dans la partie map() il récupère le mois de chaque ligne et ajoute ce mois à la Map instancié, ce qui lui permet de compter le nombre d'occurrences de chaque mois.
Enfin dans la partie cleanUp(), il parcourt la Map pour écrire dans le context des tuples (mois, nombre d'occurrences)

Dans les logs de l'exécution du programme, on remarque la ligne "Launched map tasks=12" qui veut dire que le Job a été fait avec 12 mapper, c'est pour cela qu'il y a 144 entrées dans le reduce (12 mois * 12 mapper = 144 entrées)

### Reducer

Le reducer instancie une PriorityQueue de taille 3 dans sa phase de setup()
Dans la partie reduce() il fait le total du compte des mapper pour chaque mois et ajoute le tuple (mois, nombre d'occurences) à la priority queue
Enfin dans la partie cleanUp(), les trois éléments triés de la priority queue sont écrits dans le context

### Analyse

Ce diagramme en colonne montre que les trois mois les plus criminels sur cette période le sont quasiment autant.
Le nombre de crimes au mois de mai correspond exactement au nombre de crimes au mois de juillet à 4,4% près.

## Question 5 alternative
Donnez le top 3 des mois les plus concernés par les cas de crimes, pour chaque crime
Données techniques

Cette exécution a été réalisée sur le cluster Hadoop de l'UFR

### Temps de traitement

Total time spent by all map tasks (ms)=260720
Total time spent by all reduce tasks (ms)=14620

Map input records=6486072
Map output records=3999

Reduce input records=3999
Reduce output records=103

### Mapper

Le mapper instancie une Map dans sa partie setup()
Dans la partie map() il récupère le mois et crimes de chaque ligne et ajoute cette clé (crime et mois) à la Map instanciée, ce qui lui permet de compter le nombre d'occurrences de chaque crime pour chaque mois.
Enfin dans la partie cleanUp(), il parcourt les entrées de la Map pour écrire dans le context des tuples de forme (crime, "mois:nombre d'occurrences ") qui seront traités par le reducer


### Reducer

Le reducer instancie une PriorityQueue de taille 3 dans sa phase de reduce()
Il fait ensuite pour chaque clé (crime) le total du compte du nombre de crime pour chaque mois à l'aide d'une Map. Enfin, il ajoute tous les tuples (mois, nombre d'occurrences) de la Map à la priority queue
Ensuite il parcours les trois éléments (maximum) de la priority queue, pour écrire dans le context des lignes de la forme (crime:mois, count) (crime étant la clé, mois et count étant les informations qui étaient dans la Map)

Le reducer fais cela pour chaque clé (crime) étant donné que tout est dans la partie reducer, ainsi on peut avoir un top 3 des moins les plus criminels par crime.

### Analyse

L'intérêt de ces graphiques est la recherche de certaines tendances dans les occurences de certain crimes.
On remarque par exemple que les mois les plus criminels pour coup et blessures (battery) ont tous à peu près le même nombre de crimes, comme pour beaucoup des crimes les plus communs.
Les cas de paris illégaux semblent être réguliers l'été avec une légère augmentation en août.
Il semble possible avec certains graphique avec peu de crime de tirer des conclusions, mais ces tendances avec peu de données ne sont pas significatives. Beaucoup de types de crime avec peu de données pourraient être regroupés pour être plus juste, mais au vu de leur rareté les résultats ne changeraient pas.