mvn package 
sudo /usr/share/elasticsearch/bin/plugin -remove river-mysql-scala
sudo /usr/share/elasticsearch/bin/plugin install river-mysql-scala -url file:///home/marc/river/scala-mysql-river/target/releases/scala-mysql-river-1.0-SNAPSHOT.zip
curl -XDELETE 127.0.0.1:9200/_river/river-mysql-scala
sudo service elasticsearch restart
