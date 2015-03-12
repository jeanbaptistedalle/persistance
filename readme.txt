Pour récuperer ce tutoriel, utilisez les commandes suivantes : 
git init
git clone https://github.com/jeanbaptistedalle/persistance.git

Une fois ceci fait, il vous faudrat initialiser votre base de données. Pour cela, il vous faudra créer une base nommée 'persistance' et posséder un utilisateur nommé 'root' et dont le mot de passe est 'root' (ou modifier les champs user et password dans le fichier java pour jdbc ou dans le fichier hibernate.cfg.xml hibernate), puis executer les scripts .sql : d'abord Client.sql puis Commande.sql.
Finalement, vous aurez besoin de divers package importé grâce à maven. Pour les importer, faites un clic droit sur le projet > Maven > Update maven > ok. Si le projet n'est pas initialisé pour maven, faite un clic droit sur le projet > Configure > add Maven nature puis retentez l'étape précédente.
