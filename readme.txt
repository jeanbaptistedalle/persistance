Pour récuperer ce tutoriel, utilisez les commandes suivantes : 
git init
git clone https://github.com/jeanbaptistedalle/hibernate.git

Une fois ceci fait, il vous faudrat initialiser votre base de données. Pour cela, il vous faudra créer une base nommée 'persistance' et posséder un utilisateur nommé 'root' et dont le mot de passe est 'root' (ou modifier les champs user et password dans le fichier hibernate.cfg.xml), puis executer les scripts .sql : d'abord Client.sql puis Commande.sql
