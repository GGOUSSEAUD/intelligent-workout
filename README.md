# intelligent-workout

GOUSSEAUD GAËTAN
13402498
LE JEUNE VINCENT
14505788

&&IntelligentWorkoutMenu.java

Cette classe est la **surfaceview utilisée par ++MainActivity.java.
Elle implémente le menu principale du jeu, ici on peut choisir :
  - de jouer (bouton play en bleu)
  - de consulter les paramètres (bouton roue/engrenage)
  - de consulter l'"About" (bouton point d'interrogation)

Pour connaître la prochaine activité qui sera à lancer en fonction de l'appuye de l'utilisateur, nous utilisons un
**Listener implémenter dans notre **surfaceview que l'on appelle dans le **onTouchEvent.
Il nous reste à l'appeler dans l'activité grace à **setEventListener.


&&IntelligentWorkoutSettings.java

Cette classe est la surfaceview utilisée par ++MySettings.java.
Elle implémente le menu des paramètres du jeu, ici on peut choisir :
  - Couper ou non le volume de la music
  - Couper ou non le volume des bruitages
  - Revenir au menu principal


&&IntelligentWorkoutAbout.java

Cette classe est la **surfaceview utilisée par ++MyAbout.java.
Elle implémente uniquement le menu à propos, ici on ne peut rien vraiment choisir,
à par de revenir au menu principal, en revanche on peut voir les auteurs du jeu.


&&IntelligentWorkoutMenuLvl.java

Cette classe est la **surfaceview utilisée par ++Menulevel.java.
Elle implémente le menu des choix du niveau, ici l'utilisateur peut choisir le niveau
auquel il va jouer.


&&IntelligentWorkout.java

Cette classe est la **surfaceview utilisée par ++Inlevel.java.
C'est la classe principale du jeu : C'est la qu'un niveau se joueras.
C'est donc ici que nous aurons nos fonctions pour tester si le niveau
est gagné ou non, que l'on affichera le niveau (que nous avons précèdement
choisi dans le menu des niveaux), à la fin de la partie toujours dans la même **surfaceview,
nous afficherons les scores et les écrirons sur le telephone.
En cas de **onPause ou de **onStop les données sont sauvegardés sur le téléphone.

Le Jeu :

Il suffit d'appuyer sur une case de la ligne ou de la colonne que l'on souhaite déplacer et
bouger tout en restant appuyer du coté ou l'on veut que cette ligne ou colonne se déplace.
Le but étant detrouver le moyen de représenter le même plateau que sur le dessin.
Pour chaque déplacement, une variable **nbCoup sera incrémenté immédiatement, et sera
afficher dans notre **surfaceview.
Un timer sera aussi présent casiment aux mêmes endroits, et représenteras le temps joué sur un
niveau.


&&Tuple.java

C'est une classe qui est utilisé par ++IntelligentWorkout.java, elle sert uniquement
pour récuperer un tuple (comme son nom l'indique), ce tuple correspond pour nous à l'indice
x et y de la case ou l'utilisateur à appuyer sur le plateau (pendant une partie).


&&Les ressources

Pour les images, la plupart ont étés créer par nous même avec le logiciel GIMP.
Et pour quelques unes, nous les avons trouvées sur Google en utilisant l'option :
Libre de droits d'usage, de distribution ou de modification.

Pour les musiques en revanche :

  - blabla
  - blabla













