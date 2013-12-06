curl -XDELETE 127.0.0.1:9200/_river/river-mysql-scala
sbt compile
sbt assembly
sudo /usr/share/elasticsearch/bin/plugin -remove river-mysql-scala
sudo /usr/share/elasticsearch/bin/plugin install river-mysql-scala -url file:///home/marc/river/scala-mysql-river/target/scala-2.10/scala-mysql-river-assembly-1.0.jar
sudo service elasticsearch restart
